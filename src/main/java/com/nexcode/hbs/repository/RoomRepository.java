package com.nexcode.hbs.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.nexcode.hbs.model.dto.RoomTypeAvailabilityDto;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.RoomType;
import com.nexcode.hbs.model.entity.status.RoomStatus;

public interface RoomRepository extends JpaRepository<Room, Long> {

	boolean existsByNumber(Integer number);

	int countByType_Name(String name);
	
	int countByType(RoomType type);

//	@Query("SELECT new com.nexcode.hbs.model.dto.RoomTypeAvailabilityDto(rt.id, COUNT(r)) "
//			+ "FROM Room r "
//			+ "JOIN r.type rt "
//			+ "LEFT JOIN r.reservedRooms rr "
//			+ "ON (rr.status IN ('PENDING', 'CONFIRMED')) "
//			+ "AND (:checkIn <= rr.checkOut AND :checkOut >= rr.checkIn) "
//			+ "LEFT JOIN r.occupiedRooms o "
//            + "ON (o.isCompleted = false AND :checkIn <= o.checkOut AND :checkOut >= o.checkIn) "
//			+ "WHERE rr.id IS NULL "
//            + "AND o.id IS NULL "
//            + "AND r.status NOT IN ('MAINTAINED', 'UNAVAILABLE') "
//			+ "GROUP BY rt.id")
//	List<RoomTypeAvailabilityDto> getAvailableRoomTypes(@Param("checkIn") Instant checkIn, 
//			@Param("checkOut") Instant checkOut);
	
	@Query("SELECT new com.nexcode.hbs.model.dto.RoomTypeAvailabilityDto(rt, COALESCE(COUNT(r.id), 0)) " +
	        "FROM RoomType rt " +
	        "LEFT JOIN rt.rooms r ON (r.status NOT IN ('MAINTAINED', 'UNAVAILABLE'))" +
	        "LEFT JOIN r.reservedRooms rr " +
	        "ON (rr.status IN ('PENDING', 'CONFIRMED')) " +
	        "AND (:checkIn <= rr.checkOut AND :checkOut >= rr.checkIn) " +
	        "LEFT JOIN r.occupiedRooms o " +
	        "ON (o.isCompleted = false AND :checkIn <= o.checkOut AND :checkOut >= o.checkIn) " +
	        "WHERE (rr.id IS NULL OR o.id IS NULL OR (rr.id IS NULL AND o.id IS NULL)) " +
	        "OR r.id IS NULL " + 
	        "GROUP BY rt.id")
	List<RoomTypeAvailabilityDto> getAvailableRoomTypes(
	        @Param("checkIn") Instant checkIn,
	        @Param("checkOut") Instant checkOut);

	@Query("SELECT r FROM Room r "
			+ "WHERE r.type.name = :roomType "
			+ "AND r.status NOT IN ('UNAVAILABLE', 'MAINTAINED') "
			+ "AND r NOT IN "
		    + "(SELECT rr.room FROM ReservedRoom rr "
		    	+ "WHERE (rr.status IN ('PENDING', 'CONFIRMED')) "
		    	+ "AND (:checkIn <= rr.checkOut AND :checkOut >= rr.checkIn)) "
		    + "AND r NOT IN "
	        + "(SELECT o.room FROM OccupiedRoom o "
	            + "WHERE o.isCompleted = false "
	            + "AND (:checkIn <= o.checkOut AND :checkOut >= o.checkIn))")
	List<Room> findAvailableRoomsByTypeAndDate(@Param("roomType") String roomType, 
			@Param("checkIn") Instant checkIn, @Param("checkOut") Instant checkOut);
	
	@Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
	@QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
	@Query("SELECT r FROM Room r "
				+ "WHERE r.type.name = :roomType "
				+ "AND r.status NOT IN ('UNAVAILABLE', 'MAINTAINED') "
				+ "AND r NOT IN "
			    + "(SELECT rr.room FROM ReservedRoom rr "
			    	+ "WHERE (rr.status IN ('PENDING', 'CONFIRMED')) "
			    	+ "AND (:checkIn <= rr.checkOut AND :checkOut >= rr.checkIn)) "
			    + "AND r NOT IN "
		        + "(SELECT o.room FROM OccupiedRoom o "
		            + "WHERE o.isCompleted = false "
		            + "AND (:checkIn <= o.checkOut AND :checkOut >= o.checkIn))")
	List<Room> findFirstAvailableRoomsByTypeAndDateWithLock(@Param("roomType") String roomType, 
				@Param("checkIn") Instant checkIn, @Param("checkOut") Instant checkOut, Pageable pageable);

	@Query("SELECT r FROM Room r "
			+ "WHERE r.type.name = :roomType "
			+ "AND r.id NOT IN :reservedRoomIds "
			+ "AND r.status NOT IN ('UNAVAILABLE', 'MAINTAINED') "
			+ "ORDER BY r.id ASC")
	List<Room> findByTypeAndIdNotIn(@Param("roomType") String roomType, 
			@Param("reservedRoomIds") List<Long> reservedRoomIds);

	Optional<Room> findByNumber(Integer roomNumber);

	@Query("SELECT r FROM Room r "
			+ "WHERE (:type is null or r.type.name = :type) "
			+ "and (:floor is null or r.floor = :floor) "
			+ "and (:status is null or r.status = :status)")
	List<Room> findWithFilters(@Param("type") String type, 
			@Param("floor") Integer floor, @Param("status") RoomStatus status);

	@Query("SELECT r FROM Room r "
			+ "WHERE r.status NOT IN ('UNAVAILABLE', 'MAINTAINED') "
			+ "AND r NOT IN "
		    + "(SELECT rr.room FROM ReservedRoom rr "
		    	+ "WHERE (rr.status IN ('PENDING', 'CONFIRMED')) "
		    	+ "AND (:checkIn <= rr.checkOut AND :checkOut >= rr.checkIn)) "
		    + "AND r NOT IN "
	        + "(SELECT o.room FROM OccupiedRoom o "
	            + "WHERE o.isCompleted = false "
	            + "AND (:checkIn <= o.checkOut AND :checkOut >= o.checkIn))")
	List<Room> findAvailableRoomsByDate(Instant checkIn, Instant checkOut);

	Long countByStatus(RoomStatus available);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
	@Query("SELECT r FROM Room r "
			+ "WHERE r.type.name = :roomType "
			+ "AND r.status NOT IN ('UNAVAILABLE', 'MAINTAINED') "
			+ "AND r NOT IN "
		    + "(SELECT rr.room FROM ReservedRoom rr "
		    	+ "WHERE (rr.status IN ('PENDING', 'CONFIRMED')) "
		    	+ "AND (:checkIn <= rr.checkOut AND :checkOut >= rr.checkIn)) "
		    + "AND r NOT IN "
	        + "(SELECT o.room FROM OccupiedRoom o "
	            + "WHERE o.isCompleted = false "
	            + "AND (:checkIn <= o.checkOut AND :checkOut >= o.checkIn))")
	Room findAvailableRoomForReservationWithPessimisticWriteLock(String roomType, Instant checkIn, Instant checkOut);
}