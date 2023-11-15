package com.nexcode.hbs.service.impl;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.nexcode.hbs.model.dto.GuestInfoDto;
import com.nexcode.hbs.model.dto.ReservationDto;
import com.nexcode.hbs.model.entity.GuestInfo;
import com.nexcode.hbs.model.entity.Reservation;
import com.nexcode.hbs.model.entity.ReservedRoom;
import com.nexcode.hbs.model.entity.Room;
import com.nexcode.hbs.model.entity.status.ReservationStatus;
import com.nexcode.hbs.model.entity.status.ReservedRoomStatus;
import com.nexcode.hbs.model.exception.AppException;
import com.nexcode.hbs.model.exception.BadRequestException;
import com.nexcode.hbs.model.exception.InvalidStatusException;
import com.nexcode.hbs.model.exception.NoContentException;
import com.nexcode.hbs.model.exception.RecordNotFoundException;
import com.nexcode.hbs.model.mapper.ReservationMapper;
import com.nexcode.hbs.model.response.DailyIncomeForMonthResponse;
import com.nexcode.hbs.repository.GuestInfoRepository;
import com.nexcode.hbs.repository.ReservationRepository;
import com.nexcode.hbs.repository.ReservedRoomRepository;
import com.nexcode.hbs.repository.RoomRepository;
import com.nexcode.hbs.service.MailService;
import com.nexcode.hbs.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	
	private final RoomRepository roomRepository;
	
	private final ReservedRoomRepository reservedRoomRepository;
	
	private final GuestInfoRepository guestInfoRepository;
	
	private final MailService mailService;
	
	private final ReservationMapper reservationMapper;
	
	@Override
	@Transactional
	public void createReservation(ReservationDto reservationDto) throws Exception {
		
		GuestInfoDto guestInfoDto = reservationDto.getGuestInfo();
		GuestInfo guestInfo = new GuestInfo();
	    guestInfo.setName(guestInfoDto.getName());
	    guestInfo.setEmail(guestInfoDto.getEmail());
	    guestInfo.setPhone(guestInfoDto.getPhone());
	    guestInfo.setAddress(guestInfoDto.getAddress());

	    guestInfo = guestInfoRepository.save(guestInfo);
	    
	    Reservation reservation = new Reservation();
	    reservation.setGuestInfo(guestInfo);
	    reservation.setNumberOfGuest(reservationDto.getNumberOfGuest());
	    reservation.setTotalRoom(reservationDto.getTotalRoom());
	    reservation.setCheckIn(reservationDto.getCheckIn());
	    reservation.setCheckOut(reservationDto.getCheckOut());
	    reservation.setLengthOfStay(reservationDto.getLengthOfStay());
	    reservation.setTotalCost(reservationDto.getTotalCost());
	    reservation.setIsPaid(false);
	    reservation.setSpecialRequest(reservationDto.getSpecialRequest());
	    reservation.setExpiredAt(Instant.now().plus(24, ChronoUnit.HOURS));
	    reservation.setIsExpired(false);
	    reservation.setStatus(ReservationStatus.PENDING);
	    
	    reservation = reservationRepository.save(reservation);
	    
	    List<String> selectedRooms = reservationDto.getSelectedRooms();
	    
	    for (String roomType: selectedRooms) {
	    	
	    	List<Room> rooms = roomRepository.findAvailableRoomsByTypeAndDate(roomType, reservation.getCheckIn(), reservation.getCheckOut());
	    	System.out.println(rooms.size());
	    	System.out.println(roomType);
	    	if (rooms.isEmpty()) {
	    		throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
	    	}
	    	
	    	Room room = rooms.get(0);
	    	ReservedRoom reservedRoom = new ReservedRoom();
	    	reservedRoom.setRoom(room);
	    	reservedRoom.setReservation(reservation);
	    	reservedRoom.setCheckIn(reservation.getCheckIn());
	    	reservedRoom.setCheckOut(reservation.getCheckOut());
	    	reservedRoom.setPricePerNight(room.getType().getPricePerNight());
	    	reservedRoom.setStatus(ReservedRoomStatus.PENDING);
	    	reservedRoomRepository.save(reservedRoom);
	    }
        
	    try {
            sendMail(guestInfo, reservation, "Reservation Details", "reservationConfirmation");
        } catch (OptimisticLockingFailureException e) {
            throw new Exception("The reservation was updated by another transaction. Please try again.", e);
        } catch (Exception e) {
            throw new Exception("Failed to save the reservation. Please try again.", e);
        }
		
	}

	private void sendMail(GuestInfo guestInfo, Reservation reservation, String subject, String mailContent) {
		
		
		try {
			Context context = new Context();
			context.setVariable("guestInfo", guestInfo);
			context.setVariable("reservation", reservation);
			mailService.sendMail(guestInfo.getEmail(), subject, context, mailContent);
		} catch (Exception e) {
			throw new AppException("Failed to send email!", e);
		}
	}
	
	@Override
	public List<ReservationDto> getPendingReservations() {

		List<Reservation> reservations = reservationRepository.findByStatus(ReservationStatus.PENDING)
				.orElseThrow(() -> new NoContentException("There is no pending reservation."));

		return reservationMapper.mapToDto(reservations);
	}
	
	@Override
	public List<ReservationDto> getCurrentMonthCompletedReservations() {
		
		YearMonth yearMonth = YearMonth.now();
		Integer month = yearMonth.getMonthValue();
		Integer year = yearMonth.getYear();
		
		List<Reservation> reservations = reservationRepository.findByStatusAndMonth(ReservationStatus.COMPLETED, month, year)
				.orElseThrow(() -> new NoContentException("There is no completed reservation for this month."));

		return reservationMapper.mapToDto(reservations);
	}
	
	@Override
	public List<ReservationDto> getCompletedReservationsForMonth(Integer month, Integer year) {
		
		List<Reservation> reservations = reservationRepository.findByStatusAndMonth(ReservationStatus.COMPLETED, month, year)
				.orElseThrow(() -> new NoContentException("There is no completed reservation for this month."));

		return reservationMapper.mapToDto(reservations);
	}
	
