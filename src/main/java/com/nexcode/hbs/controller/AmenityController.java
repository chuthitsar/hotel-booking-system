package com.nexcode.hbs.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexcode.hbs.model.dto.AmenityDto;
import com.nexcode.hbs.model.mapper.AmenityMapper;
import com.nexcode.hbs.model.response.AmenityResponse;
import com.nexcode.hbs.model.response.ApiResponse;
import com.nexcode.hbs.service.AmenityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityController {

	private final AmenityService amenityService;
	
	private final AmenityMapper amenityMapper;
	
	@PostMapping
	public ResponseEntity<ApiResponse> createAmenity(@RequestBody AmenityDto amenityDto){
		amenityService.createAmentiy(amenityDto);
		return new ResponseEntity<>(new ApiResponse(true, "Amenity created successfully"), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateAmenity(@PathVariable Long id, @RequestBody AmenityDto amenityDto) {
		amenityService.updateAmenity(id, amenityDto);
		return new ResponseEntity<>(new ApiResponse(true, "Amenity updated successfully"), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<AmenityResponse>> getAllAmenities(){
		List<AmenityDto> amenities = amenityService.getAllAmenities();
		return ResponseEntity.ok(amenityMapper.mapToResponse(amenities));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AmenityResponse> getAmenityById(@PathVariable Long id){
		AmenityDto amenity = amenityService.getAmenityById(id);
		return ResponseEntity.ok(amenityMapper.mapToResponse(amenity));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteAmenity(@PathVariable Long id){
		amenityService.deleteAmenity(id);
		return new ResponseEntity<>(new ApiResponse(true, "Amenity deleted successfully"), HttpStatus.NO_CONTENT);
	}
}
