package com.nexcode.hbs.model.dto;

import java.time.Instant;

import com.nexcode.hbs.model.entity.status.ReservedRoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservedRoomDto {

	private Long id;
	
	private RoomDto roomDto;
	
	private ReservationDto reservation;
	
	private Instant checkIn;
	
	private Instant checkOut;
	
	private Integer pricePerNight;
	
	private ReservedRoomStatus status;
	
	private Long reservationId;
	
	private GuestInfoDto guestInfo;
}
