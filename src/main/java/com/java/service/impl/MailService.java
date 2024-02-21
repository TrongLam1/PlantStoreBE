package com.java.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.java.dto.MailDTO;
import com.java.service.IMailService;

@Service
public class MailService implements IMailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromMail;
	
	@Override
	public void sendMail(String mail, MailDTO mailDTO) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(fromMail);
		simpleMailMessage.setSubject(mailDTO.getSubject());
		simpleMailMessage.setText(mailDTO.getMessage());
		simpleMailMessage.setTo(mail);
		
		mailSender.send(simpleMailMessage);
	}
}
