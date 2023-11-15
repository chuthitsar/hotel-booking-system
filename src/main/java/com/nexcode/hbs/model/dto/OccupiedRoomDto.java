package com.nexcode.hbs.model.dto;

import java.time.Instant;

import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OccupiedRoomDto {

	private Long id;
	
	private GuestInfoDto guestInfo;
	
	private RoomDto roomDto;
	
	private ReservationDto reservationDto;
	
	private Long reservationId;
	
	private Instant checkIn;
	
	private Instant checkOut;
	
	private Boolean isCompleted;
	
	private OccupiedRoomStatus status;
}
