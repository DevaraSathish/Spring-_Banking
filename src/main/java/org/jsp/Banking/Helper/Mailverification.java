package org.jsp.Banking.Helper;

import org.jsp.Banking.Dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class Mailverification {
	
	@Autowired
	JavaMailSender mailSender;
	
	public void sendmail(Customer customer) {
		MimeMessage mimeMessage=mailSender.createMimeMessage();
	MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
	try {
		helper.setFrom("sathish8008150394@gmail.com");
	} catch (MessagingException e) {
		e.printStackTrace();
	}
	try {
		helper.setTo(customer.getEmail());
	} catch (MessagingException e) {
		e.printStackTrace();
	}
	try {
		helper.setSubject("Mail verification");
	} catch (MessagingException e) {
	
		e.printStackTrace();
	}
	try {
		helper.setText("your otp for email verification"+customer.getOtp());
	} catch (MessagingException e) {
		e.printStackTrace();
	}
	mailSender.send(mimeMessage);
	
	}

}
