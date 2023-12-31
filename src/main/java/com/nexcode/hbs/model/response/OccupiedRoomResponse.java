package com.nexcode.hbs.model.response;

import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OccupiedRoomResponse {

	private Long id;
	
	private Integer roomNumber;
	
	private String roomType;
	
	private String checkIn;
	
	private String checkOut;
	
	private OccupiedRoomStatus status;
	
}
