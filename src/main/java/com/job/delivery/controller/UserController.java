package com.job.delivery.controller;

import com.job.delivery.entity.SignUpRequest;
import com.job.delivery.exception.UserException;
import com.job.delivery.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/userApi")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            userService.signup(signUpRequest);
            return ResponseEntity.ok().body("User registered successfully!");
        } catch (UserException u) {
            return ResponseEntity.badRequest().body(u);
        }
    }
}

