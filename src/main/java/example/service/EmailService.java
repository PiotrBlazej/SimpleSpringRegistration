package example.service;

import org.springframework.mail.SimpleMailMessage;


public interface EmailService {

	void sendEmail(SimpleMailMessage email);
	void sendActivactionEmail(String from, String to, String message);
}
