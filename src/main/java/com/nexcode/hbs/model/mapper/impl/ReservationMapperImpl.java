package com.nexcode.hbs.model.mapper.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.GuestInfoDto;
import com.nexcode.hbs.model.dto.ReservationDto;
import com.nexcode.hbs.model.entity.Reservation;
import com.nexcode.hbs.model.mapper.GuestInfoMapper;
import com.nexcode.hbs.model.mapper.OccupiedRoomMapper;
import com.nexcode.hbs.model.mapper.ReservationMapper;
import com.nexcode.hbs.model.mapper.ReservedRoomMapper;
import com.nexcode.hbs.model.request.ReservationRequest;
import com.nexcode.hbs.model.response.CompletedReservationResponse;
import com.nexcode.hbs.model.response.ReservationDetailsResponse;
import com.nexcode.hbs.model.response.ReservationResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationMapperImpl implements ReservationMapper {

	private final GuestInfoMapper guestInfoMapper;
	
	private final ReservedRoomMapper reservedRoomMapper;
	
	private final OccupiedRoomMapper occupiedRoomMapper;
	
	@Override
	public ReservationDto mapToDto(ReservationRequest reservationRequest) {

		if(reservationRequest == null) {
			return null;
		}
		
		GuestInfoDto guestInfoDto = new GuestInfoDto();
		guestInfoDto.setName(reservationRequest.getGuestName());
		guestInfoDto.setEmail(reservationRequest.getEmail());
		guestInfoDto.setPhone(reservationRequest.getPhone());
		guestInfoDto.setAddress(reservationRequest.getAddress());
		
		ReservationDto reservationDto = new ReservationDto();
		reservationDto.setCheckIn(convertToDateTime(reservationRequest.getCheckIn(), 7, 30));
		reservationDto.setCheckOut(convertToDateTime(reservationRequest.getCheckOut(), 5, 30));
		reservationDto.setLengthOfStay(reservationRequest.getLengthOfStay());
		reservationDto.setTotalCost(reservationRequest.getTotalCost());
		reservationDto.setNumberOfGuest(reservationRequest.getNumberOfGuest());
		reservationDto.setTotalRoom(reservationRequest.getTotalRoom());
		reservationDto.setSelectedRooms(reservationRequest.getSelectedRooms());
		reservationDto.setSpecialRequest(reservationRequest.getSpecialRequest());
		reservationDto.setGuestInfo(guestInfoDto);
		
		return reservationDto;
	}
	
	@Override
	public ReservationDto mapToDto(Reservation reservation) {
		
		if (reservation == null) {
			return null;
		}
		
		ReservationDto reservationDto = new ReservationDto();
		reservationDto.setId(reservation.getId());
		reservationDto.setReservationID(reservation.getReservationID());
		reservationDto.setGuestInfo(guestInfoMapper.mapToDto(reservation.getGuestInfo()));
		reservationDto.setNumberOfGuest(reservation.getNumberOfGuest());
		reservationDto.setTotalRoom(reservation.getTotalRoom());
		reservationDto.setCheckIn(reservation.getCheckIn());
		reservationDto.setCheckOut(reservation.getCheckOut());
		reservationDto.setLengthOfStay(reservation.getLengthOfStay());
		reservationDto.setTotalCost(reservation.getTotalCost());
		reservationDto.setIsPaid(reservation.getIsPaid());
		reservationDto.setSpecialRequest(reservation.getSpecialRequest());
		reservationDto.setCreatedAt(reservation.getCreatedAt());
		reservationDto.setExpiredAt(reservationDto.getExpiredAt());
		reservationDto.setStatus(reservation.getStatus());
		reservationDto.setReservedRoomDtos(reservedRoomMapper.mapToDto(reservation.getReservedRooms()));
		reservationDto.setOccupiedRoomDtos(occupiedRoomMapper.mapToDto(reservation.getOccupiedRooms()));
		
		return reservationDto;
	}

	@Override
	public List<ReservationDto> mapToDto(List<Reservation> reservations) {
		
		if (reservations == null) {
			return null;
		}
		
		List<ReservationDto> reservationDtos = new ArrayList<>();
		for (Reservation reservation: reservations) {
			reservationDtos.add(mapToDto(reservation));
		}
		return reservationDtos;
	}

	@Override
	public ReservationResponse mapToResponse(ReservationDto reservationDto) {
		
		if(reservationDto == null) {
			return null;
		}
		
		ReservationResponse reservationResponse = new ReservationResponse();
		reservationResponse.setId(reservationDto.getId());
		reservationResponse.setReservationId(reservationDto.getReservationID());
		reservationResponse.setGuestName(reservationDto.getGuestInfo().getName());
		reservationResponse.setGuestEmail(reservationDto.getGuestInfo().getEmail());
		reservationResponse.setTotalRoom(reservationDto.getTotalRoom());
		reservationResponse.setCheckIn(reservationDto.getCheckIn().toString());
		reservationResponse.setCheckOut(reservationDto.getCheckOut().toString());
		reservationResponse.setCreatedAt(reservationDto.getCreatedAt().toString());
		reservationResponse.setStatus(reservationDto.getStatus());
		
		return reservationResponse;
	}

	@Override
	public List<ReservationResponse> mapToResponse(List<ReservationDto> reservationDtos) {

		if (reservationDtos == null) {
			return null;
		}
		
		List<ReservationResponse> reservationResponses = new ArrayList<>();
		for (ReservationDto reservationDto: reservationDtos) {
			reservationResponses.add(mapToResponse(reservationDto));
		}
		return reservationResponses;
	}

	@Override
	public ReservationDetailsResponse mapToDetailsResponse(ReservationDto reservationDto) {
		
		if(reservationDto == null) {
			return null;
		}
		
		ReservationDetailsResponse reservationResponse = new ReservationDetailsResponse();
		reservationResponse.setId(reservationDto.getId());
		reservationResponse.setReservationId(reservationDto.getReservationID());
		reservationResponse.setGuestName(reservationDto.getGuestInfo().getName());
		reservationResponse.setGuestEmail(reservationDto.getGuestInfo().getEmail());
		reservationResponse.setGuestPhone(reservationDto.getGuestInfo().getPhone());
		reservationResponse.setGuestAddress(reservationDto.getGuestInfo().getAddress());
		reservationResponse.setNumberOfGuest(reservationDto.getNumberOfGuest());
		reservationResponse.setTotalRoom(reservationDto.getTotalRoom());
		reservationResponse.setCheckIn(reservationDto.getCheckIn().toString());
		reservationResponse.setCheckOut(reservationDto.getCheckOut().toString());
		reservationResponse.setCreatedAt(reservationDto.getCreatedAt().toString());
		reservationResponse.setLengthOfStay(reservationDto.getLengthOfStay());
		reservationResponse.setTotalCost(reservationDto.getTotalCost());
		reservationResponse.setStatus(reservationDto.getStatus());
		reservationResponse.setReservedRooms(reservedRoomMapper.mapToResponse(reservationDto.getReservedRoomDtos()));
		reservationResponse.setSelectedRooms(reservationDto.getSelectedRooms());
		reservationResponse.setSpecialRequest(reservationDto.getSpecialRequest());
		
		return reservationResponse;
	}

	@Override
	public CompletedReservationResponse mapToCompletedDetailsResponse(ReservationDto reservationDto) {
		
		if(reservationDto == null) {
			return null;
		}
		
		CompletedReservationResponse reservationResponse = new CompletedReservationResponse();
		reservationResponse.setId(reservationDto.getId());
		reservationResponse.setReservationId(reservationDto.getReservationID());
		reservationResponse.setGuestName(reservationDto.getGuestInfo().getName());
		reservationResponse.setGuestEmail(reservationDto.getGuestInfo().getEmail());
		reservationResponse.setGuestPhone(reservationDto.getGuestInfo().getPhone());
		reservationResponse.setGuestAddress(reservationDto.getGuestInfo().getAddress());
		reservationResponse.setNumberOfGuest(reservationDto.getNumberOfGuest());
		reservationResponse.setTotalRoom(reservationDto.getTotalRoom());
		reservationResponse.setCheckIn(reservationDto.getCheckIn().toString());
		reservationResponse.setCheckOut(reservationDto.getCheckOut().toString());
		reservationResponse.setCreatedAt(reservationDto.getCreatedAt().toString());
		reservationResponse.setLengthOfStay(reservationDto.getLengthOfStay());
		reservationResponse.setTotalCost(reservationDto.getTotalCost());
		reservationResponse.setStatus(reservationDto.getStatus());
		reservationResponse.setOccupiedRooms(occupiedRoomMapper.mapToResponse(reservationDto.getOccupiedRoomDtos()));
		reservationResponse.setSpecialRequest(reservationDto.getSpecialRequest());
		
		return reservationResponse;
	}
	
	public Instant convertToDateTime(String date, int hour, int minute) {
		
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime dateTime = localDate.atTime(hour, minute);
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneOffset.UTC);

        Instant instant = zonedDateTime.toInstant();

        return instant;
    }

}
