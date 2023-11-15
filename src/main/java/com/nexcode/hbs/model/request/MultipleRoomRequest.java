package com.nexcode.hbs.model.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleRoomRequest {

	@NotBlank
	private List<RoomRequest> roomRequests;
}
