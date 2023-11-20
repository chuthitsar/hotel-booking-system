package com.nexcode.hbs.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nexcode.hbs.model.entity.ReservedRoom;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.status.ReservedRoomStatus;

public interface ReservedRoomRepository extends JpaRepository<ReservedRoom, Long> {

//	@Query("SELECT rr.room.id FROM ReservedRoom rr "
//			+ "WHERE rr.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') "
//			+ "AND ((rr.checkIn <= :checkIn AND rr.checkOut >= :checkIn) "
//			+ "OR (rr.checkIn <= :checkOut AND rr.checkOut >= :checkOut) "
//			+ "OR (rr.checkIn >= :checkIn AND rr.checkOut <= :checkOut))")
//	List<Long> findReservedRoomIdsWithinDateRange(@Param("checkIn") Instant checkIn, 
//			@Param("checkOut") Instant checkOut);

	@Query("SELECT rr.room.id FROM ReservedRoom rr " + "WHERE rr.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') "
			+ "AND (rr.checkIn <= :checkOut AND rr.checkOut >= :checkIn) ")
	List<Long> findReservedRoomIdsWithinDateRange(Instant checkIn, Instant checkOut);

	List<ReservedRoom> findByReservationId(Long id);

	@Query("SELECT rr FROM ReservedRoom rr " + "WHERE (:type is null or rr.room.type.name = :type) "
			+ "and (:status is null or rr.status = :status) "
			+ "and (:checkIn is null or DATE(rr.checkIn) = DATE(:checkIn)) "
			+ "and (:checkOut is null or DATE(rr.checkOut) = DATE(:checkOut))")
	List<ReservedRoom> findWithFilters(String type, ReservedRoomStatus status, LocalDate checkIn, LocalDate checkOut);

	@Query("SELECT rr FROM ReservedRoom rr " + "WHERE MONTH(rr.reservation.createdAt) = :month "
			+ "and YEAR(rr.reservation.createdAt) = :year "
			+ "and rr.status in ('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT')")
	List<ReservedRoom> findByMonth(Integer month, Integer year);

	ReservedRoom findByRoomAndStatus(Room room, ReservedRoomStatus status);

}
