package example.service;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import example.model.User;
import example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository repository;
	private BCryptPasswordEncoder encoder;
	private EmailService emailService;

	@Value("${spring.mail.from}")
	private String mailFrom;

	@Autowired
	public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder, EmailService emailService) {
		this.repository = repository;
		this.encoder = encoder;
		this.emailService = emailService;
	}

	@Override
	public void saveUser(User user, HttpServletRequest request) {
		User findUserByEmail = repository.findUserByEmail(user.getEmail());
		if (findUserByEmail != null)
			throw new ValidationException("User with this email exsist");
		else {
			user.setActive(false);
			String token = UUID.randomUUID().toString();
			user.setToken(token);
			user.setRole("User");
			user.setPassword(encoder.encode(user.getPassword()));
			String confirmationMessage = "To confirm your e-mail address, please click the link below:\n"
					+ request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort()
					+ "/confirm?token=" + token;

			repository.save(user);
			emailService.sendActivactionEmail(mailFrom, user.getEmail(), confirmationMessage);
		}

	}

	@Override
	public void activeUser(String token) {
		User user = repository.findUserByToken(token);
		if (user == null)
			throw new NullPointerException("USer with token " + token + " not exsist");
		else
			repository.activeUser(user.getEmail(), "", true);

	}

	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return repository.findUserByEmail(email);
	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public User findUserById(int id) {
		return repository.findUserById(id);
	}

}
