package com.nexcode.hbs.service;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;

public interface JwtService {

	String extractUsername(String jwt);
	
	boolean isTokenValid(String jwt);
	
	Claims getClaims(String jwt);
	
	String generateToken(Authentication authentication);
}
