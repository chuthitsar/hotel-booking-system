package com.nexcode.hbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexcode.hbs.model.entity.Amenity;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long>{

	boolean existsByName(String name);

	List<Amenity> findAllByIdIn(List<Long> amenityIds);

}
