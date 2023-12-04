package com.nexcode.hbs.model.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.AmenityDto;
import com.nexcode.hbs.model.entity.Amenity;
import com.nexcode.hbs.model.mapper.AmenityMapper;
import com.nexcode.hbs.model.request.AmenityRequest;
import com.nexcode.hbs.model.response.AmenityResponse;

@Component
public class AmenityMapperImpl implements AmenityMapper {

	@Override
	public AmenityDto mapToDto(AmenityRequest amenityRequest) {
		if (amenityRequest == null) {
			return null;
		}

		// Mapping
		AmenityDto amenityDto = new AmenityDto();
		amenityDto.setName(amenityRequest.getName());
		amenityDto.setIcon(amenityRequest.getIcon());

		return amenityDto;
	}
	
	@Override
	public AmenityDto mapToDto(Amenity amenity) {
		if (amenity == null) {
			return null;
		}

		// Mapping
		AmenityDto amenityDto = new AmenityDto();
		amenityDto.setId(amenity.getId());
		amenityDto.setName(amenity.getName());
		amenityDto.setIcon(amenity.getIcon());

		return amenityDto;
	}

	@Override
	public List<AmenityDto> mapToDto(List<Amenity> amenities) {
		if (amenities == null) {
			return null;
		}

		List<AmenityDto> amenityDtos = new ArrayList<>();

		// Mapping
		for (Amenity amenity : amenities) {
			AmenityDto amenityDto = new AmenityDto();
			amenityDto = mapToDto(amenity);
			amenityDtos.add(amenityDto);
			
		}

		return amenityDtos;
	}

	@Override
	public AmenityResponse mapToResponse(AmenityDto amenity) {
		if (amenity == null) {
			return null;
		}

		// Mapping
		AmenityResponse amenityResponse = new AmenityResponse();
		amenityResponse.setId(amenity.getId());
		amenityResponse.setName(amenity.getName());
		amenityResponse.setIcon(amenity.getIcon());

		return amenityResponse;
	}

	@Override
	public List<AmenityResponse> mapToResponse(List<AmenityDto> amenities) {
		if (amenities == null) {
			return null;
		}

		List<AmenityResponse> amenityResponses = new ArrayList<>();

		// Mapping
		for (AmenityDto amenity : amenities) {
			AmenityResponse amenityResponse = new AmenityResponse();
			amenityResponse = mapToResponse(amenity);
			amenityResponses.add(amenityResponse);
		}

		return amenityResponses;
	}

}
