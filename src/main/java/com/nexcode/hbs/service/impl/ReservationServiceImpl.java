package com.nexcode.hbs.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	
//	@Override
//	@Transactional
//	public ReservationDto createReservation(ReservationDto reservationDto) throws Exception {
//		
//		if (!reservationDto.getCheckIn().isBefore(reservationDto.getCheckOut())) {
//			throw new BadRequestException("Date Invalid!");
//		}
//		
//		GuestInfoDto guestInfoDto = reservationDto.getGuestInfo();
//		GuestInfo guestInfo = new GuestInfo();
//	    guestInfo.setName(guestInfoDto.getName());
//	    guestInfo.setEmail(guestInfoDto.getEmail());
//	    guestInfo.setPhone(guestInfoDto.getPhone());
//	    guestInfo.setAddress(guestInfoDto.getAddress());
//
//	    guestInfo = guestInfoRepository.save(guestInfo);
//	    
//	    Reservation reservation = new Reservation();
//	    reservation.setGuestInfo(guestInfo);
//	    reservation.setNumberOfGuest(reservationDto.getNumberOfGuest());
//	    reservation.setTotalRoom(reservationDto.getTotalRoom());
//	    reservation.setCheckIn((reservationDto.getCheckIn()));
//	    reservation.setCheckOut(reservationDto.getCheckOut());
//	    reservation.setLengthOfStay(reservationDto.getLengthOfStay());
//	    reservation.setTotalCost(reservationDto.getTotalCost());
//	    reservation.setIsPaid(false);
//	    reservation.setSpecialRequest(reservationDto.getSpecialRequest());
//	    reservation.setExpiredAt(Instant.now().plus(24, ChronoUnit.HOURS));
//	    reservation.setIsExpired(false);
//	    reservation.setStatus(ReservationStatus.PENDING);
//	    
//	    reservation = reservationRepository.save(reservation);
//	    
//	    List<String> selectedRooms = reservationDto.getSelectedRooms();
//	    
//	    try {
//	    for (String roomType: selectedRooms) {
//	    	
//	    	List<Room> rooms = roomRepository.findAvailableRoomsByTypeAndDate(roomType, reservation.getCheckIn(), reservation.getCheckOut());
//
//	    	if (rooms.isEmpty()) {
//	    		throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//	    	}
//	    	
//	    	Room room = rooms.get(0);
//	    	ReservedRoom reservedRoom = new ReservedRoom();
//	    	reservedRoom.setRoom(room);
//	    	reservedRoom.setReservation(reservation);
//	    	reservedRoom.setCheckIn(reservation.getCheckIn());
//	    	reservedRoom.setCheckOut(reservation.getCheckOut());
//	    	reservedRoom.setPricePerNight(room.getType().getPricePerNight());
//	    	reservedRoom.setStatus(ReservedRoomStatus.PENDING);
//	    	reservedRoomRepository.save(reservedRoom);
//	    }
//        
//	    
//            sendMail(guestInfo, reservation, "Reservation Details", "reservation-confirmation");
//        } catch (OptimisticLockingFailureException e) {
//            throw new BadRequestException("The reservation was updated by another transaction. Please try again.", e);
//        } catch (Exception e) {
//            throw new Exception("Failed to save the reservation. Please try again.", e);
//        }
//	    
//	    ReservationDto createdReservationDto = reservationMapper.mapToDto(reservation);
//	    createdReservationDto.setSelectedRooms(selectedRooms);
//	    return createdReservationDto;
//		
//	}
	
