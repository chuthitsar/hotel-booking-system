package com.nexcode.hbs.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.entity.User;
import com.nexcode.hbs.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username).orElse(null);
		
		if(user == null) {
			throw new UsernameNotFoundException("User Not found with Username " + username);
		}
				
		return UserPrincipal.build(user);
	}

}
