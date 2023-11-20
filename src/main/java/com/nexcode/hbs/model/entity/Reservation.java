package com.nexcode.hbs.model.entity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Future;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.nexcode.hbs.model.entity.status.ReservationStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "reservation_id")
	private String reservationID;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "guest_id", referencedColumnName = "id")
	private GuestInfo guestInfo;
	
	@Column(name = "number_of_guest")
	private Integer numberOfGuest;
	
	@Column(name = "total_room")
	private Integer totalRoom;
	
	@Column(name = "check_in", columnDefinition = "TIMESTAMP")
	private Instant checkIn;
	
	@Column(name = "check_out", columnDefinition = "TIMESTAMP")
	private Instant checkOut;
	
	@Column(name = "length_of_stay")
	private Integer lengthOfStay;
	
	@Column(name = "total_cost")
	private Integer totalCost;
	
	@Column(name = "is_paid", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isPaid;
	
	@Column(name = "special_request", columnDefinition = "TEXT")
	private String specialRequest;
	
	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private Instant createdAt;
	
	@Column(name = "expiration_time", columnDefinition = "TIMESTAMP")
	private Instant expiredAt; 
	
	@Column(name = "is_expired", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isExpired;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ReservationStatus status;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation")
	private List<ReservedRoom> reservedRooms;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation")
	private List<OccupiedRoom> occupiedRooms;
	
	@Version
	private Long version;
	
	@UpdateTimestamp
	@Column(name = "last_modified_at")
	private Instant lastModifiedAt;
	
	@PostPersist
    public void prePersist() {
        this.reservationID = String.format("%05d", id);
    }
	
	public int totalDays() {
        if(checkIn == null || checkOut == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}
