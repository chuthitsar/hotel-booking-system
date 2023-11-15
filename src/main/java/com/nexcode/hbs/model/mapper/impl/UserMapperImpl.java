package com.nexcode.hbs.model.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.UserDto;
import com.nexcode.hbs.model.entity.User;
import com.nexcode.hbs.model.mapper.UserMapper;
import com.nexcode.hbs.model.response.UserResponse;

@Component
public class UserMapperImpl implements UserMapper{

	@Override
	public UserDto mapToDto(User user) {

		if(user==null) {
			return null;
		}
		
		//Mapping
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		userDto.setRoles(user.getRoles());
		
		return userDto;
	}

	@Override
	public List<UserDto> mapToDto(List<User> users) {
		
		if(users==null) {
			return null;
		}
		
		List<UserDto> usersDto = new ArrayList<>();
		
		//Mapping
		for(User user : users) {
			UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setUsername(user.getUsername());
			userDto.setRoles(user.getRoles());
			usersDto.add(userDto);
		}
		
		return usersDto;
	}


	@Override
	public UserResponse mapToResponse(UserDto userDto) {
		if(userDto==null) {
			return null;
		}
		
		//Mapping
		UserResponse userResponse = new UserResponse();
		userResponse.setId(userDto.getId());
		userResponse.setUsername(userDto.getUsername());
		userResponse.setRoles(userDto.getRoles());
		
		return userResponse;
	}

	@Override
	public List<UserResponse> mapToResponse(List<UserDto> userDtos) {
		if(userDtos==null) {
			return null;
		}
		
		List<UserResponse> userResponses = new ArrayList<>();
		
		//Mapping
		for(UserDto userDto : userDtos) {
			UserResponse userResponse = new UserResponse();
			userResponse.setId(userDto.getId());
			userResponse.setUsername(userDto.getUsername());
			userResponse.setRoles(userDto.getRoles());
			userResponses.add(userResponse);
		}
		
		return userResponses;
	}
}
