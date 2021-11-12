package com.revature.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.data.RoleRepository;
import com.revature.data.UserRepository;
import com.revature.model.AuthResponse;
import com.revature.model.CustomUserBean;
import com.revature.model.Role;
import com.revature.model.Roles;
import com.revature.model.SignUpRequest;
import com.revature.model.User;
import com.revature.security.JwtTokenUtil;

/**
 * This controller class has two methods-
 * 
 * - 1. userSignup() which is a handler method for any POST request to /auth/signup. 
 * - 2. userLogin() which is a handler method for any POST request to /auth/login.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody User user) {
		// System.out.println("AuthController -- userLogin");
		
		// authenticate the user and set the authentication object in SecurityContext.
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Then generate the token and get the list of user roles
		String token = jwtTokenUtil.generateJwtToken(authentication);
		CustomUserBean userBean = (CustomUserBean) authentication.getPrincipal();
		List<String> roles = userBean.getAuthorities().stream().map(auth -> auth.getAuthority())
				.collect(Collectors.toList());

		// Set both token and list of roles in the response that is sent back.
		AuthResponse authResponse = new AuthResponse();
		authResponse.setToken(token);
		authResponse.setRoles(roles);
		return ResponseEntity.ok(authResponse);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> userSignup(@Valid @RequestBody SignUpRequest signupRequest) {
		
		/**
		 * In the userSignup() method first thing is to verify 
		 * that the passed user name or email are not already in use.
		 */
		if (userRepository.existsByUserName(signupRequest.getUserName())) {
			return ResponseEntity.badRequest().body("Username is already taken");
		}
		
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body("Email is already taken");
		}
		
		// Then create a User object using the values passed in SignupRequest object.
		User user = new User();
		Set<Role> roles = new HashSet<>();
		
		user.setUserName(signupRequest.getUserName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(encoder.encode(signupRequest.getPassword()));
		// System.out.println("Encoded password--- " + user.getPassword());
		
		// Extract the roles and set those also in the User object and then save the User.
		String[] roleArr = signupRequest.getRoles();

		if (roleArr == null) {
			roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
		}
		
		for (String role : roleArr) {
			switch (role) {
			case "admin":
				roles.add(roleRepository.findByRoleName(Roles.ROLE_ADMIN).get());
				break;
			case "user":
				roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
				break;
			default:
				return ResponseEntity.badRequest().body("Specified role not found");
			}
		}
		
		user.setRoles(roles);
		userRepository.save(user);
		
		// If everything works fine then set the status code as ok in the response with the message.
		return ResponseEntity.ok("User signed up successfully");
	}

}
