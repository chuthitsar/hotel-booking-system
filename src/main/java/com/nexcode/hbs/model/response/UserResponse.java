package com.nexcode.hbs.model.response;

import java.util.Set;

import com.nexcode.hbs.model.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

	private Long id;
	private String username;
	private Set<Role> roles;
}
