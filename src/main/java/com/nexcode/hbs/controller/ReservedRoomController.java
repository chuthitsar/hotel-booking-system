package com.nexcode.hbs.controller;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexcode.hbs.model.dto.ReservedRoomDto;
import com.nexcode.hbs.model.dto.RoomDto;
import com.nexcode.hbs.model.mapper.ReservedRoomMapper;
import com.nexcode.hbs.model.mapper.RoomMapper;
import com.nexcode.hbs.model.request.ChangeReservedRoomRequest;
import com.nexcode.hbs.model.response.ApiResponse;
import com.nexcode.hbs.model.response.ReservedRoomResponse;
import com.nexcode.hbs.model.response.RoomResponse;
import com.nexcode.hbs.service.ReservedRoomService;
import com.nexcode.hbs.service.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reserved-rooms")
@RequiredArgsConstructor
public class ReservedRoomController {

	private final ReservedRoomService reservedRoomService;

	private final ReservedRoomMapper reservedRoomMapper;

	private final RoomService roomService;

	private final RoomMapper roomMapper;

	//// Updating the reserved room no. or status////
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> changeRoom(@PathVariable Long id,
			@RequestBody ChangeReservedRoomRequest changeRoomRequest) {

		reservedRoomService.updateReservedRoomNumber(id, changeRoomRequest.getRoomNumber());
		return new ResponseEntity<>(new ApiResponse(true, "Reserved Room has changed successfully."), HttpStatus.OK);
	}

	@PutMapping("/{id}/check-in")
	public ResponseEntity<ApiResponse> checkInReservedRoom(@PathVariable Long id) {
		
		reservedRoomService.checkInRoom(id);
		return new ResponseEntity<>(
				new ApiResponse(true,
						"Checked in the Reserved Room Successfully."),
				HttpStatus.OK);
	}
	
	@PutMapping("/{id}/cancel")
	public ResponseEntity<ApiResponse> cancelReservedRoom(@PathVariable Long id) {
		
		reservedRoomService.cancelReservedRoom(id);
		return new ResponseEntity<>(
				new ApiResponse(true,
						"Cancel the Reserved Room Successfully."),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<ReservedRoomResponse>> getAllReservedRooms(
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String checkInDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String checkOutDate) {

		List<ReservedRoomDto> reservedRoomDtos = (type != null || status != null || checkInDate != null
				|| checkOutDate != null)
						? reservedRoomService.getReservedRoomsWithFilters(status, type, checkInDate, checkOutDate)
						: reservedRoomService.getAllReservedRooms();
		return ResponseEntity.ok(reservedRoomMapper.mapToResponse(reservedRoomDtos));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReservedRoomResponse> getReservedRoomsById(@PathVariable Long id) {
		ReservedRoomDto reservedRoomDto = reservedRoomService.getReservedRoomById(id);
		return ResponseEntity.ok(reservedRoomMapper.mapToResponse(reservedRoomDto));
	}

	//// Getting all available rooms to edit the Reserved Room No. ////
	@GetMapping("/{id}/edit")
	public ResponseEntity<List<RoomResponse>> getAvailableRooms(@PathVariable("id") Long id) {
		List<RoomDto> roomDto = roomService.getAvailableRoomsByTypeAndDate(id);
		return ResponseEntity.ok(roomMapper.mapToResponse(roomDto));
	}

	//// Getting all pending, confirmed, check-in, check-out reserved rooms by month ////
	@GetMapping("/month")
	public ResponseEntity<List<ReservedRoomResponse>> getReservedRoomsForMonth(@RequestParam Integer month,
			@RequestParam Integer year) {
		List<ReservedRoomDto> reservedRoomDtos = reservedRoomService.getReservedRoomsForMonth(month, year);
		return ResponseEntity.ok(reservedRoomMapper.mapToResponse(reservedRoomDtos));
	}

}