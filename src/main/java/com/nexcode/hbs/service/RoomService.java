package com.nexcode.hbs.service;

import java.util.List;

import com.nexcode.hbs.model.dto.RoomDto;

public interface RoomService {

	public RoomDto createRoom(RoomDto roomDto);
	
	public List<RoomDto> createMultipleRooms(List<RoomDto> list);
	
	public RoomDto updateRoom(Long id, RoomDto roomDto);
	
	public List<RoomDto> getAllRooms();

	public RoomDto getRoomById(Long id);

	public void deleteRoomById(Long id);

	public List<RoomDto> getAvailableRoomsByTypeAndDate(Long reservedRoomId);
	
	public List<RoomDto> getAvailableRoomsByDate(String reservationId);

	public List<RoomDto> getRoomsWithFilters(String type, Integer floor, String status);

	public void checkOutRoom(Long id);

	public void checkInRoomWithReservationId(Long roomId, String reservationId);

	public void maintainRoom(Long id, Boolean isMaintained);

}
