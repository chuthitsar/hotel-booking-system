package com.nexcode.hbs.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nexcode.hbs.service.JwtService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;
		final String roles;
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		jwt = authHeader.replace("Bearer", "");
		username = jwtService.extractUsername(jwt);
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			if(jwtService.isTokenValid(jwt)) {
				Claims claims = jwtService.getClaims(jwt);
				roles = claims.get("roles", String.class);
				Long userId = Long.parseLong(claims.getId());
				List<String> authorityArray = Arrays.asList(roles.split(","));
				
				
				List<GrantedAuthority> authorities = authorityArray.stream()
						.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
				
				UserPrincipal userPrincipal = new UserPrincipal(userId, username, null, authorities);
				
				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(
								userPrincipal,
								null,
								authorities);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

	

}
