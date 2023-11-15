package com.nexcode.hbs.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {

	@NotBlank
	private String username;
}
