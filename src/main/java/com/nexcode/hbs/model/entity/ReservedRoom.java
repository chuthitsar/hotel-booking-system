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
import javax.persistence.Version;

import com.nexcode.hbs.model.entity.status.ReservedRoomStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "reserved_rooms")
public class ReservedRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
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
	
	@Column(name = "price_per_night")
	private Integer pricePerNight;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ReservedRoomStatus status;

	
	@Version
	@Column(name="version", columnDefinition = "integer default 0")
	private Long version;
	
}
