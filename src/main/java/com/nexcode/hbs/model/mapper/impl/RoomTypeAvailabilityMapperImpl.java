package com.nexcode.hbs.model.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.RoomTypeAvailabilityDto;
import com.nexcode.hbs.model.mapper.RoomTypeAvailabilityMapper;
import com.nexcode.hbs.model.mapper.RoomTypeMapper;
import com.nexcode.hbs.model.response.RoomTypeAvailabilityResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomTypeAvailabilityMapperImpl implements RoomTypeAvailabilityMapper {

	private final RoomTypeMapper roomTypeMapper;
	
	@Override
	public RoomTypeAvailabilityResponse mapToResponse(RoomTypeAvailabilityDto roomTypeAvailabilityDto) {

		if(roomTypeAvailabilityDto == null) {
			return null;
		}
		
		RoomTypeAvailabilityResponse rtAvailabilityResponse = new RoomTypeAvailabilityResponse();
		
		rtAvailabilityResponse.setType(roomTypeMapper.mapToResponse(roomTypeMapper.mapToDto(roomTypeAvailabilityDto.getType())));
		rtAvailabilityResponse.setAvailable(roomTypeAvailabilityDto.getAvailable());
		
		return rtAvailabilityResponse;
	}

	@Override
	public List<RoomTypeAvailabilityResponse> mapToResponse(List<RoomTypeAvailabilityDto> roomTypeAvailabilityDtos) {

		if(roomTypeAvailabilityDtos == null) {
			return null;
		}
		
		List<RoomTypeAvailabilityResponse> responses = new ArrayList<>();
		
		for(RoomTypeAvailabilityDto roomTypeAvailabilityDto : roomTypeAvailabilityDtos) {
			RoomTypeAvailabilityResponse response = new RoomTypeAvailabilityResponse();
			response = mapToResponse(roomTypeAvailabilityDto);
			responses.add(response);
		}
		return responses;
	}

}
