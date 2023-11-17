package com.nexcode.hbs.model.response;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyIncomeForMonthResponse {

	private String date;
	private Long income;
	
	public DailyIncomeForMonthResponse(Date date, Long income) {
		super();
		this.date = date.toString();
		this.income = income;
	}
	
}
