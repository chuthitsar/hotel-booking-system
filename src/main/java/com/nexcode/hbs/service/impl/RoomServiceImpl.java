package com.nexcode.hbs.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.nexcode.hbs.model.dto.RoomDto;
import com.nexcode.hbs.model.entity.OccupiedRoom;
import com.nexcode.hbs.model.entity.Reservation;
import com.nexcode.hbs.model.entity.ReservedRoom;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.RoomType;
import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;
import com.nexcode.hbs.model.entity.status.ReservationStatus;
import com.nexcode.hbs.model.entity.status.ReservedRoomStatus;
import com.nexcode.hbs.model.entity.status.RoomStatus;
import com.nexcode.hbs.model.exception.BadRequestException;
import com.nexcode.hbs.model.exception.DuplicateEntityException;
import com.nexcode.hbs.model.exception.InvalidStatusException;
import com.nexcode.hbs.model.exception.NoContentException;
import com.nexcode.hbs.model.exception.RecordNotFoundException;
import com.nexcode.hbs.model.mapper.RoomMapper;
import com.nexcode.hbs.repository.OccupiedRoomRepository;
import com.nexcode.hbs.repository.ReservationRepository;
import com.nexcode.hbs.repository.ReservedRoomRepository;
import com.nexcode.hbs.repository.RoomRepository;
import com.nexcode.hbs.repository.RoomTypeRepository;
import com.nexcode.hbs.service.OccupiedRoomService;
import com.nexcode.hbs.service.RoomService;
import com.nexcode.hbs.service.RoomTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;

	private final RoomTypeRepository roomTypeRepository;
	
	private final ReservedRoomRepository reservedRoomRepository;
	
	private final OccupiedRoomRepository occupiedRoomRepository;
	
	private final ReservationRepository reservationRepository;

	private final RoomTypeService roomTypeService;
	
	private final OccupiedRoomService occupiedRoomService;

	private final RoomMapper roomMapper;

	@Override
	public RoomDto createRoom(RoomDto roomDto) {

		if (roomRepository.existsByNumber(roomDto.getNumber())) {
			throw new DuplicateEntityException("A room with the same number already exists: " + roomDto.getNumber());
		}
		Room room = new Room();
		room.setNumber(roomDto.getNumber());
		RoomType roomType = roomTypeRepository.findByName(roomDto.getRoomTypeName()).orElseThrow(
				() -> new RecordNotFoundException("Room type not found with Name: " + roomDto.getRoomTypeName()));
		room.setType(roomType);
		room.setFloor(roomDto.getFloor());
		room.setStatus(RoomStatus.AVAILABLE);
		room.setIsMaintained(false);

		roomRepository.save(room);

		roomTypeService.updateTotalRoomCount(roomType.getId());

		return roomMapper.mapToDto(room);
	}

	@Override
	@Transactional
	public List<RoomDto> createMultipleRooms(List<RoomDto> roomDtos) {

		for (RoomDto roomDto : roomDtos) {
			if (roomRepository.existsByNumber(roomDto.getNumber())) {
				throw new DuplicateEntityException(
						"A room with the same number already exists: " + roomDto.getNumber());
			}
		}

		List<Room> rooms = new ArrayList<>();
		for (RoomDto roomDto : roomDtos) {
			Room room = new Room();
			room.setNumber(roomDto.getNumber());
			RoomType roomType = roomTypeRepository.findByName(roomDto.getRoomTypeName()).orElseThrow(
					() -> new RecordNotFoundException("Room type not found with Name: " + roomDto.getRoomTypeName()));
			room.setType(roomType);
			room.setFloor(roomDto.getFloor());
			room.setStatus(RoomStatus.AVAILABLE);
			room.setIsMaintained(false);
			roomRepository.save(room);
			rooms.add(room);
			roomTypeService.updateTotalRoomCount(roomType.getId());
		}
		return roomMapper.mapToDto(rooms);
	}

	@Override
	public RoomDto updateRoom(Long id, RoomDto roomDto) {

		Room room = roomRepository.findById(id).orElseThrow(
				() -> new RecordNotFoundException("Room not found with the number: " + roomDto.getNumber()));
		
		if (!roomDto.getNumber().equals(room.getNumber())) {
			if (roomRepository.existsByNumber(roomDto.getNumber())) {
				throw new DuplicateEntityException(
						"A room with the same number already exists: " + roomDto.getNumber());
			}
		}
		room.setNumber(roomDto.getNumber());
		
		if (roomDto.getRoomTypeName() != null) {
			RoomType roomType = roomTypeRepository.findByName(roomDto.getRoomTypeName()).orElseThrow(
					() -> new RecordNotFoundException("Room type not found with Name: " + roomDto.getRoomTypeName()));
			room.setType(roomType);
		}
		
		room.setFloor(roomDto.getFloor());

		roomRepository.save(room);

		return roomMapper.mapToDto(room);
	}
	
	@Override
	public void maintainRoom(Long id, Boolean isMaintained) {
		
		Room room = roomRepository.findById(id).orElseThrow(
				() -> new RecordNotFoundException("Room not found with ID: " + id));
		
		if (isMaintained && !room.getIsMaintained()) {
			if (room.getStatus() == RoomStatus.OCCUPIED) {
				throw new BadRequestException("The room is already occupied! Cannot maintain the occupied room.");
			}
			room.setStatus(RoomStatus.MAINTAINED);
		} else if (isMaintained && room.getIsMaintained()) {
			throw new BadRequestException("The room is already maintained!");
		} else {
			if (!room.getIsMaintained()) {
				throw new BadRequestException("The room is not maintained yet!");
			}
			room.setStatus(RoomStatus.AVAILABLE);
		}
		room.setIsMaintained(isMaintained);
		
		roomRepository.save(room);
	}

	@Override
	public List<RoomDto> getAllRooms() {

		try {
			List<Room> rooms = roomRepository.findAll();
			return roomMapper.mapToDto(rooms);
		} catch (NullPointerException e) {
			throw new NoContentException("No Data!");
		}
	}
	
