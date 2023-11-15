package com.nexcode.hbs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexcode.hbs.model.request.ChangePasswordRequest;
import com.nexcode.hbs.model.request.LoginRequest;
import com.nexcode.hbs.model.request.ResetPasswordRequest;
import com.nexcode.hbs.model.response.ApiResponse;
import com.nexcode.hbs.model.response.AuthenticationResponse;
import com.nexcode.hbs.security.CurrentUser;
import com.nexcode.hbs.security.UserPrincipal;
import com.nexcode.hbs.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/admin/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
		return new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK);
	}

	@PostMapping("/admin/change-password")
	public ResponseEntity<ApiResponse> changeAdminPassword(@RequestBody ChangePasswordRequest request,
			@CurrentUser UserPrincipal currentUser) {

		authService.changePassword(request, currentUser.getId());
		return new ResponseEntity<>(new ApiResponse(true, "Password changed successfully."), HttpStatus.OK);
	}

	@PostMapping("/admin/reset-password")
	public ResponseEntity<ApiResponse> resetAdminPassword(@RequestBody ResetPasswordRequest request) {

		authService.resetPasswordToDefault(request.getUsername());
		return new ResponseEntity<>(new ApiResponse(true, "Password reset to default successfully"), HttpStatus.OK);
	}
	
}
