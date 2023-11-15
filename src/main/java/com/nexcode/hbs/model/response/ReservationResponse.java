package com.nexcode.hbs.model.response;

import java.time.Instant;

import com.nexcode.hbs.model.entity.status.ReservationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponse {

	private Long id;
	
	private String reservationId;
	
	private String guestName;
	
	private String guestEmail;
	
	private Integer totalRoom;
	
	private Instant checkIn;
	
	private Instant checkOut;
	
	private Instant createdAt;
	
	private ReservationStatus status;
}