//	@Override
//	public List<ReservationDto> getReservationsWithFilters(String status, YearMonth yearMonth, Instant reservationDate, Instant checkInDate,
//			Instant checkOutDate) {
//		
//		List<Reservation> reservations = reservationRepository.findAll(Specification.where(ReservationSpecifications.hasStatus(status))
//				.and(ReservationSpecifications.hasReservationDate(reservationDate))
//				.and(ReservationSpecifications.hasCheckInDate(checkInDate))
//				.and(ReservationSpecifications.hasCheckOutDate(checkOutDate)));
//		
//		return reservationMapper.mapToDto(reservations);
//	}
	
	@Override
	public List<ReservationDto> getReservationsWithFilters(String status, YearMonth createdAtMonth, Instant reservationDate, Instant checkInDate,
			Instant checkOutDate) {
		
		Date createdAt = null;
		if (createdAtMonth != null) {
			LocalDate localDate = createdAtMonth.atDay(1);
			createdAt = Date.valueOf(localDate);
		}
		
		ReservationStatus reservationStatus = null;
	    if (status != null) {
	        try {
	        	reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            throw new InvalidStatusException("Invalid reservation status: " + status);
	        }
	    }
	    
		List<Reservation> reservations = reservationRepository.findWithFilters(reservationStatus, createdAt, reservationDate, checkInDate, checkOutDate);
		
		return reservationMapper.mapToDto(reservations);
	}

	@Override
	public List<ReservationDto> getCurrentMonthReservations() {

		YearMonth yearMonth = YearMonth.now();
		Instant startOfMonth = yearMonth.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
		Instant endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
		
		List<Reservation> reservations = reservationRepository.findByCreatedAtBetween(startOfMonth, endOfMonth)
				.orElseThrow(() -> new RecordNotFoundException("There is no reservations for this month."));
		
		return reservationMapper.mapToDto(reservations);
	}
	
	@Override
	public ReservationDto getReservationById(Long id) {

		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reservation Not Found with ID: " + id));
		
		return reservationMapper.mapToDto(reservation);
	}
	
	@Override
	@Transactional
	public void confirmReservationById(Long id) {
		
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reservation not found with ID: " + id));
		
		if (reservation.getStatus().equals(ReservationStatus.CANCELED)) {
			throw new BadRequestException("Cannot confirm the canceled reservation!");
		}
		
		reservation.setIsPaid(true);
		reservation.setStatus(ReservationStatus.CONFIRMED);
		
		List<ReservedRoom> reservedRooms = reservation.getReservedRooms();
		for (ReservedRoom reservedRoom : reservedRooms) {
			reservedRoom.setStatus(ReservedRoomStatus.CONFIRMED);
		}
		
		reservationRepository.save(reservation);
		reservedRoomRepository.saveAll(reservedRooms);
		sendMail(reservation.getGuestInfo(), reservation, "Payment Confirmation", "paymentConfirmation");
	}

	@Override
	@Transactional
	public void cancelReservationById(Long id) {

		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reservation not found with ID: " + id));
		
		reservation.setStatus(ReservationStatus.CANCELED);
		
		List<ReservedRoom> reservedRooms = reservation.getReservedRooms();
		for (ReservedRoom reservedRoom : reservedRooms) {
			reservedRoom.setStatus(ReservedRoomStatus.CANCELED);
		}
		
		reservationRepository.save(reservation);
		reservedRoomRepository.saveAll(reservedRooms);
		
		if (reservation.getIsPaid()) {
			sendMail(reservation.getGuestInfo(), reservation, "Reservation Cancellation", "paidReservationCancellation");
		} else {
			sendMail(reservation.getGuestInfo(), reservation, "Reservation Cancellation", "unpaidReservationCancellation");
		}
	}

	@Override
	public void checkAndUpdateExpired() {

		Instant now = Instant.now();

	    List<Reservation> pendingReservations = reservationRepository.findByStatus(ReservationStatus.PENDING)
	    		.orElse(null);

	    if (!pendingReservations.isEmpty()) {
	    	for (Reservation reservation : pendingReservations) {
		    	if (now.isAfter(reservation.getExpiredAt())) {
		    		reservation.setStatus(ReservationStatus.EXPIRED);
		            reservation.setIsExpired(true);
		        }
		    }
	    }
	    
	    reservationRepository.saveAll(pendingReservations);
	}
	
	@Override
	public List<ReservationDto> getNewReservations() {
		
		List<Reservation> reservations = reservationRepository.findNew();
		return reservationMapper.mapToDto(reservations);
	}
	
	@Override
	public Long countNewReservations() {
		
		Long reservationCount = reservationRepository.findNewCount();
		return reservationCount;
	}
	
	@Override
	public List<DailyIncomeForMonthResponse> getDailyIncomeForMonth(Integer month, Integer year) {
		
		List<DailyIncomeForMonthResponse> dailyIncomeForMonthResponses = reservationRepository.findDailyIncomeByMonth(month, year);
		return dailyIncomeForMonthResponses;
	}

	@Override
	public ReservationDto getCompletedReservationById(Long id) {
		
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Reservation Not Found with ID: " + id));
		
		return reservationMapper.mapToDto(reservation);
	}


}
