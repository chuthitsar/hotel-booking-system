package com.nexcode.hbs.model.mapper;

import java.util.List;

import com.nexcode.hbs.model.dto.RoomTypeDto;
import com.nexcode.hbs.model.entity.RoomType;
import com.nexcode.hbs.model.request.RoomTypeRequest;
import com.nexcode.hbs.model.response.RoomTypeResponse;

public interface RoomTypeMapper {

	RoomTypeDto mapToDto(RoomType roomType);
	
	List<RoomTypeDto> mapToDto(List<RoomType> roomTypes);
	
	RoomTypeDto mapToDto(RoomTypeRequest roomTypeRequest);
	
	RoomTypeResponse mapToResponse(RoomTypeDto roomTypeDto);
	
	List<RoomTypeResponse> mapToResponse(List<RoomTypeDto> roomTypeDtos);
	
	
}
