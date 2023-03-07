package com.job.delivery.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RequestService {
    ResponseEntity<?> addRequest(Map<String, String> requestDetails);

}
