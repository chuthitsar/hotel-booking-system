package com.nexcode.hbs.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {

	@NotBlank
	private String oldPassword;
	
	@NotBlank
	private String newPassword;
}
