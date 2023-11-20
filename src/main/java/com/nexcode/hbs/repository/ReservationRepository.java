package com.nexcode.hbs.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexcode.hbs.model.entity.Reservation;
import com.nexcode.hbs.model.entity.status.ReservationStatus;
import com.nexcode.hbs.model.response.DailyIncomeForMonthResponse;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	Optional<List<Reservation>> findByStatus(ReservationStatus status);

	Optional<List<Reservation>> findByCreatedAtBetween(Instant startOfMonth, Instant endOfMonth);

	List<Reservation> findByStatusAndCreatedAtBetween(ReservationStatus status, Instant startOfMonth, Instant endOfMonth);

//	@Query("SELECT COUNT(r) FROM Reservation r "
//			+ "WHERE r.status IN ('PENDING', 'CONFIRMED')")
//	Long findNewCount();

	@Query("SELECT new com.nexcode.hbs.model.response.DailyIncomeForMonthResponse(DATE(r.createdAt) as date, SUM(r.totalCost) as income) FROM Reservation r "
			+ "WHERE MONTH(r.createdAt) = :month " + "and YEAR(r.createdAt) = :year "
			+ "and r.isPaid = true GROUP BY DATE(r.createdAt)")
	List<DailyIncomeForMonthResponse> findDailyIncomeByMonth(@Param("month") Integer month,
			@Param("year") Integer year);

	@Query("SELECT r FROM Reservation r "
			+ "WHERE MONTH(r.createdAt) = :month " 
			+ "and YEAR(r.createdAt) = :year "
			+ "and r.status = :status")
	Optional<List<Reservation>> findByStatusAndMonth(@Param("status") ReservationStatus status, @Param("month") Integer month,
			@Param("year") Integer year);

	@Query ("SELECT r FROM Reservation r "
			+ "WHERE (:status is null or r.status = :status) "
			+ "and ((:month is null and :year is null) or (MONTH(r.createdAt) = :month and YEAR(r.createdAt) = :year)) "
			+ "and (:reservationDate is null or DATE(r.createdAt) = DATE(:reservationDate)) "
			+ "and (:checkIn is null or DATE(r.checkIn) = DATE(:checkIn)) "
			+ "and (:checkOut is null or DATE(r.checkOut) = DATE(:checkOut))")
	List<Reservation> findWithFilters( 
			@Param("month") Integer month,
			@Param("year") Integer year,
			@Param("status") ReservationStatus status,
			@Param("reservationDate") LocalDate reservationDateFormat,
			@Param("checkIn") LocalDate checkIn, 
			@Param("checkOut") LocalDate checkOut);

	Optional<Reservation> findByReservationID(String reservationId);

	Long countByStatus(ReservationStatus status);
}
