package com.nexcode.hbs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.entity.Amenity;
import com.nexcode.hbs.model.entity.Role;
import com.nexcode.hbs.model.entity.RoleName;
import com.nexcode.hbs.model.entity.User;
import com.nexcode.hbs.repository.AmenityRepository;
import com.nexcode.hbs.repository.RoleRepository;
import com.nexcode.hbs.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final AmenityRepository amenityRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Application Started!!");
		
		if(!userRepository.existsByUsername("admin")) {
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("00000000"));
			Optional<Role> adminRoleOptional = roleRepository.findByName(RoleName.ROLE_ADMIN);
			
			Set<Role> roles = new HashSet<>();
			
			if (adminRoleOptional.isEmpty()) {
				Role adminRole = new Role();
				adminRole.setName(RoleName.ROLE_ADMIN);
				Role userRole = new Role();
				userRole.setName(RoleName.ROLE_USER);
				roles.add(roleRepository.save(adminRole));
				roleRepository.save(userRole);
			}
			admin.setRoles(roles);
			userRepository.save(admin);
			System.out.println("User : admin added!");
		}
		
		if(amenityRepository.count() == 0) {
			List<Amenity> amenities = new ArrayList<>();
			List<String> amenityNames = List.of("Comfortable Beds", "Towels and Toiletries", "Housekeeping", "Elevator", "Wake-up Service", "Kitchenette", "Concierge Service", "Private Meeting Room", "Garden View", 
					"Private Bathroom", "Television", "Breakfast", "Min-fridge", "In-room Jacuzzi", "Shuttle Service", "Private Living Room", "Pool", "Safety Deposit Box", "Wi-fi", "Air-conditioning/Heating", 
					"Room Service", "Chair", "Dinner", "Coffee/Tea Maker", "Ironing Facilities", "Laundry Facilities", "Fitness Center", "City View", "Electric Kettle");
			
			List<String> amenityIcons = List.of("bed", "dry_cleaning", "cleaning_services", "elevator", "alarm_smart_wake", "countertops", "concierge", "meeting_room", "home_and_garden",
					"bathroom", "tv", "brunch_dining", "kitchen", "hot_tub", "airport_suttle", "living", "pool", "enhanced_encryption", "wifi", "heat_pump",
					"room_service", "chair", "dining", "coffee_maker", "iron", "locl_laundry_service", "fitness_center", "apartment", "kettle");
			
			for (int i = 0; i < amenityNames.size(); i++) {
			    String amenityName = amenityNames.get(i);
			    String amenityIcon = amenityIcons.get(i);

			    Amenity amenity = new Amenity();
			    amenity.setName(amenityName);
			    amenity.setIcon(amenityIcon);
			    amenities.add(amenity);
			}
			amenityRepository.saveAll(amenities);
			System.out.println("Amenities Added!");
		}
		
	}

}
