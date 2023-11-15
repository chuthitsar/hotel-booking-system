package com.nexcode.hbs.model.specification;


import org.springframework.data.jpa.domain.Specification;

import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.status.RoomStatus;
import com.nexcode.hbs.model.exception.InvalidStatusException;

public class RoomSpecifications {

	public static Specification<Room> hasType(String type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type").get("name"), type);
    }

    public static Specification<Room> hasFloor(Integer floor) {
        return (root, query, cb) -> floor == null ? null : cb.equal(root.get("floor"), floor);
    }

    public static Specification<Room> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            } else {
                try {
                    return cb.equal(root.get("status"), RoomStatus.valueOf(status));
                } catch (IllegalArgumentException e) {
                    throw new InvalidStatusException("Invalid room status: " + status);
                }
            }
        };
    }
    
}
