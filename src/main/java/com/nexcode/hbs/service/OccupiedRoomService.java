package com.nexcode.hbs.service;

import java.util.List;

import com.nexcode.hbs.model.dto.OccupiedRoomDto;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;

public interface OccupiedRoomService {

	List<OccupiedRoomDto> getOccupiedRoomsWithFilters(String monthFilter, OccupiedRoomStatus status, String type, String checkInDate, String checkOutDate);

	List<OccupiedRoomDto> getCurrentOccupiedRooms();

	List<OccupiedRoomDto> getCurrentMonthHistory();

	void setOccupiedRoom(Long id, Room room);

	void checkOutRoom(Long id);

}
