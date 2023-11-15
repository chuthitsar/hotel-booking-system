package com.nexcode.hbs.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaintainRoomRequest {

	@NotBlank
	private Boolean isMaintained;
}
