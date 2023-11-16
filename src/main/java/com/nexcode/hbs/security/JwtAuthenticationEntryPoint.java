package com.nexcode.hbs.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.exception.InvalidCredentialsException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		log.error("Unauthorized error: {}", authException.getMessage());
		System.out.println("entry point");
		//throw new InvalidCredentialsException(authException.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Unauthorized->" + authException.getMessage());
	}

}
