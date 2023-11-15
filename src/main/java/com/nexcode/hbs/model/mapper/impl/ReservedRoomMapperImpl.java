package com.nexcode.hbs.model.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.ReservedRoomDto;
import com.nexcode.hbs.model.entity.ReservedRoom;
import com.nexcode.hbs.model.mapper.GuestInfoMapper;
import com.nexcode.hbs.model.mapper.ReservedRoomMapper;
import com.nexcode.hbs.model.mapper.RoomMapper;
import com.nexcode.hbs.model.response.ReservedRoomResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservedRoomMapperImpl implements ReservedRoomMapper {

	private final RoomMapper roomMapper;
	
	private final GuestInfoMapper guestInfoMapper;
	
	@Override
	public ReservedRoomDto mapToDto(ReservedRoom reservedRoom) {
		
		if (reservedRoom == null) {
			return null;
		}
		
		ReservedRoomDto reservedRoomDto = new ReservedRoomDto();
		reservedRoomDto.setId(reservedRoom.getId());
		reservedRoomDto.setRoomDto(roomMapper.mapToDto(reservedRoom.getRoom()));
		reservedRoomDto.setReservationId(reservedRoom.getReservation().getId());
		reservedRoomDto.setCheckIn(reservedRoom.getCheckIn());
		reservedRoomDto.setCheckOut(reservedRoom.getCheckOut());
		reservedRoomDto.setPricePerNight(reservedRoom.getPricePerNight());
		reservedRoomDto.setStatus(reservedRoom.getStatus());
		if (reservedRoom.getReservation() != null && reservedRoom.getReservation().getGuestInfo() != null) {
	        reservedRoomDto.setGuestInfo(guestInfoMapper.mapToDto(reservedRoom.getReservation().getGuestInfo()));
	    }
		
		return reservedRoomDto;
	}
	
	@Override
	public List<ReservedRoomDto> mapToDto(List<ReservedRoom> reservedRooms) {

		if (reservedRooms == null) {
			return null;
		}
		
		List<ReservedRoomDto> reservedRoomDtos = new ArrayList<>();
		for (ReservedRoom reservedRoom : reservedRooms) {
			reservedRoomDtos.add(mapToDto(reservedRoom));
		}
		return reservedRoomDtos;
	}
	
	@Override
	public ReservedRoomResponse mapToResponse(ReservedRoomDto reservedRoomDto) {

		if (reservedRoomDto == null) {
			return null;
		}
		
		ReservedRoomResponse reservedRoomResponse = new ReservedRoomResponse();
		reservedRoomResponse.setId(reservedRoomDto.getId());
		reservedRoomResponse.setRoomNumber(reservedRoomDto.getRoomDto().getNumber());
		reservedRoomResponse.setReservationId(reservedRoomDto.getReservationId());
		reservedRoomResponse.setCheckIn(reservedRoomDto.getCheckIn());
		reservedRoomResponse.setCheckOut(reservedRoomDto.getCheckOut());
		reservedRoomResponse.setPricePerNight(reservedRoomDto.getPricePerNight());
		reservedRoomResponse.setStatus(reservedRoomDto.getStatus().name());
		
		reservedRoomResponse.setRoomType(reservedRoomDto.getRoomDto().getRoomTypeName());
		
		return reservedRoomResponse;
	}

	@Override
	public List<ReservedRoomResponse> mapToResponse(List<ReservedRoomDto> reservedRoomDtos) {

		if (reservedRoomDtos == null) {
			return null;
		}
		
		List<ReservedRoomResponse> reservedRoomResponses = new ArrayList<>();
		for (ReservedRoomDto reservedRoomDto : reservedRoomDtos) {
			reservedRoomResponses.add(mapToResponse(reservedRoomDto));
		}
		return reservedRoomResponses;
	}

}
