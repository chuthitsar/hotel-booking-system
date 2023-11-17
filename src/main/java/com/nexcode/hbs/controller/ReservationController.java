package com.nexcode.hbs.controller;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.nexcode.hbs.model.dto.ReservationDto;
import com.nexcode.hbs.model.dto.ReservedRoomDto;
import com.nexcode.hbs.model.exception.InvalidStatusException;
import com.nexcode.hbs.model.mapper.ReservationMapper;
import com.nexcode.hbs.model.mapper.ReservedRoomMapper;
import com.nexcode.hbs.model.request.ChangeStatusRequest;
import com.nexcode.hbs.model.request.ReservationRequest;
import com.nexcode.hbs.model.response.ApiResponse;
import com.nexcode.hbs.model.response.CompletedReservationResponse;
import com.nexcode.hbs.model.response.DailyIncomeForMonthResponse;
import com.nexcode.hbs.model.response.ReservationDetailsResponse;
import com.nexcode.hbs.model.response.ReservationResponse;
import com.nexcode.hbs.model.response.ReservedRoomResponse;
import com.nexcode.hbs.service.ReservationService;
import com.nexcode.hbs.service.ReservedRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;
	
	private final ReservedRoomService reservedRoomService;
	
	private final ReservationMapper reservationMapper;
	
	private final ReservedRoomMapper reservedRoomMapper;
	
	@PostMapping
	public ResponseEntity<ApiResponse> createReservation(@RequestBody ReservationRequest reservationRequest) throws Exception {
		reservationService.createReservation(reservationMapper.mapToDto(reservationRequest));
		return new ResponseEntity<>(new ApiResponse(true, "Reservation Successful!"), HttpStatus.CREATED);
	}
	
	//// Getting Reservations, default will be all pending reservations ////
	@GetMapping
	public ResponseEntity<List<ReservationResponse>> getReservations(
			@RequestParam(required = false) String status,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth monthFilter,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant reservationDate, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkInDate, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkOutDate){
		
		List<ReservationDto> reservationDtos = (status != null || monthFilter != null || reservationDate != null || checkInDate != null || checkOutDate != null) 
		        ? reservationService.getReservationsWithFilters(status, monthFilter, reservationDate, checkInDate, checkOutDate)
		        : reservationService.getPendingReservations();
		return ResponseEntity.ok(reservationMapper.mapToResponse(reservationDtos));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReservationDetailsResponse> getReservationById(@PathVariable Long id){
		ReservationDto reservationDto = reservationService.getReservationById(id);
		return ResponseEntity.ok(reservationMapper.mapToDetailsResponse(reservationDto));
	}
	
	@GetMapping("/{id}/reserved-rooms")
	public ResponseEntity<List<ReservedRoomResponse>> getReservedRoomsByReservationId(@PathVariable Long id) {
		List<ReservedRoomDto> reservedRoomDtos = reservedRoomService.getReservedRoomsByReservationId(id);
		return ResponseEntity.ok(reservedRoomMapper.mapToResponse(reservedRoomDtos));
	}
	
	//// Check and Update Expired Reservations ////
	@PutMapping("/refresh")
	public ResponseEntity<ApiResponse> checkAndUpdateExpired() {
		reservationService.checkAndUpdateExpired();
		return new ResponseEntity<>(new ApiResponse(true, "Checked And Updated Expired"), HttpStatus.OK);
	}
	
	//// Confirm or Cancel reservation ////
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> changeReservationStatusById(@PathVariable("id") Long id, @RequestBody ChangeStatusRequest changeStatusRequest) {
		if (changeStatusRequest.getStatus().equals("CONFIRMED")) {
			reservationService.confirmReservationById(id);
			return new ResponseEntity<>(new ApiResponse(true, "Reservation Confirmed!"), HttpStatus.OK);
		}
		else if (changeStatusRequest.getStatus().equals("CANCELED")) {
			reservationService.cancelReservationById(id);
			return new ResponseEntity<>(new ApiResponse(true, "Reservation Canceled!"), HttpStatus.OK);
		}
		else {
			throw new InvalidStatusException("Invalid Status!");
		}
	}
	
	//// Getting all reservations, each of which status is PENDING or CONFIRMED ////
	@GetMapping("/new-reservations")
	public ResponseEntity<List<ReservationResponse>> getNewReservations() {
		List<ReservationDto> reservationDtos = reservationService.getNewReservations();
		return ResponseEntity.ok(reservationMapper.mapToResponse(reservationDtos));
	}
	
	//// Getting count of all pending and confirmed reservations ////
	@GetMapping("/count")
	public ResponseEntity<Long> countNewReservations() {
		Long reservationCount = reservationService.countNewReservations();
		return ResponseEntity.ok(reservationCount);
	}
	
	//// Getting daily income for a particular month ////
	@GetMapping("/daily-income")
	public ResponseEntity<List<DailyIncomeForMonthResponse>> getDailyIncomeForMonth(@RequestParam Integer month, @RequestParam Integer year) {
		List<DailyIncomeForMonthResponse> dailyIncomeForMonthResponses = reservationService.getDailyIncomeForMonth(month, year);
		return ResponseEntity.ok(dailyIncomeForMonthResponses);
	}
	
	//// Getting Completed Reservations, default will be for current month ////
	@GetMapping("/completed")
	public ResponseEntity<List<ReservationResponse>> getCompletedReservations(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth monthFilter, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant reservationDate, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkInDate, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkOutDate){
		
		List<ReservationDto> reservationDtos = (monthFilter != null || reservationDate != null || checkInDate != null || checkOutDate != null) 
		        ? reservationService.getReservationsWithFilters("COMPLETED", monthFilter, reservationDate, checkInDate, checkOutDate)
		        : reservationService.getCurrentMonthCompletedReservations();
		return ResponseEntity.ok(reservationMapper.mapToResponse(reservationDtos));
	}
	
	@GetMapping("/completed/{id}")
	public ResponseEntity<CompletedReservationResponse> getCompletedReservationById(@PathVariable Long id){
		ReservationDto reservationDto = reservationService.getCompletedReservationById(id);
		return ResponseEntity.ok(reservationMapper.mapToCompletedDetailsResponse(reservationDto));
	}
	
}
