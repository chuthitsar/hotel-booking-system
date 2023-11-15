package com.nexcode.hbs.model.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.RoomDto;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.mapper.RoomMapper;
import com.nexcode.hbs.model.mapper.RoomTypeMapper;
import com.nexcode.hbs.model.request.MultipleRoomRequest;
import com.nexcode.hbs.model.request.RoomRequest;
import com.nexcode.hbs.model.response.RoomResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomMapperImpl implements RoomMapper {
	
	private final RoomTypeMapper roomTypeMapper;

	@Override
	public RoomDto mapToDto(Room room) {
		
		if (room == null) {
			return null;
		}

		// Mapping
		RoomDto roomDto = new RoomDto();
		roomDto.setId(room.getId());
		roomDto.setNumber(room.getNumber());
		roomDto.setType(roomTypeMapper.mapToDto(room.getType()));
		roomDto.setFloor(room.getFloor());
		roomDto.setStatus(room.getStatus());
		roomDto.setIsMaintained(room.getIsMaintained());
		roomDto.setRoomTypeName(roomDto.getType().getName());
		roomDto.setRoomTypeId(roomDto.getType().getId());
				
		return roomDto;
	}

	@Override
	public List<RoomDto> mapToDto(List<Room> rooms) {
		
		if (rooms == null) {
			return null;
		}
		
		List<RoomDto> roomDtos = new ArrayList<>();

		// Mapping
		for (Room room : rooms) {
			roomDtos.add(mapToDto(room));	
		}

		return roomDtos;
	}
	
	@Override
	public RoomDto mapToDto(RoomRequest roomRequest) {
		
		if (roomRequest == null) {
			return null;
		}

		// Mapping
		RoomDto roomDto = new RoomDto();
		roomDto.setNumber(roomRequest.getNumber());
		roomDto.setRoomTypeName(roomRequest.getType());
		roomDto.setFloor(roomRequest.getFloor());
		
		return roomDto;
	}
	
	@Override
	public List<RoomDto> mapToDtoList(MultipleRoomRequest multipleRoomRequests) {

		if (multipleRoomRequests == null) {
			return null;
		}
		
		List<RoomRequest> roomRequests = multipleRoomRequests.getRoomRequests();
		
		
		List<RoomDto> roomDtos = new ArrayList<>();

		// Mapping
		for (RoomRequest roomRequest : roomRequests) {
			roomDtos.add(mapToDto(roomRequest));	
		}

		return roomDtos;
	}

	@Override
	public RoomResponse mapToResponse(RoomDto roomDto) {
		
		if (roomDto == null) {
			return null;
		}

		// Mapping
		RoomResponse roomResponse = new RoomResponse();
		roomResponse.setId(roomDto.getId());
		roomResponse.setNumber(roomDto.getNumber());
		roomResponse.setType(roomDto.getRoomTypeName());
		roomResponse.setStatus(roomDto.getStatus());
		roomResponse.setFloor(roomDto.getFloor());
		roomResponse.setIsMaintained(roomDto.getIsMaintained());
				
		return roomResponse;
	}

	@Override
	public List<RoomResponse> mapToResponse(List<RoomDto> roomDtos) {
		
		if (roomDtos == null) {
			return null;
		}
		
		List<RoomResponse> roomResponses = new ArrayList<>();

		// Mapping
		for (RoomDto roomDto : roomDtos) {
			roomResponses.add(mapToResponse(roomDto));	
		}

		return roomResponses;
	}

}
