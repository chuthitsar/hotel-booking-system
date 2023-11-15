package com.nexcode.hbs.model.response;

import com.nexcode.hbs.model.entity.status.RoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponse {

	private Long id;
	
	private Integer number;
	
	private String type;
	
	private Integer floor;
	
	private RoomStatus status;
	
	private Boolean isMaintained;
}
