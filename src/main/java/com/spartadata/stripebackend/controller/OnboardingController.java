package com.spartadata.stripebackend.controller;


import com.spartadata.stripebackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {
    @Autowired
    private AccountService accountService;

//    @GetMapping("/stripeReturn")
//    public ResponseEntity<Map<String, String>> stripeReturn() {
////            @RequestParam String userId,
////            @RequestParam String token)  {
//        return accountService.stripeReturn("userId", "token");
//    }
//
//    @GetMapping("/stripeRefresh")
//    public ResponseEntity<Map<String, String>> stripeRefresh() {
////            @RequestParam String userId,
////            @RequestParam String token)  {
//        return accountService.stripeRefresh("userId", "token");
//    }
}
