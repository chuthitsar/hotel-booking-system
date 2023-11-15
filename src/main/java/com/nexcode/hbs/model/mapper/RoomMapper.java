package com.nexcode.hbs.model.mapper;

import java.util.List;

import com.nexcode.hbs.model.dto.RoomDto;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.request.MultipleRoomRequest;
import com.nexcode.hbs.model.request.RoomRequest;
import com.nexcode.hbs.model.response.RoomResponse;

public interface RoomMapper {

	RoomDto mapToDto(Room room);
	
	List<RoomDto> mapToDto(List<Room> rooms);
	
	RoomDto mapToDto(RoomRequest roomRequest);
	
	List<RoomDto> mapToDtoList(MultipleRoomRequest roomRequests);
	
	RoomResponse mapToResponse(RoomDto roomDto);
	
	List<RoomResponse> mapToResponse(List<RoomDto> roomDtos);

}
