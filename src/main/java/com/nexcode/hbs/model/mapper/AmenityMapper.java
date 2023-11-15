package com.nexcode.hbs.model.mapper;

import java.util.List;

import com.nexcode.hbs.model.dto.AmenityDto;
import com.nexcode.hbs.model.entity.Amenity;
import com.nexcode.hbs.model.response.AmenityResponse;

public interface AmenityMapper {

	AmenityDto mapToDto(Amenity amenity);
	List<AmenityDto> mapToDto(List<Amenity> amenities);
	AmenityResponse mapToResponse(AmenityDto amenity);
	List<AmenityResponse> mapToResponse(List<AmenityDto> amenities);
}
