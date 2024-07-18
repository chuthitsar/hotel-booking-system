package com.nexcode.hbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nexcode.hbs.security.JwtAuthenticationEntryPoint;
import com.nexcode.hbs.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;
	
	private final AuthenticationProvider authenticationProvider;
	
	private final JwtAuthenticationEntryPoint entryPoint;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable();
		
		http.authorizeHttpRequests().antMatchers("/api/auth/admin/login",
												"/api/auth/admin/reset-password").permitAll()
									.antMatchers(HttpMethod.GET, "/api/amenities/**", "/api/room-types/**").permitAll()
									.antMatchers(HttpMethod.POST, "/api/room-types/availability", "/api/reservations/**").permitAll()
									.antMatchers("/error", "/actuator/**").permitAll()
									.anyRequest().authenticated().and()
						.exceptionHandling().authenticationEntryPoint(entryPoint).and().sessionManagement()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
						.authenticationProvider(authenticationProvider)
						.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
