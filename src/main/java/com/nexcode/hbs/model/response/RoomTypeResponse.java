package com.nexcode.hbs.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeResponse {
	
	private Long id;

	private String name;
	
	private Integer maximumCapacity;
	
	private String size;
	
	private Integer pricePerNight;
	
	private String description;
	
	private Integer totalRoom;
		
	private String imageUrl;
	
	private List<AmenityResponse> amenities;
}
