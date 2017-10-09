package example.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/allUser")
	@ResponseBody
	public List<User> getAllUser() {
		return service.findAllUsers();
	}

	@GetMapping("/")
	public String mainPage(Model model) {
		User user = service.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		if (user != null) {
			model.addAttribute("user", user);
		}
		return "home";
	}

	@GetMapping("/signup")
	public String registerNewUser(Model model) {
		User user = service.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		if (user == null) {
			model.addAttribute("newUser", new User());
			return "register";
		} else
			return "redirect:/";
	}

	@PostMapping("/signup")
	public String saveNewUser(@Valid @ModelAttribute(value = "newUser") User user, BindingResult bindingResult,
			HttpServletRequest request, Model model) {
		if (bindingResult.hasErrors())
			return "register";
		else {
			try {
				service.saveUser(user, request);
			} catch (ValidationException e) {
				bindingResult.rejectValue("email", "email.exsist", "User with this email exsist");
				return "register";
			}
			model.addAttribute("confirmMessage", "Activation message was sent to: " + user.getEmail());
			return "confirm";
		}
	}

	@GetMapping("/confirm")
	public String confirmRegistration(@RequestParam(value = "token", required = false) String token, Model model) {
		try {
			service.activeUser(token);
		} catch (NullPointerException e) {
			model.addAttribute("errorMessage", "Invalid confirmation token");
			return "confirm";
		}
		model.addAttribute("confirmMessage", "Your account is actived");
		return "confirm";
	}

	@GetMapping("/signin")
	public String loginUser(Model model) {

		User user = service.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		if (user != null) {
			model.addAttribute("user", user);
			return "home";
		} else
			return "login";
	}

	@GetMapping("/signout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

	}

}
