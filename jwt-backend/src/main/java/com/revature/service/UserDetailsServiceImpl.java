package com.revature.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.revature.data.UserRepository;
import com.revature.model.CustomUserBean;
import com.revature.model.User;

/**
 * Within Spring security there is an interface
 * org.springframework.security.core.userdetails.UserDetailsService that has a
 * method loadUserByUsername(java.lang.String username) to load user-specific
 * data.
 * 
 * We’ll provide a concrete implementation of this interface where the
 * loadUserByUsername() method implementation acts as a wrapper over the
 * userRepository.findByUserName() method call.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

		/**
		 * The returned user is passed to the CustomUserBean.createInstance() 
		 * method to create instance of CustomUserBean as per our implementation.
		 */
		return CustomUserBean.createInstance(user);
	}

}
