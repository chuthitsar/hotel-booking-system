package com.nexcode.hbs.model.mapper;

import java.util.List;

import com.nexcode.hbs.model.dto.ReservedRoomDto;
import com.nexcode.hbs.model.entity.ReservedRoom;
import com.nexcode.hbs.model.response.ReservedRoomResponse;

public interface ReservedRoomMapper {

	ReservedRoomDto mapToDto(ReservedRoom reservedRoom);
	
	List<ReservedRoomDto> mapToDto(List<ReservedRoom> reservedRooms);
	
	ReservedRoomResponse mapToResponse(ReservedRoomDto reservedRoomDto);
	
	List<ReservedRoomResponse> mapToResponse(List<ReservedRoomDto> reservedRoomDtos);

}