//	@Override
//	@Transactional
//	public synchronized ReservationDto createReservation(ReservationDto reservationDto) throws Exception {
//	    if (!reservationDto.getCheckIn().isBefore(reservationDto.getCheckOut())) {
//	        throw new BadRequestException("Invalid date!");
//	    }
//
//	    GuestInfoDto guestInfoDto = reservationDto.getGuestInfo();
//	    GuestInfo guestInfo = new GuestInfo();
//	    guestInfo.setName(guestInfoDto.getName());
//	    guestInfo.setEmail(guestInfoDto.getEmail());
//	    guestInfo.setPhone(guestInfoDto.getPhone());
//	    guestInfo.setAddress(guestInfoDto.getAddress());
//
//	    guestInfo = guestInfoRepository.save(guestInfo);
//
//	    Reservation reservation = new Reservation();
//	    reservation.setGuestInfo(guestInfo);
//	    reservation.setNumberOfGuest(reservationDto.getNumberOfGuest());
//	    reservation.setTotalRoom(reservationDto.getTotalRoom());
//	    reservation.setCheckIn((reservationDto.getCheckIn()));
//	    reservation.setCheckOut(reservationDto.getCheckOut());
//	    reservation.setLengthOfStay(reservationDto.getLengthOfStay());
//	    reservation.setTotalCost(reservationDto.getTotalCost());
//	    reservation.setIsPaid(false);
//	    reservation.setSpecialRequest(reservationDto.getSpecialRequest());
//	    reservation.setExpiredAt(Instant.now().plus(24, ChronoUnit.HOURS));
//	    reservation.setIsExpired(false);
//	    reservation.setStatus(ReservationStatus.PENDING);
//
//	    reservation = reservationRepository.save(reservation);
//
//	    List<String> selectedRooms = reservationDto.getSelectedRooms();
//	
//	    synchronized (lock) {
//	    	for (String roomType : selectedRooms) {
//				List<Room> rooms = roomRepository.findAvailableRoomsByTypeAndDate(roomType, reservation.getCheckIn(),
//						reservation.getCheckOut());
//
//				if (rooms.isEmpty()) {
//					System.out.println("Room is empty");
//					throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//				}
//				else {
//					
//					for(Room room : rooms) {
//						if (!reservedRoomRepository.existsReservedRoomForDateRange(room, reservation.getCheckIn(),
//								reservation.getCheckOut())) {
//							ReservedRoom reservedRoom = new ReservedRoom();
//							reservedRoom.setRoom(room);
//							reservedRoom.setReservation(reservation);
//							reservedRoom.setCheckIn(reservation.getCheckIn());
//							reservedRoom.setCheckOut(reservation.getCheckOut());
//							reservedRoom.setPricePerNight(room.getType().getPricePerNight());
//							reservedRoom.setStatus(ReservedRoomStatus.PENDING);
//							System.out.println(reservation.getId());
//							System.out.println(room.getId());
//							System.out.println(reservation.getCheckIn().toString());
//							System.out.println(reservation.getCheckOut().toString());
//							System.out.println(reservedRoomRepository.existsReservedRoomForDateRange(room,
//									reservation.getCheckIn(), reservation.getCheckOut()));
//
//							if (reservedRoomRepository.existsReservedRoomForDateRange(room, reservation.getCheckIn(),
//									reservation.getCheckOut())) {
//								System.out.println(reservation.getId());
//								System.out.println(reservedRoomRepository.existsReservedRoomForDateRange(room,
//										reservation.getCheckIn(), reservation.getCheckOut()));
//								throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//							}
//							reservedRoomRepository.save(reservedRoom);
//							System.out.println(reservation.getId());
//							System.out.println(room.getId());
//							System.out.println(reservation.getCheckIn().toString());
//							System.out.println(reservation.getCheckOut().toString());
//							System.out.println(reservedRoomRepository.existsReservedRoomForDateRange(room,
//									reservation.getCheckIn(), reservation.getCheckOut()));
//							break;
//						}
//					}
//
//				//Room room = rooms.get(0);
//
//				}
//			}
//	    }
//
//	    try {
//	        sendMail(guestInfo, reservation, "Reservation Details", "reservation-confirmation");
//	    } catch (OptimisticLockingFailureException e) {
//	        throw new BadRequestException("The reservation was updated by another transaction. Please try again.", e);
//	    } 
////	    catch (Exception e) {
////	        throw new Exception("Failed to save the reservation. Please try again.", e);
////	    }
//
//	    ReservationDto createdReservationDto = reservationMapper.mapToDto(reservation);
//	    createdReservationDto.setSelectedRooms(selectedRooms);
//	    return createdReservationDto;
//	}
	
