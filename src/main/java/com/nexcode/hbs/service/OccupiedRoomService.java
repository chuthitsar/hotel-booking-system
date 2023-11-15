package com.nexcode.hbs.service;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

import com.nexcode.hbs.model.dto.OccupiedRoomDto;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;

public interface OccupiedRoomService {

	List<OccupiedRoomDto> getOccupiedRoomsWithFilters(YearMonth checkInMonth, OccupiedRoomStatus status, String type, Instant checkInDate, Instant checkOutDate);

	List<OccupiedRoomDto> getCurrentOccupiedRooms();

	List<OccupiedRoomDto> getCurrentMonthHistory();

	void setOccupiedRoom(Long id, Room room);

}
