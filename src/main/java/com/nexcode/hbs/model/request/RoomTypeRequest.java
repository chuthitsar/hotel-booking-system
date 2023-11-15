package com.nexcode.hbs.model.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeRequest {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private Integer maximumCapacity;
	
	@NotBlank
	private String size;
	
	@NotBlank
	private Integer pricePerNight;
	
	@NotBlank
	private String description;
	
	@NotBlank
	private String imageUrl;
	
	@NotBlank
	private List<Long> amenityIds;
}
