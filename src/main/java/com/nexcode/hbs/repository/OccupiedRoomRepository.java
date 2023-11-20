package com.nexcode.hbs.repository;

import java.time.Instant;
import java.time.LocalDate;
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
	
	Optional<OccupiedRoom> findByIdAndStatus(Long id, OccupiedRoomStatus checkedIn);

	@Query ("SELECT r FROM OccupiedRoom r "
			+ "WHERE (:status is null or r.status = :status) "
			+ "and ((:month is null and :year is null) or (MONTH(r.checkIn) = :month and YEAR(r.checkIn) = :year)) "
			+ "and (:type is null or r.room.type.name = :type) "
			+ "and (:checkIn is null or DATE(r.checkIn) = DATE(:checkIn)) "
			+ "and (:checkOut is null or DATE(r.checkOut) = DATE(:checkOut))")
	List<OccupiedRoom> findWithFilters(Integer month, Integer year, OccupiedRoomStatus status, String type, LocalDate checkIn,
			LocalDate checkOut);

	Optional<List<OccupiedRoom>> findByCheckInBetweenAndIsCompletedIsTrue(Instant startOfMonth, Instant endOfMonth);

}
