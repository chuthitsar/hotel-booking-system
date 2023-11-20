package com.nexcode.hbs.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nexcode.hbs.model.dto.RoomTypeAvailabilityDto;
import com.nexcode.hbs.model.dto.RoomTypeDto;
import com.nexcode.hbs.model.entity.Amenity;
import com.nexcode.hbs.model.entity.RoomType;
import com.nexcode.hbs.model.exception.BadRequestException;
import com.nexcode.hbs.model.exception.DuplicateEntityException;
import com.nexcode.hbs.model.exception.RecordNotFoundException;
import com.nexcode.hbs.model.mapper.RoomTypeMapper;
import com.nexcode.hbs.repository.AmenityRepository;
import com.nexcode.hbs.repository.RoomRepository;
import com.nexcode.hbs.repository.RoomTypeRepository;
import com.nexcode.hbs.service.RoomTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

	private final RoomTypeRepository roomTypeRepository;
	
	private final AmenityRepository amenityRepository;
	
	private final RoomRepository roomRepository;
	
	private final RoomTypeMapper roomTypeMapper;

	@Override
	public RoomTypeDto createRoomType(RoomTypeDto roomTypeDto) {

		if (roomTypeRepository.existsByName(roomTypeDto.getName())) {
			throw new DuplicateEntityException("Room type already exists with the same name: " + roomTypeDto.getName());
		}
		RoomType roomType = new RoomType();
		roomType.setName(roomTypeDto.getName());
		roomType.setMaximumCapacity(roomTypeDto.getMaximumCapacity());
		roomType.setSize(roomTypeDto.getSize());
		roomType.setPricePerNight(roomTypeDto.getPricePerNight());
		roomType.setDescription(roomTypeDto.getDescription());
		roomType.setImageUrl(roomTypeDto.getImageUrl());
		
		List<Long> amenityIds = roomTypeDto.getAmenityIds();
		List<Amenity> amenities = amenityRepository.findAllByIdIn(amenityIds);
		roomType.setAmenities(new ArrayList<>(amenities));
		
		int numberOfRoom = roomRepository.countByType_Name(roomTypeDto.getName());
		roomType.setTotalRoom(numberOfRoom);
		
		roomTypeRepository.save(roomType);
		System.out.println(numberOfRoom);
		
		return roomTypeMapper.mapToDto(roomType);
	}

	@Override
	public RoomTypeDto updateRoomType(Long id, RoomTypeDto roomTypeDto) {

		RoomType existingRoomType = roomTypeRepository.findById(id)
										.orElseThrow(() -> new RecordNotFoundException("Room Type not found with ID: " + id));
		
		if (!existingRoomType.getName().equals(roomTypeDto.getName()) && 
				roomTypeRepository.existsByName(roomTypeDto.getName())) {
			throw new DuplicateEntityException("Room type already exists with the same name: " + roomTypeDto.getName());
		}
		existingRoomType.setName(roomTypeDto.getName());
		existingRoomType.setMaximumCapacity(roomTypeDto.getMaximumCapacity());
		existingRoomType.setSize(roomTypeDto.getSize());
		existingRoomType.setPricePerNight(roomTypeDto.getPricePerNight());
		existingRoomType.setDescription(roomTypeDto.getDescription());
		existingRoomType.setImageUrl(roomTypeDto.getImageUrl());
		
		List<Long> amenityIds = roomTypeDto.getAmenityIds();
		List<Amenity> amenities = amenityRepository.findAllByIdIn(amenityIds);
		existingRoomType.setAmenities(new ArrayList<>(amenities));
		
		int numberOfRoom = roomRepository.countByType_Name(roomTypeDto.getName());
		existingRoomType.setTotalRoom(numberOfRoom);
		
		roomTypeRepository.save(existingRoomType);
		
		return roomTypeMapper.mapToDto(existingRoomType);
	}

	@Override
	public List<RoomTypeDto> getAllRoomTypes() {

		List<RoomType> roomTypes = roomTypeRepository.findAll();
		
		return roomTypeMapper.mapToDto(roomTypes);
	}

	@Override
	public RoomTypeDto getRoomTypeById(Long id) {

		RoomType roomType = roomTypeRepository.findById(id)
								.orElseThrow(() -> new RecordNotFoundException("Room type not found with ID: " + id));
		
		return roomTypeMapper.mapToDto(roomType);
	}

	@Override
	public void deleteRoomTypeById(Long id) {
		
		RoomType roomType = roomTypeRepository.findById(id)
								.orElseThrow(() -> new RecordNotFoundException("Room type not found with ID: " + id));

		if(!roomType.getRooms().isEmpty()) {
			throw new BadRequestException("This room type cannot be deleted! There are rooms with this room type. Please change their room type first!");
		}
		roomTypeRepository.delete(roomType);
	}

	@Override
	public List<RoomTypeAvailabilityDto> getAvailableRoomTypes(Instant checkInDate, Instant checkOutDate) {

		if (!checkInDate.isBefore(checkOutDate)) {
			throw new BadRequestException("Date Invalid!");
		}
		return roomRepository.getAvailableRoomTypes(checkInDate, checkOutDate);
	}

	@Override
	public RoomTypeDto getRoomTypeByName(String roomTypeName) {
		
		RoomType roomType = roomTypeRepository.findByName(roomTypeName)
				.orElseThrow(() -> new RecordNotFoundException("Room type not found with Name: " + roomTypeName));

		return roomTypeMapper.mapToDto(roomType);
	}
	
	@Override
	public void updateTotalRoomCount(Long roomTypeId) {
	 
	    RoomType roomType = roomTypeRepository.findById(roomTypeId).orElse(null);

	    if (roomType != null) {
	    	int updatedTotalRoom = roomRepository.countByType(roomType);
	        roomType.setTotalRoom(updatedTotalRoom);

	        roomTypeRepository.save(roomType);
	    }
	
	}

}
