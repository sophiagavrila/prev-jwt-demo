package com.revature.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller just demonstrates the use of authorization by access
 * controlling the methods using @PreAuthorize annotation.
 * 
 * @PreAuthorize- This annotation contains a Spring Expression Language (SpEL)
 *                snippet that is assessed to determine if the request should be
 *                authenticated. 
 *                
 *                If access is not granted, the method is not
 *                executed and an HTTP Unauthorized is returned. 
 *                
 *                In practice, using the @PreAuthorize annotation on a controller 
 *                method is very similar to using HttpSecurity pattern matchers on a
 *                specific endpoint

 */
@RestController
@RequestMapping("/user")
public class UserController {

	// This endpoint is available to all
	@GetMapping("/allusers")
	public String displayUsers() {
		return "Display All Users";
	}

	// This endpoint is only available if the user has USER or ADMIN role
	@GetMapping("/displayuser")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public String displayToUser() {
		return "Display to both user and admin";
	}

	// This endpoint is only available if the user has USER or ADMIN role
	@GetMapping("/displayadmin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String displayToAdmin() {
		return "Display only to admin";
	}

}
