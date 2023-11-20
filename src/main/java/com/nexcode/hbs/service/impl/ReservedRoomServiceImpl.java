package com.nexcode.hbs.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.nexcode.hbs.model.dto.ReservedRoomDto;
import com.nexcode.hbs.model.entity.ReservedRoom;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.status.ReservationStatus;
import com.nexcode.hbs.model.entity.status.ReservedRoomStatus;
import com.nexcode.hbs.model.entity.status.RoomStatus;
import com.nexcode.hbs.model.exception.BadRequestException;
import com.nexcode.hbs.model.exception.InvalidStatusException;
import com.nexcode.hbs.model.exception.RecordNotFoundException;
import com.nexcode.hbs.model.mapper.ReservedRoomMapper;
import com.nexcode.hbs.repository.ReservedRoomRepository;
import com.nexcode.hbs.repository.RoomRepository;
import com.nexcode.hbs.service.OccupiedRoomService;
import com.nexcode.hbs.service.ReservedRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservedRoomServiceImpl implements ReservedRoomService {

	private final ReservedRoomRepository reservedRoomRepository;
	
	private final RoomRepository roomRepository;
	
	private final ReservedRoomMapper reservedRoomMapper;
	
	private final OccupiedRoomService occupiedRoomService;
	
	@Override
	public void updateStatus(Long id, String status) {
		
		ReservedRoom reservedRoom = reservedRoomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reserverd Room Not Found!"));
		
		Room room = reservedRoom.getRoom();
		switch(status) {
		
		case "CHECKED_IN":
			if (!reservedRoom.getReservation().getStatus().equals(ReservationStatus.CONFIRMED)) {
				throw new BadRequestException("The reservation is currently not CONFIRMED. Cannot Check In!");
			}
			
			if (!reservedRoom.getReservation().getStatus().equals(ReservationStatus.COMPLETED)) {
				reservedRoom.getReservation().setStatus(ReservationStatus.COMPLETED);
			}
			reservedRoom.setStatus(ReservedRoomStatus.CHECKED_IN);
			reservedRoom.setCheckIn(Instant.now());
			room.setStatus(RoomStatus.OCCUPIED);
			break;
			
		case "CHECKED_OUT":
			if (!reservedRoom.getStatus().equals(ReservedRoomStatus.CHECKED_IN)) {
				throw new BadRequestException("This room is currently not CHECKED_IN. Cannot Check Out!");
			}
			reservedRoom.setStatus(ReservedRoomStatus.CHECKED_OUT);
			reservedRoom.setCheckOut(Instant.now());
			room.setStatus(RoomStatus.AVAILABLE);
			break;
			
		case "CANCELED":
			if (reservedRoom.getStatus().equals(ReservedRoomStatus.PENDING)) {
				throw new BadRequestException("The reservation is currently PENDING. Cannot Cancel!");
			} else if (reservedRoom.getStatus().equals(ReservedRoomStatus.CHECKED_IN)) {
				room.setStatus(RoomStatus.AVAILABLE);
			}
			reservedRoom.setStatus(ReservedRoomStatus.CANCELED);
			break;
			
		default:
			throw new InvalidStatusException("Invalid Status: " + status);
		}
		
		reservedRoomRepository.save(reservedRoom);
	}

	@Override
	public void updateReservedRoomNumber(Long id, Integer roomNumber) {
		
		ReservedRoom reservedRoom = reservedRoomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reserverd Room Not Found!"));
		
		Room room = roomRepository.findByNumber(roomNumber)
				.orElseThrow(() -> new RecordNotFoundException("Room Not Found With Number: " + roomNumber));
		
		Integer reservedRoomNumber = reservedRoom.getRoom().getNumber();
		if (reservedRoomNumber.equals(roomNumber)) {
			reservedRoom.setRoom(room);	
		} else if (!roomRepository.findAvailableRoomsByTypeAndDate(
				reservedRoom.getRoom().getType().getName(), 
				reservedRoom.getCheckIn(), 
				reservedRoom.getCheckOut()).contains(room)) {
			throw new BadRequestException("Room Unavailable! Please choose another room.");
		} else if (room.getStatus() == RoomStatus.OCCUPIED || room.getStatus() == RoomStatus.MAINTAINED) {
			throw new BadRequestException("This room is no longer available. Choose another room.");
		}
		reservedRoom.setRoom(room);
		reservedRoomRepository.save(reservedRoom);
	}

	@Override
	public List<ReservedRoomDto> getAllReservedRooms() {
		
		List<ReservedRoom> reservedRooms = reservedRoomRepository.findAll();
		
		return reservedRoomMapper.mapToDto(reservedRooms);
	}
	
//	@Override
//	public List<ReservedRoomDto> getAllReservedRoomsByFilter(String status, String type, Instant checkInDate, Instant checkOutDate) {
//		
//		List<ReservedRoom> reservedRooms = reservedRoomRepository.findAll(Specification.where(ReservedRoomSpecifications.hasStatus(status))
//				.and(ReservedRoomSpecifications.hasType(type))
//				.and(ReservedRoomSpecifications.hasCheckInDate(checkInDate))
//				.and(ReservedRoomSpecifications.hasCheckOutDate(checkOutDate)));
//		
//		return reservedRoomMapper.mapToDto(reservedRooms);
//	}
	
	@Override
	public List<ReservedRoomDto> getReservedRoomsWithFilters(String status, String type, String checkInDate, String checkOutDate) {
		
		ReservedRoomStatus roomStatus = null;
	    if (status != null) {
	        try {
	            roomStatus = ReservedRoomStatus.valueOf(status.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            throw new InvalidStatusException("Invalid room status: " + status);
	        }
	    }
	    
	    LocalDate checkIn = null;
		LocalDate checkOut = null;
		if (checkInDate != null) {
			checkIn = LocalDate.parse(checkInDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		if (checkOutDate != null) {
			checkOut = LocalDate.parse(checkOutDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		
		List<ReservedRoom> reservedRooms = reservedRoomRepository.findWithFilters(type, roomStatus, checkIn, checkOut);
		
		return reservedRoomMapper.mapToDto(reservedRooms);
	}

	@Override
	public ReservedRoomDto getReservedRoomById(Long id) {
		
		ReservedRoom reservedRoom = reservedRoomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reserved Room Not Found with id: " + id));
		
		return reservedRoomMapper.mapToDto(reservedRoom);
	}

	@Override
	public List<ReservedRoomDto> getReservedRoomsByReservationId(Long id) {

		List<ReservedRoom> reservedRooms = reservedRoomRepository.findByReservationId(id);
		
		if (reservedRooms.isEmpty()) {
			throw new BadRequestException("There is no reserved room with reservation id: " + id);
		}
		
		return reservedRoomMapper.mapToDto(reservedRooms);
	}
	
	@Override
	public List<ReservedRoomDto> getReservedRoomsForMonth(Integer month, Integer year) {
		
		List<ReservedRoom> reservedRooms = reservedRoomRepository.findByMonth(month, year);
		return reservedRoomMapper.mapToDto(reservedRooms);
	}

	@Override
	public void checkInRoom(Long id) {
		
		ReservedRoom reservedRoom = reservedRoomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reserverd Room Not Found!"));
		
		if (reservedRoom.getStatus().equals(ReservedRoomStatus.CHECKED_IN)) {
			throw new BadRequestException("This reserved room is already checked-in!");
		} else if (reservedRoom.getStatus().equals(ReservedRoomStatus.CANCELED)) {
			throw new BadRequestException("This reserved room is already canceled. Cannot Check In!");
		}
		
		Room room = reservedRoom.getRoom();
		ReservationStatus reservationStatus = reservedRoom.getReservation().getStatus();
		
		if (!reservationStatus.equals(ReservationStatus.COMPLETED)) {
			if (reservationStatus.equals(ReservationStatus.CONFIRMED)) {
				reservedRoom.getReservation().setStatus(ReservationStatus.COMPLETED);
			} else {
				throw new BadRequestException("The reservation is currently not CONFIRMED. Cannot Check In!");
			}
		}
		
		if (!room.getStatus().equals(RoomStatus.AVAILABLE)) {
			throw new BadRequestException("The room is currently unavailable!");
		}
		room.setStatus(RoomStatus.OCCUPIED);
		
		reservedRoom.setStatus(ReservedRoomStatus.CHECKED_IN);
		reservedRoom.setCheckIn(Instant.now());
		reservedRoomRepository.save(reservedRoom);
		
		occupiedRoomService.setOccupiedRoom(reservedRoom.getReservation().getId(), room);
	}

	@Override
	public void cancelReservedRoom(Long id) {

		ReservedRoom reservedRoom = reservedRoomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reserverd Room Not Found!"));

		Room room = reservedRoom.getRoom();

		if (reservedRoom.getStatus().equals(ReservedRoomStatus.PENDING)) {
			throw new BadRequestException("The reservation is currently PENDING. Cannot Cancel!");
		} else if (reservedRoom.getStatus().equals(ReservedRoomStatus.CHECKED_IN)) {
			room.setStatus(RoomStatus.AVAILABLE);
		}
		reservedRoom.setStatus(ReservedRoomStatus.CANCELED);

		reservedRoomRepository.save(reservedRoom);
	}

}
