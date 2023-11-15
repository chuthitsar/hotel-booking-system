package com.nexcode.hbs.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.nexcode.hbs.model.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	private Long id;
	
	private String username;
	
	private String password;
	
	private Set<Role> roles = new HashSet<>();
}
