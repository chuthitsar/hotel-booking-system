package com.nexcode.hbs.controller;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexcode.hbs.model.dto.OccupiedRoomDto;
import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;
import com.nexcode.hbs.model.mapper.OccupiedRoomMapper;
import com.nexcode.hbs.model.response.ApiResponse;
import com.nexcode.hbs.model.response.OccupiedRoomDetailsResponse;
import com.nexcode.hbs.service.OccupiedRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/occupied-rooms")
@RequiredArgsConstructor
public class OccupiedRoomController {

	private final OccupiedRoomService occupiedRoomService;
	
	private final OccupiedRoomMapper occupiedRoomMapper;
	
	//// Getting All Checked-In Rooms ////
	@GetMapping
	public ResponseEntity<List<OccupiedRoomDetailsResponse>> getOccupiedRooms(
			@RequestParam(required = false) String type,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String checkInDate, 
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String checkOutDate) {
		
		
		List<OccupiedRoomDto> occupiedRoomDtos = (type != null || checkInDate != null || checkOutDate != null)
				? occupiedRoomService.getOccupiedRoomsWithFilters(null, OccupiedRoomStatus.CHECKED_IN, type, checkInDate, checkOutDate)
				: occupiedRoomService.getCurrentOccupiedRooms();
		return ResponseEntity.ok(occupiedRoomMapper.mapToDetailsResponse(occupiedRoomDtos));
	}
	
	@GetMapping("/history")
	public ResponseEntity<List<OccupiedRoomDetailsResponse>> getOccupiedRoomsHistory(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") String monthFilter, 
			@RequestParam(required = false) String type,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String checkInDate, 
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String checkOutDate) {
		
		List<OccupiedRoomDto> occupiedRoomDtos = (monthFilter != null || type != null || checkInDate != null || checkOutDate != null)
				? occupiedRoomService.getOccupiedRoomsWithFilters(monthFilter, OccupiedRoomStatus.CHECKED_OUT, type, checkInDate, checkOutDate)
				: occupiedRoomService.getCurrentMonthHistory();
		return ResponseEntity.ok(occupiedRoomMapper.mapToDetailsResponse(occupiedRoomDtos));
	}
	
	@PutMapping("/{id}/check-out")
	public ResponseEntity<ApiResponse> CheckOutRoom(@PathVariable Long id) {
		occupiedRoomService.checkOutRoom(id);
		return new ResponseEntity<>(
				new ApiResponse(true,
						"Checked out the Room Successfully."),
				HttpStatus.OK);
	}
}
