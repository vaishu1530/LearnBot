package com.example.learnbot.service;
	
	import com.example.learnbot.model.User;
	import com.example.learnbot.repository.UserRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.security.core.userdetails.UserDetails;
	import org.springframework.security.core.userdetails.UserDetailsService;
	import org.springframework.security.core.userdetails.UsernameNotFoundException;
	import org.springframework.stereotype.Service;

	import java.util.Collections;

	@Service
	public class CustomUserDetailsService implements UserDetailsService {

	    @Autowired
	    private UserRepository userRepository;

	    @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

	        // 1️⃣ Fetch user from DB by email
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

	        // 2️⃣ Build Spring Security UserDetails object
	        return org.springframework.security.core.userdetails.User
	                .withUsername(user.getEmail())
	                .password(user.getPassword())
	                .authorities(Collections.emptyList())
	                .build();

	    }
	}