//	@Override
//	public List<RoomDto> getAllRoomsByFilter(String type, Integer floor, String status) {
//		
//		try {
//			List<Room> rooms = roomRepository.findAll(Specification.where(RoomSpecifications.hasType(type))
//	                .and(RoomSpecifications.hasFloor(floor))
//	                .and(RoomSpecifications.hasStatus(status)));
//			return roomMapper.mapToDto(rooms);
//		} catch (NullPointerException e) {
//			throw new NoContentException("No Data!");
//		}
//	}
	
	@Override
	public List<RoomDto> getRoomsWithFilters(String type, Integer floor, String status) {
		
		RoomStatus roomStatus = null;
	    if (status != null) {
	        try {
	            roomStatus = RoomStatus.valueOf(status.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            throw new InvalidStatusException("Invalid room status: " + status);
	        }
	    }
	    List<Room> rooms = roomRepository.findWithFilters(type, floor, roomStatus);
	    return roomMapper.mapToDto(rooms);
	}

	@Override
	public RoomDto getRoomById(Long id) {

		Room room = roomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Room not found with ID: " + id));

		return roomMapper.mapToDto(room);
	}

	@Override
	public void deleteRoomById(Long id) {

		Room room = roomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Room not found with ID: " + id));

		if (!room.getReservedRooms().isEmpty()) {
			throw new BadRequestException("The room cannot be deleted!");
		}
		RoomType roomType = room.getType();
		roomRepository.delete(room);
		roomTypeService.updateTotalRoomCount(roomType.getId());
	}

	@Override
	public List<RoomDto> getAvailableRoomsByTypeAndDate(Long reservedRoomId) {
		
		ReservedRoom reservedRoom = reservedRoomRepository.findById(reservedRoomId)
				.orElseThrow(() -> new RecordNotFoundException("Room not found with ID: " + reservedRoomId));
		
		List<Room> rooms = roomRepository.findAvailableRoomsByTypeAndDate(reservedRoom.getRoom().getType().getName(), reservedRoom.getCheckIn(), reservedRoom.getCheckOut());
		
		return roomMapper.mapToDto(rooms);
	}

	@Override
	public void checkOutRoom(Long id) {
		
		Room room = roomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Room not found with ID: " + id));
		
		if (!room.getStatus().equals(RoomStatus.OCCUPIED)) {
			throw new BadRequestException("This room is currently not CHECKED_IN. Cannot Check Out!");
		}
		OccupiedRoom occupiedRoom = occupiedRoomRepository.findByRoomAndStatus(room, OccupiedRoomStatus.CHECKED_IN)
				.orElseThrow(() -> new RecordNotFoundException("Occupied Room not found with number: " + room.getNumber()));
		
		occupiedRoom.setStatus(OccupiedRoomStatus.CHECKED_OUT);
		occupiedRoom.setCheckOut(Instant.now());
		occupiedRoom.setIsCompleted(true);
		occupiedRoomRepository.save(occupiedRoom);
		
		ReservedRoom reservedRoom = reservedRoomRepository.findByRoomAndStatus(room, ReservedRoomStatus.CHECKED_IN);
		if (reservedRoom != null) {
			reservedRoom.setStatus(ReservedRoomStatus.CHECKED_OUT);
			reservedRoom.setCheckOut(Instant.now());
			reservedRoomRepository.save(reservedRoom);
		}
		
		room.setStatus(RoomStatus.AVAILABLE);
		roomRepository.save(room);
	}
	
	@Override
	public List<RoomDto> getAvailableRoomsByDate(String reservationId) {
		
		Reservation reservation = reservationRepository.findByReservationID(reservationId)
				.orElseThrow(() -> new RecordNotFoundException("Reservation Not Found with ID: " + reservationId));
		
		if (reservation.getCheckOut().isBefore(Instant.now())) {
			throw new BadRequestException("Cannot find available rooms for past check out date!");
		}
		
		List<Room> rooms = roomRepository.findAvailableRoomsByDate(Instant.now(), reservation.getCheckOut());
		
		return roomMapper.mapToDto(rooms);
	}

	@Override
	public void checkInRoomWithReservationId(Long roomId, String reservationId) {
		
		Reservation reservation = reservationRepository.findByReservationID(reservationId)
				.orElseThrow(() -> new RecordNotFoundException("Reservation Not Found with ID: " + reservationId));
		
		if (!reservation.getStatus().equals(ReservationStatus.CONFIRMED) 
				&& !reservation.getStatus().equals(ReservationStatus.COMPLETED)) {
			throw new BadRequestException("Reservation is neither CONFIMRED nor COMPLETED! Cannot Change room!");
		}
		
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new RecordNotFoundException("Room not found with ID: " + roomId));
		
		if (!roomRepository.findAvailableRoomsByDate(Instant.now(), reservation.getCheckOut()).contains(room) 
				&& !room.getStatus().equals(RoomStatus.AVAILABLE)) {
			throw new BadRequestException("Room Unavailable! Please choose another room.");
		}
		room.setStatus(RoomStatus.OCCUPIED);
		occupiedRoomService.setOccupiedRoom(reservation.getId(), room);
	}

	@Override
	public Long countAvailableRooms() {
		
		Long roomCount = roomRepository.countByStatus(RoomStatus.AVAILABLE);
		return roomCount;
	}

	@Override
	public Long countOccupiedRooms() {
		
		Long roomCount = roomRepository.countByStatus(RoomStatus.OCCUPIED);
		return roomCount;
	}

}
