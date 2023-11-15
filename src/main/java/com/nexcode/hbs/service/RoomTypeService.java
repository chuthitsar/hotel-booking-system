package com.nexcode.hbs.service;

import java.time.Instant;
import java.util.List;

import com.nexcode.hbs.model.dto.RoomTypeAvailabilityDto;
import com.nexcode.hbs.model.dto.RoomTypeDto;

public interface RoomTypeService {

	public RoomTypeDto createRoomType(RoomTypeDto roomTypeDto);
	
	public RoomTypeDto updateRoomType(Long id, RoomTypeDto roomTypeDto);
	
	public List<RoomTypeDto> getAllRoomTypes();

	public RoomTypeDto getRoomTypeById(Long id);

	public void deleteRoomTypeById(Long id);
	
	public List<RoomTypeAvailabilityDto> getAvailableRoomTypes(Instant checkInDate, Instant checkOutDate);

	public RoomTypeDto getRoomTypeByName(String roomType);
	
	public void updateTotalRoomCount(Long roomTypeId);

}
