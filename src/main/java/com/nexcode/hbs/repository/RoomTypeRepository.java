package com.nexcode.hbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexcode.hbs.model.entity.RoomType;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long>{

	boolean existsByName(String name);

	Optional<RoomType> findById(Long id);

	void deleteById(Long id);

	Optional<RoomType> findByName(String roomTypeName);

}
