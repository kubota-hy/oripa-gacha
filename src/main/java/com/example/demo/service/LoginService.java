package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	
	private UserRepository userRepository;
	
	public User Login(String email,String password) {
		return userRepository.findByEmailAndPassword(email, password)
				.orElseThrow(() -> new RuntimeException("メールアドレスまたはパスワードが違います"));
	}

}
