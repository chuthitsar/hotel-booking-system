package com.nexcode.hbs.model.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "occupied_rooms")
public class OccupiedRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "guest_id")
	private GuestInfo guestInfo;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;
	
	@ManyToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;
	
	@Column(name = "check_in")
	private Instant checkIn;
	
	@Column(name = "check_out")
	private Instant checkOut;
	
	@Column(name = "is_completed", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isCompleted;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private OccupiedRoomStatus status;
}
