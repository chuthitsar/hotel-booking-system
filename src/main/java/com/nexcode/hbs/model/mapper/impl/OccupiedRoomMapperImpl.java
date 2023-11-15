package com.nexcode.hbs.model.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.GuestInfoDto;
import com.nexcode.hbs.model.dto.OccupiedRoomDto;
import com.nexcode.hbs.model.entity.OccupiedRoom;
import com.nexcode.hbs.model.mapper.GuestInfoMapper;
import com.nexcode.hbs.model.mapper.OccupiedRoomMapper;
import com.nexcode.hbs.model.mapper.RoomMapper;
import com.nexcode.hbs.model.response.OccupiedRoomResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OccupiedRoomMapperImpl implements OccupiedRoomMapper {
	
	private final RoomMapper roomMapper;
	
	private final GuestInfoMapper guestInfoMapper;

	@Override
	public OccupiedRoomDto mapToDto(OccupiedRoom occupiedRoom) {
		
		if (occupiedRoom == null) {
			return null;
		}
		
		OccupiedRoomDto occupiedRoomDto = new OccupiedRoomDto();
		occupiedRoomDto.setId(occupiedRoom.getId());
		occupiedRoomDto.setGuestInfo(guestInfoMapper.mapToDto(occupiedRoom.getGuestInfo()));
		occupiedRoomDto.setRoomDto(roomMapper.mapToDto(occupiedRoom.getRoom()));
		occupiedRoomDto.setReservationId(occupiedRoom.getReservation().getId());
		occupiedRoomDto.setCheckIn(occupiedRoom.getCheckIn());
		occupiedRoomDto.setCheckOut(occupiedRoom.getCheckOut());
		occupiedRoomDto.setStatus(occupiedRoom.getStatus());
		
		return occupiedRoomDto;
	}
	
	@Override
	public List<OccupiedRoomDto> mapToDto(List<OccupiedRoom> occupiedRooms) {
		
		if (occupiedRooms == null) {
			return null;
		}
		
		List<OccupiedRoomDto> occupiedRoomDtos = new ArrayList<>();
		for (OccupiedRoom occupiedRoom : occupiedRooms) {
			occupiedRoomDtos.add(mapToDto(occupiedRoom));
		}
		return occupiedRoomDtos;
	}
	
	@Override
	public OccupiedRoomResponse mapToResponse(OccupiedRoomDto occupiedRoomDto) {
		
		if (occupiedRoomDto == null) {
			return null;
		}
		
		OccupiedRoomResponse occupiedRoomResponse = new OccupiedRoomResponse();
		occupiedRoomResponse.setId(occupiedRoomDto.getId());
		occupiedRoomResponse.setReservationId(occupiedRoomDto.getReservationId());
		
		GuestInfoDto guestInfo = occupiedRoomDto.getGuestInfo();
		occupiedRoomResponse.setGuestName(guestInfo.getName());
		occupiedRoomResponse.setGuestPhone(guestInfo.getPhone());
		occupiedRoomResponse.setGuestEmail(guestInfo.getEmail());
		occupiedRoomResponse.setRoomNumber(occupiedRoomDto.getRoomDto().getNumber());
		occupiedRoomResponse.setRoomType(occupiedRoomDto.getRoomDto().getRoomTypeName());
		occupiedRoomResponse.setCheckIn(occupiedRoomDto.getCheckIn());
		occupiedRoomResponse.setCheckOut(occupiedRoomDto.getCheckOut());
		occupiedRoomResponse.setStatus(occupiedRoomDto.getStatus());
		
		return occupiedRoomResponse;
	}

	@Override
	public List<OccupiedRoomResponse> mapToResponse(List<OccupiedRoomDto> occupiedRoomDtos) {
		
		if (occupiedRoomDtos == null) {
			return null;
		}
		
		List<OccupiedRoomResponse> occupiedRoomResponses = new ArrayList<>();
		for (OccupiedRoomDto occupiedRoomDto : occupiedRoomDtos) {
			occupiedRoomResponses.add(mapToResponse(occupiedRoomDto));
		}
		return occupiedRoomResponses;
	}

}
