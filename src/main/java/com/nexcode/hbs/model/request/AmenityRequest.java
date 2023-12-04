package com.nexcode.hbs.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class AmenityRequest {

	@NotBlank
	private String name;
	
	@NotBlank
	private String icon;
}
