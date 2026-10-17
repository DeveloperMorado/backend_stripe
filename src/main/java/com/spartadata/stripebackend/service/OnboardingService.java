package com.spartadata.stripebackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface OnboardingService {

    ResponseEntity<Map<String, String>> stripeReturn(@RequestParam String token);
    ResponseEntity<Map<String, String>> stripeRefresh();
}
