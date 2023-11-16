package com.nexcode.hbs.model.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        throw ex;
    }
	
	@ExceptionHandler(InvalidCredentialsException.class)
    public final ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());
        body.put("path", ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
        
//        throw new InvalidCredentialsException("Incorrect");
    }
	
}
