package com.nexcode.hbs.service;

import java.util.List;

import com.nexcode.hbs.model.dto.ReservationDto;
import com.nexcode.hbs.model.response.DailyIncomeForMonthResponse;

public interface ReservationService {

	ReservationDto createReservation(ReservationDto reservationDto) throws Exception;

	List<ReservationDto> getPendingReservations();
	
	List<ReservationDto> getCurrentMonthCompletedReservations();
	
	List<ReservationDto> getCompletedReservationsForMonth(Integer month, Integer year);
	
	List<ReservationDto> getCurrentMonthReservations();
	
	ReservationDto getReservationById(Long id);

	void confirmReservationById(Long id);
	
	void cancelReservationById(Long id);

	void checkAndUpdateExpired();

	List<ReservationDto> getReservationsWithFilters(String status, String monthFilter, String reservationDate, String checkInDate,
			String checkOutDate);
	
	List<DailyIncomeForMonthResponse> getDailyIncomeForMonth(Integer month, Integer year);

	Long countNewReservations();

	ReservationDto getCompletedReservationById(Long id);

	void checkReservationByID(String reservationId);

}
