package com.java.service;

import com.java.dto.MailDTO;

public interface IMailService {

	public void sendMail(String mail, MailDTO mailDTO);
	
}
