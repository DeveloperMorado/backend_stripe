package com.spartadata.stripebackend.controller;

import com.spartadata.stripebackend.model.AccountDTO;
import com.spartadata.stripebackend.model.CustomerResponseDTO;
import com.spartadata.stripebackend.service.AccountService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/hello")
    public String sayHello() {
        return accountService.sayHello();
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDTO> create(
            @RequestBody AccountDTO accountDTO) throws StripeException {

        Customer customer = accountService.create(accountDTO);

        CustomerResponseDTO response = new CustomerResponseDTO(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/createConnectedAccount")
    public ResponseEntity<Map<String, String>>  createConnectedAccount(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String userId,
            @RequestParam String email) throws StripeException {

        return accountService.createConnectedAccount(authorizationHeader, userId, email);
    }

    @GetMapping("/onboarding/returnStripe")
    public ResponseEntity<Map<String, String>> returnStripe(
            @RequestParam String token,
            @RequestParam String userId) {
        return accountService.returnStripe(userId, token);
    }

    @GetMapping("/onboarding/refreshStripe")
    public ResponseEntity<Map<String, String>> refreshStripe(
            @RequestParam String token,
            @RequestParam String userId) {
        return accountService.refreshStripe(userId, token);
    }
}
