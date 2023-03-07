package com.job.delivery.serviceImplementation;

import com.job.delivery.config.MyUserDetailsService;
import com.job.delivery.entity.SignUpRequest;
import com.job.delivery.repository.*;
import com.job.delivery.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final MyUserDetailsService myUserDetailsService;

    public UserServiceImpl(UserRepository userRepository, MyUserDetailsService myUserDetailsService) {
        this.userRepository = userRepository;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    public ResponseEntity<?> signup(SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        myUserDetailsService.signUp(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }
}

