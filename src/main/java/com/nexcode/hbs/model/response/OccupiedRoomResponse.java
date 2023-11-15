package com.nexcode.hbs.model.response;

import java.time.Instant;

import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OccupiedRoomResponse {

	private Long id;
	
	private Long reservationId;
	
	private String guestName;
	
	private String guestPhone;
	
	private String guestEmail;
	
	private Integer roomNumber;
	
	private String roomType;
	
	private Instant checkIn;
	
	private Instant checkOut;
	
	private OccupiedRoomStatus status;
	
}
