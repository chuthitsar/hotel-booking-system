package com.nexcode.hbs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomTypeAvailabilityDto {

	private Long type;
	
	private Long available;
}
