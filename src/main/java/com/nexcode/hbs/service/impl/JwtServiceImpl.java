package com.nexcode.hbs.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.nexcode.hbs.security.UserPrincipal;
import com.nexcode.hbs.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService{
	
	private final String SECRET_KEY = "HotelBookingSystemSecretKeyHotelBookingSystemSecretKey";
	
	private final int JWT_EXPIRATION_MS = 86400000;

	@Override
	public String extractUsername(String jwt) {
		return getClaims(jwt).getSubject();
	}
	
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public boolean isTokenValid(String jwt) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
			.parseClaimsJws(jwt).getBody();
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature -> Message: {} ", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token -> Message: {} ", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token -> Message: {} ", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token -> Message: {} ", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty -> Message: {} ", e.getMessage());
		}
		return false;
		
	}
	
//	private boolean isTokenExpired(String jwt) {
//		return extractExpiration(jwt).before(new Date());
//	}
	
//	private Date extractExpiration(String jwt) {
//		return getClaims(jwt).getExpiration();
//	}
	
	@Override
	public Claims getClaims(String jwt) {
		
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
				.parseClaimsJws(jwt).getBody();
	}

	@Override
	public String generateToken(Authentication authentication) {
		
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		final String authorities = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.joining(","));
		
		Map<String, Object> expected = new HashMap<>();
		expected.put("roles", authorities);
		
		return Jwts.builder().setClaims(expected)
							.setId(Long.toString(userPrincipal.getId()))
							.setSubject(userPrincipal.getUsername())
							.setIssuedAt(new Date(System.currentTimeMillis()))
							.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
							.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
		
	}
}
