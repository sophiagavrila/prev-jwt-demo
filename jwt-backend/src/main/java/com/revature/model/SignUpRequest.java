package com.revature.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Apart from these Entity classes there are Model classes related to the
 * request that is sent and the response received.
 * 
 * This class captures the data for the SignUp request which is sent when a new
 * user registers.
 */

@Getter @Setter
public class SignUpRequest {

	private String userName;
	private String email;
	private String password;
	private String[] roles;

}
