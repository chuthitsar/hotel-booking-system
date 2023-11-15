package com.nexcode.hbs.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class RoomTypeAvailabilityRequest {

	@NotBlank
	private String checkInTime;
	
	@NotBlank
	private String checkOutTime;
}
