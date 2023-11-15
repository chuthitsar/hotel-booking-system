package com.nexcode.hbs.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmenityDto {

	@JsonIgnore
	private Long id;
	
	private String name;
	
	private String icon;
}
