package com.nexcode.hbs.model.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DailyIncomeForMonthResponse {

	private Date date;
	private Long income;
}
