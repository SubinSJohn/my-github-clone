package com.subin.github_clone.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.subin.github_clone.models.User;
import com.subin.github_clone.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Optional<User> getUserById(Long id) {
		
		return userRepository.findById(id);
	}

	public User updateUser(Long id, User updatedUser) {
	    return userRepository.findById(id).map(user -> {
	        user.setUsername(updatedUser.getUsername());  
	        user.setPassword(updatedUser.getPassword());  
	        user.setRole(updatedUser.getRole());          
	        return userRepository.save(user);
	    }).orElse(null);
	}

	
}
