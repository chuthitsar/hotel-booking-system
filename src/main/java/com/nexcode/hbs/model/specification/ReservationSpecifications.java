package com.nexcode.hbs.model.specification;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.data.jpa.domain.Specification;

import com.nexcode.hbs.model.entity.Reservation;
import com.nexcode.hbs.model.entity.status.ReservationStatus;
import com.nexcode.hbs.model.exception.InvalidStatusException;

public class ReservationSpecifications {

	public static Specification<Reservation> hasStatus(String status) {
		return (root, query, cb) -> {
			if (status == null) {
				return null;
			} else {
				try {
					return cb.equal(root.get("status"), ReservationStatus.valueOf(status));
				} catch (IllegalArgumentException e) {
					throw new InvalidStatusException("Invalid reservation status: " + status);
				}
			}
		};
	}

	public static Specification<Reservation> hasReservationDate(Instant reservationDate) {
		return (root, query, cb) -> {
			if (reservationDate == null) {
				return null;
			} else {
				Instant startOfDay = reservationDate.truncatedTo(ChronoUnit.DAYS);
				Instant startOfNextDay = startOfDay.plus(1, ChronoUnit.DAYS);
				return cb.between(root.get("createdAt"), startOfDay, startOfNextDay);
			}
		};
	}

	public static Specification<Reservation> hasCheckInDate(Instant checkInDate) {
		return (root, query, cb) -> {
			if (checkInDate == null) {
				return null;
			} else {
				Instant startOfDay = checkInDate.truncatedTo(ChronoUnit.DAYS);
				Instant startOfNextDay = startOfDay.plus(1, ChronoUnit.DAYS);
				return cb.between(root.get("checkIn"), startOfDay, startOfNextDay);
			}
		};
	}

	public static Specification<Reservation> hasCheckOutDate(Instant checkOutDate) {
		return (root, query, cb) -> {
			if (checkOutDate == null) {
				return null;
			} else {
				Instant startOfDay = checkOutDate.truncatedTo(ChronoUnit.DAYS);
				Instant startOfNextDay = startOfDay.plus(1, ChronoUnit.DAYS);
				return cb.between(root.get("checkOut"), startOfDay, startOfNextDay);
			}
		};
	}
}
