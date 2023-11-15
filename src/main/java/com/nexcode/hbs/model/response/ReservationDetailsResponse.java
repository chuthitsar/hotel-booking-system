package com.nexcode.hbs.model.response;

import java.time.Instant;
import java.util.List;

import com.nexcode.hbs.model.entity.status.ReservationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDetailsResponse {

	private Long id;
	
	private String reservationId;
	
	private String guestName;
	
	private String guestEmail;
	
	private String guestPhone;
	
	private String guestAddress;
	
	private Integer numberOfGuest;
	
	private Integer totalRoom;
	
	private Instant checkIn;
	
	private Instant checkOut;
	
	private Instant createdAt;
	
	private Integer lengthOfStay;
	
	private Integer totalCost;
	
	private ReservationStatus status;
	
	private List<ReservedRoomResponse> reservedRooms;
	
	private String specialRequest;
}