//	@Override
//	@Transactional
//	public ReservationDto createReservation(ReservationDto reservationDto) throws Exception {
//	    if (!reservationDto.getCheckIn().isBefore(reservationDto.getCheckOut())) {
//	        throw new BadRequestException("Invalid date!");
//	    }
//
//	    GuestInfoDto guestInfoDto = reservationDto.getGuestInfo();
//	    GuestInfo guestInfo = new GuestInfo();
//	    guestInfo.setName(guestInfoDto.getName());
//	    guestInfo.setEmail(guestInfoDto.getEmail());
//	    guestInfo.setPhone(guestInfoDto.getPhone());
//	    guestInfo.setAddress(guestInfoDto.getAddress());
//
//	    guestInfo = guestInfoRepository.save(guestInfo);
//
//	    Reservation reservation = new Reservation();
//	    reservation.setGuestInfo(guestInfo);
//	    reservation.setNumberOfGuest(reservationDto.getNumberOfGuest());
//	    reservation.setTotalRoom(reservationDto.getTotalRoom());
//	    reservation.setCheckIn((reservationDto.getCheckIn()));
//	    reservation.setCheckOut(reservationDto.getCheckOut());
//	    reservation.setLengthOfStay(reservationDto.getLengthOfStay());
//	    reservation.setTotalCost(reservationDto.getTotalCost());
//	    reservation.setIsPaid(false);
//	    reservation.setSpecialRequest(reservationDto.getSpecialRequest());
//	    reservation.setExpiredAt(Instant.now().plus(24, ChronoUnit.HOURS));
//	    reservation.setIsExpired(false);
//	    reservation.setStatus(ReservationStatus.PENDING);
//
//	    reservation = reservationRepository.save(reservation);
////	    System.out.println("save");
////	    List<Reservation> previousReservations = getReservationsCreatedLast5SecondsAndCheckInOutRange(reservationDto.getCheckIn(), reservationDto.getCheckOut(), reservation.getId());
////	    if (!previousReservations.isEmpty()) {
////	    	Thread.sleep(4000);
////	    	System.out.println("Thread sleeping for 4 seconds");
////	    	for (Reservation previousReservation : previousReservations) {
////	    		System.out.println(previousReservation.getId());
////	    	}
////	    }
//	    List<String> selectedRooms = reservationDto.getSelectedRooms();
//	    ReservedRoom reservedRoom = new ReservedRoom();
//	    for (String roomType : selectedRooms) {
//	    	try {
//			List<Room> rooms = roomRepository.findAvailableRoomsByTypeAndDate(roomType, reservation.getCheckIn(),
//					reservation.getCheckOut());
//
//			if (rooms.isEmpty()) {
//				throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//			} else {
//
//				for (Room room : rooms) {
//					if (!reservedRoomRepository.existsReservedRoomForDateRange(room, reservation.getCheckIn(),
//							reservation.getCheckOut())) {
//						
//						room.setRent(room.getRent()+1);
//						roomRepository.save(room);
//						reservedRoom.setRoom(room);
//						reservedRoom.setReservation(reservation);
//						reservedRoom.setCheckIn(reservation.getCheckIn());
//						reservedRoom.setCheckOut(reservation.getCheckOut());
//						reservedRoom.setPricePerNight(room.getType().getPricePerNight());
//						reservedRoom.setStatus(ReservedRoomStatus.PENDING);
//						
//						reservedRoomRepository.save(reservedRoom);
//						
//						System.out.println(reservation.getId());
//						System.out.println(room.getId());
//
//						System.out.println("save reserved room");
//						if(reservedRoomRepository.existsOverlappingReservedRoomsForRoom(reservation.getId(),room.getId(), reservedRoom.getCheckIn(), reservedRoom.getCheckOut())) {
//							System.out.println("Overlapping");
//							List<Room> otherRooms = roomRepository.findAvailableRoomsByTypeAndDate(roomType, reservation.getCheckIn(),
//									reservation.getCheckOut());
//
//							if (otherRooms.isEmpty()) {
//								System.out.println("No other room");
//								throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//							} else {
//								System.out.println("Change room");
//								reservedRoom.setRoom(otherRooms.get(0));
//								reservedRoomRepository.save(reservedRoom);
//							}
//						}
//						break;
//					}
//				}
//
//			}
//	    } catch(CannotAcquireLockException | LockAcquisitionException e) {
//	    	System.out.println("Lock Exception!");
//	    	List<Reservation> previousReservations = getReservationsCreatedLast5SecondsAndCheckInOutRange(reservationDto.getCheckIn(), reservationDto.getCheckOut(), reservation.getId());
//		    if (!previousReservations.isEmpty()) {
//		    	throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//		    }
//	    	List<Room> otherRooms = roomRepository.findAvailableRoomsByTypeAndDate(roomType, reservation.getCheckIn(),
//					reservation.getCheckOut());
//
//			if (otherRooms.isEmpty()) {
//				System.out.println("No other room");
//				throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//			} else {
//				System.out.println("Change room");
//				reservedRoom.setRoom(otherRooms.get(0));
//				reservedRoomRepository.save(reservedRoom);
//			}
//	    }catch (DataAccessException ex) {
//	    	if (ex.getCause() instanceof SQLTransactionRollbackException) {
//	            SQLTransactionRollbackException rollbackEx = (SQLTransactionRollbackException) ex.getCause();
//	    	}
//	    	System.out.println("DataAccess Exception!");
//	    	List<Reservation> previousReservations = getReservationsCreatedLast5SecondsAndCheckInOutRange(reservationDto.getCheckIn(), reservationDto.getCheckOut(), reservation.getId());
//		    if (!previousReservations.isEmpty()) {
//		    	throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//		    }
//	    	List<Room> otherRooms = roomRepository.findAvailableRoomsByTypeAndDate(roomType, reservation.getCheckIn(),
//					reservation.getCheckOut());
//
//			if (otherRooms.isEmpty()) {
//				System.out.println("No other room");
//				throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//			} else {
//				System.out.println("Change room");
//				reservedRoom.setRoom(otherRooms.get(0));
//				reservedRoomRepository.save(reservedRoom);
//			}
//	    }
//	    	
//	    }
//	    try {
//	        sendMail(guestInfo, reservation, "Reservation Details", "reservation-confirmation");
//	    } catch (OptimisticLockingFailureException e) {
//	    	throw new BadRequestException("Reservation Failed!");
//	    } 
////	    catch (Exception e) {
////	        throw new Exception("Failed to save the reservation. Please try again.", e);
////	    }
//
//	    ReservationDto createdReservationDto = reservationMapper.mapToDto(reservation);
//	    createdReservationDto.setSelectedRooms(selectedRooms);
//	    return createdReservationDto;
//	}
	
	@Override
	@Transactional
	public ReservationDto createReservation(ReservationDto reservationDto) throws Exception {
	    
		if (!reservationDto.getCheckIn().isBefore(reservationDto.getCheckOut())) {
	        throw new BadRequestException("Invalid date!");
	    }

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
	    reservation.setCheckIn((reservationDto.getCheckIn()));
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
	    
	    final int maxRetries = 2;
		int retryCount = 0;
		boolean success = false;
		while (!success && retryCount < maxRetries) {
			try {
				success = assignRooms(selectedRooms, reservation);
			} catch (ObjectOptimisticLockingFailureException e) {
				log.warn("Optimistic locking failure detected. Handling the exception: ");
				retryCount++;
				if (retryCount < maxRetries) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignored) {
						Thread.currentThread().interrupt();
					}
				}
				success = false;
			}	
		}
		
		if (!success) {
			throw new BadRequestException("Reservation failed after retry.");
		}
	    
	    try {
	        sendMail(guestInfo, reservation, "Reservation Details", "reservation-confirmation");
	    } catch (Exception e) {
	    	throw new BadRequestException("Reservation Failed!");
	    } 

	    ReservationDto createdReservationDto = reservationMapper.mapToDto(reservation);
	    createdReservationDto.setSelectedRooms(selectedRooms);
	    return createdReservationDto;
	}
	
	@Transactional
	private boolean assignRooms(List<String> selectedRooms, Reservation reservation) {
		System.out.println("assignRooms count");
		for (String roomType : selectedRooms) {
			System.out.println("Before Getting Available rooms!");
			Pageable pageable = PageRequest.of(0, 1);
			List<Room> rooms = roomRepository.findFirstAvailableRoomsByTypeAndDateWithLock(roomType, reservation.getCheckIn(),
					reservation.getCheckOut(), pageable);
			System.out.println("Getting Available rooms!");
			if (rooms.isEmpty()) {
				System.out.println("Room is empty!");
				throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
			} else {
				ReservedRoom reservedRoom = new ReservedRoom();
				try {
					Room room = rooms.get(0);
					System.out.println(room.getId());
					
					
//					roomRepository.save(room);
//					roomRepository.flush();
					
					reservedRoom.setRoom(room);
					//room.setRent(room.getRent() + 1);
					//roomRepository.save(room);
					//roomRepository.flush();
					reservedRoom.setReservation(reservation);
					reservedRoom.setCheckIn(reservation.getCheckIn());
					reservedRoom.setCheckOut(reservation.getCheckOut());
					reservedRoom.setPricePerNight(room.getType().getPricePerNight());
					reservedRoom.setStatus(ReservedRoomStatus.PENDING);
					
					reservedRoomRepository.save(reservedRoom);
					reservedRoomRepository.flush();
					
					System.out.println(reservation.getId());
					System.out.println(room.getId());
					System.out.println("save reserved room");
					//return true;
				} catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
					log.warn("OptimisticLock detected. Retrying reservation...");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ignored) {
						Thread.currentThread().interrupt();
					}
					return false;
				}
			}
		}
		return true;
	}
	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	private boolean assignRooms(List<String> selectedRooms, Reservation reservation) {
//		System.out.println("assignRooms count");
//		for (String roomType : selectedRooms) {
//
//			List<Room> rooms = roomRepository.findAvailableRoomsByTypeAndDate(roomType, reservation.getCheckIn(),
//					reservation.getCheckOut());
//
//			if (rooms.isEmpty()) {
//				throw new BadRequestException("The selected room is not available anymore. Reservation Failed!");
//			} else {
//				ReservedRoom reservedRoom = new ReservedRoom();
//				try {
//					Room room = rooms.get(0);
//					System.out.println(room.getId());
//					
//					room.setRent(room.getRent() + 1);
//					roomRepository.save(room);
//					roomRepository.flush();
//					reservedRoom.setRoom(room);
//					reservedRoom.setReservation(reservation);
//					reservedRoom.setCheckIn(reservation.getCheckIn());
//					reservedRoom.setCheckOut(reservation.getCheckOut());
//					reservedRoom.setPricePerNight(room.getType().getPricePerNight());
//					reservedRoom.setStatus(ReservedRoomStatus.PENDING);
//					
//					reservedRoomRepository.save(reservedRoom);
//					reservedRoomRepository.flush();
//					
//					System.out.println(reservation.getId());
//					System.out.println(room.getId());
//					System.out.println("save reserved room");
//					return true;
//				} catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
//					log.warn("OptimisticLock detected. Retrying reservation...");
//					if (rooms.size() > 1) {
//						try {
//							Room room = rooms.get(1);
//							System.out.println("Room Id for second index: ");
//							System.out.println(room.getId());
//							room.setRent(room.getRent() + 1);
//							roomRepository.save(room);
//							roomRepository.flush();
//							reservedRoom.setRoom(rooms.get(1));
//							reservedRoom.setReservation(reservation);
//							reservedRoom.setCheckIn(reservation.getCheckIn());
//							reservedRoom.setCheckOut(reservation.getCheckOut());
//							reservedRoom.setPricePerNight(room.getType().getPricePerNight());
//							reservedRoom.setStatus(ReservedRoomStatus.PENDING);
//							
//							reservedRoomRepository.save(reservedRoom);
//							reservedRoomRepository.flush();
//							
//							return true;
//						} catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
//							log.warn("OptimisticLock detected for 2nd index. Retrying reservation...");
//							return false;
//						}
//					} else {
//						return false;
//					}
//					
//				}
//			}
//		}
//		return true;
//	}

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
		Instant startOfMonth = yearMonth.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
		Instant endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
		
		List<Reservation> reservations = reservationRepository.findByStatusAndCreatedAtBetween(ReservationStatus.COMPLETED, startOfMonth, endOfMonth);
		
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
	public List<ReservationDto> getReservationsWithFilters(String status, String monthFilter, String reservationDate, String checkInDate,
			String checkOutDate) {
		
		Integer month = null;
		Integer year = null;
		if (monthFilter != null) {
			YearMonth monthFilterFormat = YearMonth.parse(monthFilter, DateTimeFormatter.ofPattern("yyyy-MM"));
			month = monthFilterFormat.getMonthValue();
			year = monthFilterFormat.getYear();
		}
		
		LocalDate reservationDateFormat = null;
		LocalDate checkIn = null;
		LocalDate checkOut = null;
		
		if (reservationDate != null) {
			reservationDateFormat = LocalDate.parse(reservationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		if (checkInDate != null) {
			checkIn = LocalDate.parse(checkInDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		if (checkOutDate != null) {
			checkOut = LocalDate.parse(checkOutDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		
		ReservationStatus reservationStatus = null;
	    if (status != null) {
	        try {
	        	reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            throw new InvalidStatusException("Invalid reservation status: " + status);
	        }
	    }
	    
		List<Reservation> reservations = reservationRepository.findWithFilters(month, year, reservationStatus, reservationDateFormat, checkIn, checkOut);
		
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
		sendMail(reservation.getGuestInfo(), reservation, "Payment Confirmation", "payment-confirmation");
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
			sendMail(reservation.getGuestInfo(), reservation, "Reservation Cancellation", "paid-reservation-cancellation");
		} else {
			sendMail(reservation.getGuestInfo(), reservation, "Reservation Cancellation", "unpaid-reservation-cancellation");
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
	public Long countNewReservations() {
		
		Long reservationCount = reservationRepository.countByStatus(ReservationStatus.PENDING);
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
