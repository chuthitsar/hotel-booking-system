package com.nexcode.hbs.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequest {

	@NotBlank
	private Integer number;
	
	@NotBlank
	private String type;
	
	@NotBlank
	private Integer floor;
	
}