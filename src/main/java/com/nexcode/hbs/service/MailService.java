package com.nexcode.hbs.service;

import org.thymeleaf.context.Context;

public interface MailService {
	
	public void sendMail(String to, String subject, Context context, String mailContent);
}
