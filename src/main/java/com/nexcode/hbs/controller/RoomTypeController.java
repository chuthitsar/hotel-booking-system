package com.nexcode.hbs.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

import com.nexcode.hbs.model.dto.RoomTypeDto;
import com.nexcode.hbs.model.mapper.RoomTypeAvailabilityMapper;
import com.nexcode.hbs.model.mapper.RoomTypeMapper;
import com.nexcode.hbs.model.request.RoomTypeAvailabilityRequest;
import com.nexcode.hbs.model.request.RoomTypeRequest;
import com.nexcode.hbs.model.response.ApiResponse;
import com.nexcode.hbs.model.response.RoomTypeAvailabilityResponse;
import com.nexcode.hbs.model.response.RoomTypeResponse;
import com.nexcode.hbs.service.RoomTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

	private final RoomTypeService roomTypeService;
	
	private final RoomTypeMapper roomTypeMapper;
	
	private final RoomTypeAvailabilityMapper roomTypeAvailabilityMapper;
	
	@PostMapping
	public ResponseEntity<ApiResponse> addRoomType(@RequestBody RoomTypeRequest roomTypeRequest) {
		roomTypeService.createRoomType(roomTypeMapper.mapToDto(roomTypeRequest));
		return new ResponseEntity<>(new ApiResponse(true, "A new room type is created successfully."), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateRoomType(@PathVariable Long id, @RequestBody RoomTypeRequest roomTypeRequest) {
		roomTypeService.updateRoomType(id, roomTypeMapper.mapToDto(roomTypeRequest));
		return new ResponseEntity<>(new ApiResponse(true, "Room Type updated successfully."), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<RoomTypeResponse>> getAllRoomType() {
		List<RoomTypeDto> roomTypeDtos = roomTypeService.getAllRoomTypes();
		return ResponseEntity.ok(roomTypeMapper.mapToResponse(roomTypeDtos));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoomTypeResponse> getRoomTypeById(@PathVariable Long id) {
		RoomTypeDto roomType = roomTypeService.getRoomTypeById(id);
		return ResponseEntity.ok(roomTypeMapper.mapToResponse(roomType));
	}
	
	@PostMapping("/availability")
	public ResponseEntity<List<RoomTypeAvailabilityResponse>> getRoomTypeAvailability(@RequestBody RoomTypeAvailabilityRequest request) {
		Instant checkIn = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(request.getCheckInTime())).atZone(ZoneId.of("UTC")).toInstant();
	    Instant checkOut = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(request.getCheckOutTime())).atZone(ZoneId.of("UTC")).toInstant();
		
	    return new ResponseEntity<>(
				roomTypeAvailabilityMapper.mapToResponse(roomTypeService.getAvailableRoomTypes(checkIn, checkOut)), 
				HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteRoomTypeById(@PathVariable Long id) {
		roomTypeService.deleteRoomTypeById(id);
		return new ResponseEntity<>(new ApiResponse(true, "Room type deleted successfully."), HttpStatus.OK);
	}
}
