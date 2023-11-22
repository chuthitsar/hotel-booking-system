package com.nexcode.hbs.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexcode.hbs.model.dto.RoomTypeAvailabilityDto;
import com.nexcode.hbs.model.entity.RoomType;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

	boolean existsByName(String name);

	Optional<RoomType> findById(Long id);

	void deleteById(Long id);

	Optional<RoomType> findByName(String roomTypeName);
	
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

}
