package com.nexcode.hbs.model.mapper;

import java.util.List;

import com.nexcode.hbs.model.dto.OccupiedRoomDto;
import com.nexcode.hbs.model.entity.OccupiedRoom;
import com.nexcode.hbs.model.response.OccupiedRoomResponse;

public interface OccupiedRoomMapper {

	OccupiedRoomDto mapToDto(OccupiedRoom occupiedRoom);
	
	List<OccupiedRoomDto> mapToDto(List<OccupiedRoom> occupiedRooms);
	
	OccupiedRoomResponse mapToResponse(OccupiedRoomDto occupiedRoomDto);
	
	List<OccupiedRoomResponse> mapToResponse(List<OccupiedRoomDto> occupiedRoomDtos);

}
