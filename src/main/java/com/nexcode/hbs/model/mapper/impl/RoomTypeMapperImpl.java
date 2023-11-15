package com.nexcode.hbs.model.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.AmenityDto;
import com.nexcode.hbs.model.dto.RoomTypeDto;
import com.nexcode.hbs.model.entity.RoomType;
import com.nexcode.hbs.model.mapper.AmenityMapper;
import com.nexcode.hbs.model.mapper.RoomTypeMapper;
import com.nexcode.hbs.model.request.RoomTypeRequest;
import com.nexcode.hbs.model.response.RoomTypeResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomTypeMapperImpl implements RoomTypeMapper {
	
	private final AmenityMapper amenityMapper;

	@Override
	public RoomTypeDto mapToDto(RoomType roomType) {
		
		if (roomType == null) {
			return null;
		}

		// Mapping
		RoomTypeDto roomTypeDto = new RoomTypeDto();
		roomTypeDto.setId(roomType.getId());
		roomTypeDto.setName(roomType.getName());
		roomTypeDto.setMaximumCapacity(roomType.getMaximumCapacity());
		roomTypeDto.setSize(roomType.getSize());
		roomTypeDto.setPricePerNight(roomType.getPricePerNight());
		roomTypeDto.setDescription(roomType.getDescription());
		roomTypeDto.setTotalRoom(roomType.getTotalRoom());
		roomTypeDto.setImageUrl(roomType.getImageUrl());
		roomTypeDto.setAmenities(amenityMapper.mapToDto(roomType.getAmenities()));
		
		List<Long> amenityIds = roomTypeDto.getAmenities().stream()
		        .map(AmenityDto::getId)
		        .collect(Collectors.toList());

		roomTypeDto.setAmenityIds(amenityIds);
				
		return roomTypeDto;
	}

	@Override
	public List<RoomTypeDto> mapToDto(List<RoomType> roomTypes) {
		
		if (roomTypes == null) {
			return null;
		}
		
		List<RoomTypeDto> roomTypeDtos = new ArrayList<>();

		// Mapping
		for (RoomType roomType : roomTypes) {
			RoomTypeDto roomTypeDto = new RoomTypeDto();
			roomTypeDto = mapToDto(roomType);
			roomTypeDtos.add(roomTypeDto);	
		}

		return roomTypeDtos;
	}
	
	@Override
	public RoomTypeDto mapToDto(RoomTypeRequest roomTypeRequest) {
		
		if (roomTypeRequest == null) {
			return null;
		}

		// Mapping
		RoomTypeDto roomTypeDto = new RoomTypeDto();
		roomTypeDto.setName(roomTypeRequest.getName());
		roomTypeDto.setMaximumCapacity(roomTypeRequest.getMaximumCapacity());
		roomTypeDto.setSize(roomTypeRequest.getSize());
		roomTypeDto.setPricePerNight(roomTypeRequest.getPricePerNight());
		roomTypeDto.setDescription(roomTypeRequest.getDescription());
		roomTypeDto.setImageUrl(roomTypeRequest.getImageUrl());
		roomTypeDto.setAmenityIds(roomTypeRequest.getAmenityIds());
				
		return roomTypeDto;
	}

	@Override
	public RoomTypeResponse mapToResponse(RoomTypeDto roomTypeDto) {
		if (roomTypeDto == null) {
			return null;
		}

		// Mapping
		RoomTypeResponse roomTypeResponse = new RoomTypeResponse();
		roomTypeResponse.setId(roomTypeDto.getId());
		roomTypeResponse.setName(roomTypeDto.getName());
		roomTypeResponse.setMaximumCapacity(roomTypeDto.getMaximumCapacity());
		roomTypeResponse.setSize(roomTypeDto.getSize());
		roomTypeResponse.setPricePerNight(roomTypeDto.getPricePerNight());
		roomTypeResponse.setDescription(roomTypeDto.getDescription());
		roomTypeResponse.setTotalRoom(roomTypeDto.getTotalRoom());
		roomTypeResponse.setImageUrl(roomTypeDto.getImageUrl());
		roomTypeResponse.setAmenityIds(roomTypeDto.getAmenityIds());
				
		return roomTypeResponse;
	}

	@Override
	public List<RoomTypeResponse> mapToResponse(List<RoomTypeDto> roomTypeDtos) {
		
		if (roomTypeDtos == null) {
			return null;
		}
		
		List<RoomTypeResponse> roomTypeResponses = new ArrayList<>();

		// Mapping
		for (RoomTypeDto roomTypeDto : roomTypeDtos) {
			RoomTypeResponse roomTypeResponse = new RoomTypeResponse();
			roomTypeResponse = mapToResponse(roomTypeDto);
			roomTypeResponses.add(roomTypeResponse);	
		}

		return roomTypeResponses;
	}

}
