package com.nexcode.hbs.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservedRoomResponse {

	private Long id;
	
	private Integer roomNumber;
	
	private String roomType;
	
	private Long reservationId;
	
	private String checkIn;
	
	private String checkOut;
	
	private Integer pricePerNight;
	
	private String status;
	
}
