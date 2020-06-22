package com.ram.ppmtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ram.ppmtool.entity.User;
import com.ram.ppmtool.exception.UsernameAlreadyExistsException;
import com.ram.ppmtool.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		try {
			
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			
			newUser.setUsername(newUser.getUsername());
			
			
			return userRepository.save(newUser);
			
		} catch (Exception e) {
			throw new  UsernameAlreadyExistsException("Username "+newUser.getUsername()+" already exists");
		}
		
		
	}
}
