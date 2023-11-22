package com.nexcode.hbs.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeAvailabilityResponse {

	private RoomTypeResponse type;
	
	private Long available;
}
