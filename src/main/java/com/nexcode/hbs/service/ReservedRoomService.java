package com.nexcode.hbs.service;

import java.time.Instant;
import java.util.List;

import com.nexcode.hbs.model.dto.ReservedRoomDto;

public interface ReservedRoomService {

	void updateStatus(Long id, String status);

	void updateReservedRoomNumber(Long id, Integer roomNumber);

	List<ReservedRoomDto> getAllReservedRooms();

	ReservedRoomDto getReservedRoomById(Long id);

	List<ReservedRoomDto> getReservedRoomsByReservationId(Long id);

	List<ReservedRoomDto> getReservedRoomsWithFilters(String status, String type, Instant checkInDate, Instant checkOutDate);

	List<ReservedRoomDto> getReservedRoomsForMonth(Integer month, Integer year);

	void checkInRoom(Long id);

	void cancelReservedRoom(Long id);

}
