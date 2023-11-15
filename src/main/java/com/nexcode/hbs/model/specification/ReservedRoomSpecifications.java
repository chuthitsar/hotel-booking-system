package com.nexcode.hbs.model.specification;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.data.jpa.domain.Specification;

import com.nexcode.hbs.model.entity.ReservedRoom;
import com.nexcode.hbs.model.entity.status.ReservedRoomStatus;
import com.nexcode.hbs.model.exception.InvalidStatusException;

public class ReservedRoomSpecifications {

	public static Specification<ReservedRoom> hasStatus(String status) {
		return (root, query, cb) -> {
            if (status == null) {
                return null;
            } else {
                try {
                    return cb.equal(root.get("status"), ReservedRoomStatus.valueOf(status));
                } catch (IllegalArgumentException e) {
                    throw new InvalidStatusException("Invalid reservation status: " + status);
                }
            }
        };
	}
	
	public static Specification<ReservedRoom> hasType(String type) {
		return (root, query, cb) -> {
            if (type == null) {
                return null;
            } else {
                try {
                    return cb.equal(root.get("room").get("type").get("name"), type);
                } catch (IllegalArgumentException e) {
                    throw new InvalidStatusException("Invalid room type: " + type);
                }
            }
        };
	}
	
	public static Specification<ReservedRoom> hasCheckInDate(Instant checkInDate) {
		return (root, query, cb) -> {
			if(checkInDate == null) {
				return null;
			} else {
				Instant startOfDay = checkInDate.truncatedTo(ChronoUnit.DAYS);
				Instant startOfNextDay = startOfDay.plus(1, ChronoUnit.DAYS);
				return cb.between(root.get("checkIn"), startOfDay, startOfNextDay);
			}
		};
	}
	
	public static Specification<ReservedRoom> hasCheckOutDate(Instant checkOutDate) {
		return (root, query, cb) -> {
			if(checkOutDate == null) {
				return null;
			} else {
				Instant startOfDay = checkOutDate.truncatedTo(ChronoUnit.DAYS);
				Instant startOfNextDay = startOfDay.plus(1, ChronoUnit.DAYS);
				return cb.between(root.get("checkOut"), startOfDay, startOfNextDay);
			}
		};
	} 
}
