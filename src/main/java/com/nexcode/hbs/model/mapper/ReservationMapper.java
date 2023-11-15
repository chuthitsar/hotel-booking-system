package com.nexcode.hbs.model.mapper;

import java.util.List;

import com.nexcode.hbs.model.dto.ReservationDto;
import com.nexcode.hbs.model.entity.Reservation;
import com.nexcode.hbs.model.request.ReservationRequest;
import com.nexcode.hbs.model.response.CompletedReservationResponse;
import com.nexcode.hbs.model.response.ReservationDetailsResponse;
import com.nexcode.hbs.model.response.ReservationResponse;

public interface ReservationMapper {

	ReservationDto mapToDto(ReservationRequest reservationRequest);
	
	ReservationDto mapToDto(Reservation reservation);
	
	List<ReservationDto> mapToDto(List<Reservation> reservations);
	
	ReservationResponse mapToResponse(ReservationDto reservationDto);

	List<ReservationResponse> mapToResponse(List<ReservationDto> reservationDtos);

	ReservationDetailsResponse mapToDetailsResponse(ReservationDto reservationDto);

	CompletedReservationResponse mapToCompletedDetailsResponse(ReservationDto reservationDto);

}
