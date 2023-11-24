package com.nexcode.hbs.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.nexcode.hbs.model.entity.status.RoomStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "number")
	private Integer number;
	
	@ManyToOne
	@JoinColumn(name = "room_type_id")
	private RoomType type;
	
	@Column(name = "floor")
	private Integer floor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private RoomStatus status;
	
	@Column(name = "is_maintained", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isMaintained;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private List<ReservedRoom> reservedRooms;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private List<OccupiedRoom> occupiedRooms;
	
	@Version
	private Long version;
}
