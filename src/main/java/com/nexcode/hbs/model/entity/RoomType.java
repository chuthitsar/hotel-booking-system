package com.nexcode.hbs.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "room_types")
public class RoomType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "maximum_capacity")
	private Integer maximumCapacity;
	
	@Column(name = "size")
	private String size;
	
	@Column(name = "price_per_night")
	private Integer pricePerNight;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "total_room")
	private Integer totalRoom;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@ManyToMany
	@JoinTable(
			name = "room_type_amenities",
			joinColumns = @JoinColumn(name = "room_type_id"),
			inverseJoinColumns = @JoinColumn(name = "amenity_id")
			)
	private List<Amenity> amenities = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
	private List<Room> rooms;
}
