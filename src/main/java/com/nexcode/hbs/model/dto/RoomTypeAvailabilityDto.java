package com.nexcode.hbs.model.dto;

import com.nexcode.hbs.model.entity.RoomType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomTypeAvailabilityDto {

	private RoomType type;
	
	private Long available;
}
