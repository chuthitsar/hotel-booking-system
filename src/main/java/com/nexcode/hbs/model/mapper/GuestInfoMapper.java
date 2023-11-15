package com.nexcode.hbs.model.mapper;

import com.nexcode.hbs.model.dto.GuestInfoDto;
import com.nexcode.hbs.model.entity.GuestInfo;

public interface GuestInfoMapper {

	GuestInfoDto mapToDto(GuestInfo guestInfo);
}
