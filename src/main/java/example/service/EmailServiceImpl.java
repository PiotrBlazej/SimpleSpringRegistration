package example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender javaMailSender;
	@Autowired
	public EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmail(SimpleMailMessage email) {
		javaMailSender.send(email);

	}

	@Override
	public void sendActivactionEmail(String from, String to, String message) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(to);
		email.setSubject("Confirm registration");
		email.setText(message);
		email.setFrom(from);
		sendEmail(email);

	}

}
