package com.nexcode.hbs.model.request;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class ReservationRequest {

	@NotBlank
	private String guestName;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String phone;
	
	@NotBlank
	private String address;
	
	@NotBlank
	private Integer numberOfGuest;
	
	@NotBlank
	private Integer totalRoom;
	
	@NotBlank
	private Instant checkIn;
	
	@NotBlank
	private Instant checkOut;
	
	@NotBlank
	private Integer lengthOfStay;
	
	@NotBlank
	private Integer totalCost;
	
	@NotBlank
	private List<String> selectedRooms;
	
	private String specialRequest;
}
