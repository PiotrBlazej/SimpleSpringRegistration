package example.service;

import java.util.List;
import java.util.UUID;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import example.model.User;
import example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository repository;
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}



	public UserServiceImpl() {
	}



	@Override
	public void saveUser(User user) {
		User findUserByEmail = repository.findUserByEmail(user.getEmail());
		if(findUserByEmail !=null)
			throw new ValidationException("User with this email exsist");
		else{
			user.setActive(false);
			String token = UUID.randomUUID().toString();
			user.setToken(token);
			user.setRole("User");
			user.setPassword(encoder.encode(user.getPassword()	));
			//send active emial
			
		repository.save(user);}

	}



	@Override
	public void activeUser(String token) {
		User user = repository.findUserByToken(token);
		if(user ==null)
			throw new NullPointerException("USer with token "+ token+ " not exsist" );
		else
		repository.activeUser(user.getEmail(), "", true);

	}

	@Override
	public User finUserByEmail(String email) {
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
