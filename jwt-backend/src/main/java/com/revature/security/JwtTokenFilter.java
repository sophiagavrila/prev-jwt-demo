package com.revature.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.revature.service.UserDetailsServiceImpl;

/**
 * The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter class
 * which guarantees a single execution per requestdispatch.
 * 
 * For any incoming request this Filter class gets executed. It checks if the
 * request has a valid JWT token. If it has a valid JWT Token then it sets the
 * Authentication in the context, to specify that the current user is
 * authenticated.
 */
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	/**
	 * In the overridden doFilterInternal() method, the token is extracted from the
	 * request header and validated. 
	 * 
	 * If token is validated then its gets the username from it and use it to get UserDetails. 
	 * From these user details and its authorities create an Authentication object.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = getTokenFromRequest(request);
			// System.out.println("Token-- " + token);
			
			if (token != null && jwtTokenUtil.validateJwtToken(token)) {
				String username = jwtTokenUtil.getUserNameFromJwtToken(token);
				// System.out.println("User Name--JwtTokenFilter-- " + username);
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				// System.out.println("Authorities--JwtTokenFilter-- " +
				
				// userDetails.getAuthorities());
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			// logger.error("Cannot set user authentication: {}", e);
			throw new RuntimeException("Cannot set user authentication" + e.getMessage());
		}
		filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			// remove "Bearer "
			return token.substring(7, token.length());
		}
		return null;
	}
}
