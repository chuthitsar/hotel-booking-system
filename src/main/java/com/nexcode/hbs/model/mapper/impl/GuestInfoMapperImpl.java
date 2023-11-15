package com.nexcode.hbs.model.mapper.impl;

import org.springframework.stereotype.Component;

import com.nexcode.hbs.model.dto.GuestInfoDto;
import com.nexcode.hbs.model.entity.GuestInfo;
import com.nexcode.hbs.model.mapper.GuestInfoMapper;

@Component
public class GuestInfoMapperImpl implements GuestInfoMapper {

	@Override
	public GuestInfoDto mapToDto(GuestInfo guestInfo) {

		if (guestInfo == null) {
			return null;
		}
		
		GuestInfoDto guestInfoDto = new GuestInfoDto();
		guestInfoDto.setName(guestInfo.getName());
		guestInfoDto.setEmail(guestInfo.getEmail());
		guestInfoDto.setPhone(guestInfo.getPhone());
		guestInfoDto.setAddress(guestInfo.getAddress());
		
		return guestInfoDto;
	}

}
