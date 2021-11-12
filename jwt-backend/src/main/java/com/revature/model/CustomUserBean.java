package com.revature.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Spring Security has an interface
 * org.springframework.security.core.userdetails.UserDetails which provides core
 * user information.
 * 
 * You need to provide a concrete implemetation of this interface to add more
 * fields. There is also a concrete implementation
 * org.springframework.security.core.userdetails.User provided by Spring
 * security that can be used directly.
 * 
 * Here we are using our own implementation ConcreteUserBean.
 * 
 * @author SophieGavrila
 *
 */

public class CustomUserBean implements UserDetails {

	private static final long serialVersionUID = 5827461080370813100L;

	private int id;
	private String userName;
	private String email;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	CustomUserBean(int id, String userName, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	/**
	 * Returns returns authorities of type GrantedAuthority.
	 * 
	 * That list of GrantedAuthority objects is built using the instances of
	 * SimpleGrantedAuthority which is the basic concrete implementation of a
	 * GrantedAuthority.
	 * 
	 * Stores a String representation of an authority granted to the Authentication
	 * object.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public static CustomUserBean createInstance(User user) {
		// Here the List of authorities represents all of the Roles that the User has.
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());

		return new CustomUserBean(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), authorities);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	/**
	 * change true to false on all of these, they're just defaults for
	 * the overridden UserDetails interface. 
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	// rhs == "right hand side"...so whatever you're passing as param
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof CustomUserBean) {
			return userName.equals(((CustomUserBean) rhs).userName);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return userName.hashCode();
	}

}
