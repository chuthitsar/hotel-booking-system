package com.nexcode.hbs.service;

import com.nexcode.hbs.model.request.ChangePasswordRequest;
import com.nexcode.hbs.model.request.LoginRequest;
import com.nexcode.hbs.model.response.AuthenticationResponse;

public interface AuthService {

	AuthenticationResponse authenticate(LoginRequest request);

	void changePassword(ChangePasswordRequest request, Long userId);

	void resetPasswordToDefault(String username);
}
