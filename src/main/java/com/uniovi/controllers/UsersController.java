package com.uniovi.controllers;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniovi.entities.User;
import com.uniovi.services.RolesService;
import com.uniovi.services.SecurityService;
import com.uniovi.services.UserListWrapper;
import com.uniovi.services.UsersService;
import com.uniovi.validators.SignUpFormValidator;

@Controller
public class UsersController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private SignUpFormValidator signUpFormValidator;
		
	@Autowired
	private RolesService rolesService;
	
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Validated User user, BindingResult result) {
		signUpFormValidator.validate(user, result);
		if (result.hasErrors()) {
			return "signup";
		}
		user.setRole(rolesService.getRoles()[0]);
		user.setDinero(100);
		usersService.addUser(user);
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
		return "redirect:home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String home(Model model, Principal principal) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		return "home";

	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String signup(Model model, Principal principal) {
		return "redirect:home";
	}
	
	@RequestMapping(value = { "/user/list" }, method = RequestMethod.GET)
	public String getList(Model model, Principal principal) {
		UserListWrapper wrapper = new UserListWrapper();
	    wrapper.setUsers(new ArrayList<User>(usersService.getNotAdminUsers()));
		model.addAttribute("userListWrapper", wrapper);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		return "user/list";

	}
	
	@RequestMapping(value = { "/user/list" }, method = RequestMethod.POST)
	public String postList(@ModelAttribute UserListWrapper userListWrapper, Model model, Principal principal) {
		
		if(userListWrapper.getUsers()!=null)
			usersService.deleteUsers(userListWrapper.getUsers());
		
		userListWrapper.setUsers(new ArrayList<User>(usersService.getNotAdminUsers()));
		model.addAttribute("userListWrapper", userListWrapper);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		return "user/list";

	}
		
	
	
	
	
	
}
