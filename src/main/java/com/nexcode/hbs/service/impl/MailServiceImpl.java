package com.nexcode.hbs.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nexcode.hbs.service.MailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private final JavaMailSender mailSender;
	
	private final TemplateEngine templateEngine;

	@Override
	public void sendMail(String to, String subject, Context context, String mailContent) {
		 MimeMessagePreparator messagePreparator = mimeMessage -> {
	            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	            messageHelper.setFrom("nextel2023.mm@gmail.com");
	            messageHelper.setTo(to);
	            messageHelper.setSubject(subject);
	            String content = templateEngine.process(mailContent, context);
	            messageHelper.setText(content, true);
	        };
	        mailSender.send(messagePreparator);
	}
}
