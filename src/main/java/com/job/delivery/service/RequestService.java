package com.job.delivery.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RequestService {
    Object addRequest(Map<String, String> requestDetails);

}
