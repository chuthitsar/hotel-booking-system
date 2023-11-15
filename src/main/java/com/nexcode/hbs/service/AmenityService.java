package com.nexcode.hbs.service;

import java.util.List;

import com.nexcode.hbs.model.dto.AmenityDto;

public interface AmenityService {

	public List<AmenityDto> getAllAmenities();
	
	public AmenityDto getAmenityById(Long id);
	
	public AmenityDto createAmentiy(AmenityDto amenityDto);

	public AmenityDto updateAmenity(Long id, AmenityDto amenityDto);
	
	public void deleteAmenity(Long id);

}
