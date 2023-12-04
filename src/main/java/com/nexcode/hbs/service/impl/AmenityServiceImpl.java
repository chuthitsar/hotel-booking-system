package com.nexcode.hbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexcode.hbs.model.dto.AmenityDto;
import com.nexcode.hbs.model.entity.Amenity;
import com.nexcode.hbs.model.exception.DuplicateEntityException;
import com.nexcode.hbs.model.exception.RecordNotFoundException;
import com.nexcode.hbs.model.mapper.AmenityMapper;
import com.nexcode.hbs.repository.AmenityRepository;
import com.nexcode.hbs.service.AmenityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService{

	private final AmenityRepository amenityRepository;
	
	private final AmenityMapper amenityMapper;
	
	@Override
	public List<AmenityDto> getAllAmenities() {
		List<Amenity> amenities = amenityRepository.findAll();
		
		return amenityMapper.mapToDto(amenities);
	}
	
	@Override
	public AmenityDto getAmenityById(Long id) {
		
		Amenity amenity = amenityRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Amenity not found with id: " + id));
		return amenityMapper.mapToDto(amenity);
	}

	@Override
	public AmenityDto createAmentiy(AmenityDto amenityDto) {
		
		if (amenityRepository.existsByName(amenityDto.getName())) {
            throw new DuplicateEntityException("An amenity with the same name already exists!");
        }

        Amenity amenity = new Amenity();
        amenity.setName(amenityDto.getName());
        amenity.setIcon(amenityDto.getIcon());
        Amenity createdAmenity = amenityRepository.save(amenity);
        AmenityDto createdAmenityDto = amenityMapper.mapToDto(createdAmenity);
        
        return createdAmenityDto;
	}

	@Override
	public AmenityDto updateAmenity(Long id, AmenityDto amenityDto) {

		Amenity existingAmenity = amenityRepository.findById(id)
									.orElseThrow(() -> new RecordNotFoundException("Amenity not found with ID: " + id));
		
		existingAmenity.setName(amenityDto.getName());
		
		Amenity updatedAmenity = amenityRepository.save(existingAmenity);
	    AmenityDto updatedAmenityDto = amenityMapper.mapToDto(updatedAmenity);
		
	    return updatedAmenityDto;
	}

	@Override
	public void deleteAmenity(Long id) {

		Amenity existingAmenity = amenityRepository.findById(id)
									.orElseThrow(() -> new RecordNotFoundException("Amenity not found with ID: " + id));
	
		amenityRepository.delete(existingAmenity);
	}

}
