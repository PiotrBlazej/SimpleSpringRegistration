package example.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import example.model.User;

public interface UserService {
	void saveUser(User user, HttpServletRequest request);
	void activeUser(String token);
	User findUserByEmail(String email);
	User findUserById(int id);
	List<User> findAllUsers();
	

}
