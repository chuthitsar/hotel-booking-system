package com.nexcode.hbs.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeDto {

	private Long id;
	
	private String name;
	
	private Integer maximumCapacity;
	
	private String size;
	
	private Integer pricePerNight;
	
	private String description;
	
	private Integer totalRoom;
	
	private String imageUrl;
	
	private List<AmenityDto> amenities;
	
	private List<Long> amenityIds;

}
