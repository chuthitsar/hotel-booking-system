package com.nexcode.hbs.model.dto;

import java.time.Instant;
import java.util.List;

import com.nexcode.hbs.model.entity.status.ReservationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDto {

	private Long id;
	
	private String reservationID;
	
	private GuestInfoDto guestInfo;
	
	private Integer numberOfGuest;
	
	private Integer totalRoom;
	
	private Instant checkIn;
	
	private Instant checkOut;
	
	private Integer lengthOfStay;
	
	private Integer totalCost;
	
	private Boolean isPaid;
	
	private String specialRequest;
	
	private Instant createdAt;
	
	private Instant lastModifiedAt;
	
	private Instant expiredAt; 
	
	private Boolean expired;
	
	private ReservationStatus status;
	
	private List<ReservedRoomDto> reservedRoomDtos;
	
	private List<OccupiedRoomDto> occupiedRoomDtos;
	
	private List<String> selectedRooms;
}
