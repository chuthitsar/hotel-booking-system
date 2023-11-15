package com.nexcode.hbs.model.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservedRoomResponse {

	private Long id;
	
	private Integer roomNumber;
	
	private String roomType;
	
	private Long reservationId;
	
	private Instant checkIn;
	
	private Instant checkOut;
	
	private Integer pricePerNight;
	
	private String status;
	
}
