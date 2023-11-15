package com.nexcode.hbs.model.mapper;

import java.util.List;

import com.nexcode.hbs.model.dto.RoomTypeAvailabilityDto;
import com.nexcode.hbs.model.response.RoomTypeAvailabilityResponse;

public interface RoomTypeAvailabilityMapper {

	RoomTypeAvailabilityResponse mapToResponse(RoomTypeAvailabilityDto roomTypeAvailabilityDto);
	
	List<RoomTypeAvailabilityResponse> mapToResponse(List<RoomTypeAvailabilityDto> roomTypeAvailabilityDtos);
}
