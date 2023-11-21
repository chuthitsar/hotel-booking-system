package com.nexcode.hbs.model.response;

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
	
	private String checkIn;
	
	private String checkOut;
	
	private String createdAt;
	
	private Integer lengthOfStay;
	
	private Integer totalCost;
	
	private ReservationStatus status;
	
	private List<ReservedRoomResponse> reservedRooms;
	
	private List<String> selectedRooms;
	
	private String specialRequest;
}
