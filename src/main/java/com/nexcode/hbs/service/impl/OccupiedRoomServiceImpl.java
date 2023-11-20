package com.nexcode.hbs.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nexcode.hbs.model.dto.OccupiedRoomDto;
import com.nexcode.hbs.model.entity.OccupiedRoom;
import com.nexcode.hbs.model.entity.Reservation;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;
import com.nexcode.hbs.model.entity.status.ReservationStatus;
import com.nexcode.hbs.model.exception.NoContentException;
import com.nexcode.hbs.model.exception.RecordNotFoundException;
import com.nexcode.hbs.model.mapper.OccupiedRoomMapper;
import com.nexcode.hbs.repository.OccupiedRoomRepository;
import com.nexcode.hbs.repository.ReservationRepository;
import com.nexcode.hbs.service.OccupiedRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OccupiedRoomServiceImpl implements OccupiedRoomService {
	
	private final OccupiedRoomRepository occupiedRoomRepository;
	
	private final ReservationRepository reservationRepository;
	
	private final OccupiedRoomMapper occupiedRoomMapper;

	@Override
	public List<OccupiedRoomDto> getOccupiedRoomsWithFilters(String monthFilter, OccupiedRoomStatus status, String type, String checkInDate, String checkOutDate) {
		
		Integer month = null;
		Integer year = null;
		if (monthFilter != null) {
			YearMonth monthFilterFormat = YearMonth.parse(monthFilter, DateTimeFormatter.ofPattern("yyyy-MM"));
			month = monthFilterFormat.getMonthValue();
			year = monthFilterFormat.getYear();
		}
		
		LocalDate checkIn = null;
		LocalDate checkOut = null;
		if (checkInDate != null) {
			checkIn = LocalDate.parse(checkInDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		if (checkOutDate != null) {
			checkOut = LocalDate.parse(checkOutDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		List<OccupiedRoom> occupiedRooms = occupiedRoomRepository.findWithFilters(month, year, status, type, checkIn, checkOut);
		
		return occupiedRoomMapper.mapToDto(occupiedRooms);
	}

	@Override
	public List<OccupiedRoomDto> getCurrentOccupiedRooms() {
		
		List<OccupiedRoom> reservations = occupiedRoomRepository.findByStatus(OccupiedRoomStatus.CHECKED_IN)
				.orElseThrow(() -> new NoContentException("There is no completed reservation."));

		return occupiedRoomMapper.mapToDto(reservations);
	}

	@Override
	public List<OccupiedRoomDto> getCurrentMonthHistory() {
		
		YearMonth currentMonth = YearMonth.now();
		Instant startOfMonth = currentMonth.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
		Instant endOfMonth = currentMonth.atEndOfMonth().atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
		
		List<OccupiedRoom> occupiedRooms = occupiedRoomRepository.findByCheckInBetweenAndIsCompletedIsTrue(startOfMonth, endOfMonth)
				.orElseThrow(() -> new RecordNotFoundException("There is no reservations for this month."));
		
		return occupiedRoomMapper.mapToDto(occupiedRooms);
	}

	@Override
	public void setOccupiedRoom(Long id, Room room) {
		
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reservation Not Found with ID: " + id));
		
		OccupiedRoom occupiedRoom = new OccupiedRoom();
		
		occupiedRoom.setGuestInfo(reservation.getGuestInfo());
		occupiedRoom.setRoom(room);
		occupiedRoom.setReservation(reservation);
		occupiedRoom.setCheckIn(Instant.now());
		occupiedRoom.setCheckOut(reservation.getCheckOut());
		occupiedRoom.setStatus(OccupiedRoomStatus.CHECKED_IN);
		occupiedRoom.setIsCompleted(false);
		
		reservation.setStatus(ReservationStatus.COMPLETED);
		
		occupiedRoomRepository.save(occupiedRoom);
	}

}
