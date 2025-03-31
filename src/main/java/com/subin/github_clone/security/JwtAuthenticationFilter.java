package com.subin.github_clone.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
	
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
    	this.jwtUtil = jwtUtil;
    	this.userDetailsService = userDetailsService;
    }
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse respond, FilterChain filterChain) throws ServletException, IOException {
		
		String token = extractToken(request);
		if (token != null && jwtUtil.validateToken(token)) {	
			 String username = jwtUtil.getUsernameFromToken(token);  // Retrieves the username from the token payload
			 UserDetails userDetails = userDetailsService.loadUserByUsername(username); //Fetches user details from the database.
			 
			 //Spring Security uses an Authentication object to track the logged-in user
			 //So we are creating a authenticated user session
			 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
			 
			 //Spring Security uses SecurityContext to keep track of the current logged-in user.
			 //This makes sure the user is recognized for the rest of the request
			 SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		// Continue with the filter chain
		filterChain.doFilter(request, respond);
	}
	
	private String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer "))
			return authHeader.substring(7);
		else
			return null;
	}
}
