package com.nexcode.hbs.model.response;

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
	
	private String checkIn;
	
	private String checkOut;
	
	private String createdAt;
	
	private ReservationStatus status;
}
