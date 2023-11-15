package com.nexcode.hbs.model.mapper;

import java.util.List;

import com.nexcode.hbs.model.dto.UserDto;
import com.nexcode.hbs.model.entity.User;
import com.nexcode.hbs.model.response.UserResponse;

public interface UserMapper {

	UserDto mapToDto(User user);
	List<UserDto> mapToDto(List<User> users);
	UserResponse mapToResponse(UserDto userDto);
	List<UserResponse> mapToResponse(List<UserDto> userDtos);
}
