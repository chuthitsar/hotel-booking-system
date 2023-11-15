package com.nexcode.hbs.model.dto;

import com.nexcode.hbs.model.entity.Reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestInfoDto {

	private Long id;
	
	private String name;
	
	private String email;
	
	private String phone;
	
	private String address;
	
	private Reservation reservation;
}
