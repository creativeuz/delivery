package com.job.delivery.service;

import com.job.delivery.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface UserService {
    ResponseEntity<?> signup(SignUpRequest signUpRequest);
}
