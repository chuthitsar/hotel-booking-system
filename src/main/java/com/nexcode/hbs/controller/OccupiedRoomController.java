package com.nexcode.hbs.controller;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexcode.hbs.model.dto.OccupiedRoomDto;
import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;
import com.nexcode.hbs.model.mapper.OccupiedRoomMapper;
import com.nexcode.hbs.model.response.OccupiedRoomResponse;
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
	public ResponseEntity<List<OccupiedRoomResponse>> getOccupiedRooms(
			@RequestParam(required = false) String type,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkInDate, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkOutDate) {
		
		List<OccupiedRoomDto> occupiedRoomDtos = (type != null || checkInDate != null || checkOutDate != null)
				? occupiedRoomService.getOccupiedRoomsWithFilters(null, OccupiedRoomStatus.CHECKED_IN, type, checkInDate, checkOutDate)
				: occupiedRoomService.getCurrentOccupiedRooms();
		return ResponseEntity.ok(occupiedRoomMapper.mapToResponse(occupiedRoomDtos));
	}
	
	@GetMapping("/history")
	public ResponseEntity<List<OccupiedRoomResponse>> getOccupiedRoomsHistory(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth checkedInMonth, 
			@RequestParam(required = false) String type,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkInDate, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkOutDate) {
		
		List<OccupiedRoomDto> occupiedRoomDtos = (checkedInMonth != null || type != null || checkInDate != null || checkOutDate != null)
				? occupiedRoomService.getOccupiedRoomsWithFilters(checkedInMonth, OccupiedRoomStatus.CHECKED_OUT, type, checkInDate, checkOutDate)
				: occupiedRoomService.getCurrentMonthHistory();
		return ResponseEntity.ok(occupiedRoomMapper.mapToResponse(occupiedRoomDtos));
	}
}
