package com.nexcode.hbs.repository;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nexcode.hbs.model.entity.OccupiedRoom;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.status.OccupiedRoomStatus;

public interface OccupiedRoomRepository extends JpaRepository<OccupiedRoom, Long> {

	Optional<List<OccupiedRoom>> findByStatus(OccupiedRoomStatus status);

	Optional<OccupiedRoom> findByRoomAndStatus(Room room, OccupiedRoomStatus status);

	@Query ("SELECT r FROM OccupiedRoom r "
			+ "WHERE (:status is null or r.status = :status) "
			+ "and (:checkInAt is null or MONTH(r.checkIn) = MONTH(:checkInAt)) "
			+ "and (:checkInAt is null or YEAR(r.checkIn) = YEAR(:checkInAt)) "
			+ "and (:type is null or r.room.type.name = :type) "
			+ "and (:checkInDate is null or DATE(r.checkIn) = DATE(:checkInDate)) "
			+ "and (:checkOutDate is null or DATE(r.checkOut) = DATE(:checkOutDate))")
	List<OccupiedRoom> findWithFilters(Date checkInAt, OccupiedRoomStatus status, String type, Instant checkInDate,
			Instant checkOutDate);

	Optional<List<OccupiedRoom>> findByCheckInBetweenAndIsCompletedIsTrue(Instant startOfMonth, Instant endOfMonth);

}
