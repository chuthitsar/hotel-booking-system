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

@RequiredArgsConstructor
@Component
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
			List<String> amenityNames = List.of("Comfortable Beds", "Private Bathroom", "Towels and Toiletries", "Wi-Fi", "Television", "Air Conditioning/Heating", "Safety Deposit Box", "Mini-Fridge", "Coffee/Tea Maker", "Room Service", 
												"Breakfast", "Garden View", "Sea View", "24-Hour Front Desk", "Parking", "Elevator", "Fitness Center", "Swimming Pool", "Fine Dining Area", 
												"Laundry Facilities", "Ironing Facilities", "Separate Living Room", "Wake-Up Service", "Concierge Services", "Shuttle Service", "Business Center", "Kitchenette", "In-Room Jacuzzi", "Private Bar");
			
			List<String> amenityIcons = List.of("Comfortable Beds", "Private Bathroom", "Towels and Toiletries", "Wi-Fi", "Television", "Air Conditioning/Heating", "Safety Deposit Box", "Mini-Fridge", "Coffee/Tea Maker", "Room Service", 
												"Breakfast", "Garden View", "Sea View", "24-Hour Front Desk", "Parking", "Elevator", "Fitness Center", "Swimming Pool", "Fine Dining Area", 
												"Laundry Facilities", "Ironing Facilities", "Separate Living Room", "Wake-Up Service", "Concierge Services", "Shuttle Service", "Business Center", "Kitchenette", "In-Room Jacuzzi", "Private Bar");
			
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
