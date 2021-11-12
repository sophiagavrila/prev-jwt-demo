package com.revature.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the response you get back when logging in. It includes
 * the authentication token and the roles user is authorized for.
 */

@Getter
@Setter
public class AuthResponse {

	private String token;
	private List<String> roles;

}
