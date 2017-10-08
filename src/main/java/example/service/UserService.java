package example.service;

import java.util.List;

import example.model.User;

public interface UserService {
	void saveUser(User user);
	void activeUser(String token);
	User finUserByEmail(String email);
	User findUserById(int id);
	List<User> findAllUsers();
	

}
