package com.nexcode.hbs.service.impl;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexcode.hbs.model.entity.User;
import com.nexcode.hbs.model.exception.BadRequestException;
import com.nexcode.hbs.model.exception.InvalidCredentialsException;
import com.nexcode.hbs.model.request.ChangePasswordRequest;
import com.nexcode.hbs.model.request.LoginRequest;
import com.nexcode.hbs.model.response.AuthenticationResponse;
import com.nexcode.hbs.repository.UserRepository;
import com.nexcode.hbs.service.AuthService;
import com.nexcode.hbs.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final int JWT_EXPIRATION_MS = 86400000;

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public AuthenticationResponse authenticate(LoginRequest request) {

		Date expiredAt = new Date((new Date()).getTime() + JWT_EXPIRATION_MS);

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
				|| authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			String jwt = jwtService.generateToken(authentication);
			return new AuthenticationResponse(jwt, expiredAt.toInstant().toString());

		} else {
			throw new InvalidCredentialsException("Username or Password is incorrect!");
		}
	}

	@Override
	public void changePassword(ChangePasswordRequest request, Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found!"));

		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			throw new BadRequestException("Password is incorrect!");
		}

		String encodedPassword = passwordEncoder.encode(request.getNewPassword());

		user.setPassword(encodedPassword);
		userRepository.save(user);
	}

	@Override
	public void resetPasswordToDefault(String username) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BadRequestException("User Not Found With Username : " + username));

		String defaultPassword = "00000000";
		String encodedPassword = passwordEncoder.encode(defaultPassword);

		user.setPassword(encodedPassword);
		userRepository.save(user);
	}
}
