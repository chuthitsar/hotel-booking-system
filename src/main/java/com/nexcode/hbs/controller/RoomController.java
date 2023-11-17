package com.nexcode.hbs.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexcode.hbs.model.dto.RoomDto;
import com.nexcode.hbs.model.mapper.RoomMapper;
import com.nexcode.hbs.model.request.AvailableRoomRequest;
import com.nexcode.hbs.model.request.MaintainRoomRequest;
import com.nexcode.hbs.model.request.MultipleRoomRequest;
import com.nexcode.hbs.model.request.RoomRequest;
import com.nexcode.hbs.model.response.ApiResponse;
import com.nexcode.hbs.model.response.RoomResponse;
import com.nexcode.hbs.service.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

	private final RoomService roomService;
	
	private final RoomMapper roomMapper;
	
	@PostMapping
	public ResponseEntity<ApiResponse> addRoom(@RequestBody MultipleRoomRequest roomRequests) {
		roomService.createMultipleRooms(roomMapper.mapToDtoList(roomRequests));
		return new ResponseEntity<>(new ApiResponse(true, "New Rooms created successfully."), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
		roomService.updateRoom(id, roomMapper.mapToDto(roomRequest));
		return new ResponseEntity<>(new ApiResponse(true, "Room updated successfully."), HttpStatus.OK);
	}
	
	@PutMapping("/{id}/maintain")
	public ResponseEntity<ApiResponse> maintainRoom(@PathVariable Long id, @RequestBody MaintainRoomRequest roomRequest) {
		roomService.maintainRoom(id, roomRequest.getIsMaintained());
		return new ResponseEntity<>(new ApiResponse(true, "Room updated successfully."), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<RoomResponse>> getAllRoomsByFilter(
			@RequestParam(required = false) String type, 
			@RequestParam(required = false) Integer floor,
			@RequestParam(required = false) String status) { 
		
		List<RoomDto> roomDtos = (type != null || floor != null || status != null)
				? roomService.getRoomsWithFilters(type, floor, status)
				: roomService.getAllRooms();
		return ResponseEntity.ok(roomMapper.mapToResponse(roomDtos));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
		RoomDto room = roomService.getRoomById(id);
		return ResponseEntity.ok(roomMapper.mapToResponse(room));
	}
	
	//@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteRoomById(@PathVariable Long id) {
		roomService.deleteRoomById(id);
		return new ResponseEntity<>(new ApiResponse(true, "Room deleted successfully."), HttpStatus.OK);
	}
	
	@GetMapping("/available-rooms")
	public ResponseEntity<List<RoomResponse>> getAvailableRooms(@RequestBody AvailableRoomRequest request) {
		List<RoomDto> roomDto = roomService.getAvailableRoomsByDate(request.getReservationId());
		return ResponseEntity.ok(roomMapper.mapToResponse(roomDto));
	}
	
	@PostMapping("/{id}/check-in")
	public ResponseEntity<ApiResponse> CheckInRoom(@PathVariable Long id, @RequestBody AvailableRoomRequest request) {
		roomService.checkInRoomWithReservationId(id, request.getReservationId());
		return new ResponseEntity<>(
				new ApiResponse(true,
						"Checked in the Room Successfully."),
				HttpStatus.OK);
	}
	
	@PutMapping("/{id}/check-out")
	public ResponseEntity<ApiResponse> CheckOutRoom(@PathVariable Long id) {
		roomService.checkOutRoom(id);
		return new ResponseEntity<>(
				new ApiResponse(true,
						"Checked out the Room Successfully."),
				HttpStatus.OK);
	}
	
	@GetMapping("/available-count")
	public ResponseEntity<Long> countAvailableRooms() {
		Long roomCount = roomService.countAvailableRooms();
		return ResponseEntity.ok(roomCount);
	}
	
	@GetMapping("/occupation-count")
	public ResponseEntity<Long> countOccupiedRooms() {
		Long roomCount = roomService.countOccupiedRooms();
		return ResponseEntity.ok(roomCount);
	}
}
