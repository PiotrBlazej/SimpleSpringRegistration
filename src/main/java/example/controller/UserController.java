package example.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import example.model.User;
import example.service.UserService;

@Controller
public class UserController {
	
	private UserService service;
@Autowired
	public UserController(UserService service) {
		this.service = service;
	}

@GetMapping("/a")
@ResponseBody
public List<User> getAllUser(){
	return service.findAllUsers();
}

@GetMapping("/signup")
public String registerNewUser(Model model){
	model.addAttribute("newUser", new User());
	return "register";
}

@PostMapping("/signup")
public String saveNewUser(@Valid @ModelAttribute(value="newUser") User user, BindingResult bindingResult){
	if(bindingResult.hasErrors())
		return "register";
	else
	{
		service.saveUser(user);
		return "redirect:/a";
	}
}
	
	

}
