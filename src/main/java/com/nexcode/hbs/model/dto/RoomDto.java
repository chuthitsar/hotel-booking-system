package com.nexcode.hbs.model.dto;

import com.nexcode.hbs.model.entity.status.RoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {

	private Long id;
	
	private Integer number;
	
	private RoomTypeDto type;
	
	private Integer floor;
	
	private RoomStatus status;
	
	private Boolean isMaintained;
	
	private String roomTypeName;
	
	private Long roomTypeId;
	
	private String statusName;
	
}
