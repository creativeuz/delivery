package com.job.delivery.service;

import com.job.delivery.entity.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void signup(SignUpRequest signUpRequest);
}
