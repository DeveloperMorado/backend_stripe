package com.spartadata.stripebackend.service;

import com.spartadata.stripebackend.model.AccountDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface AccountService {
    public String sayHello();
    public Customer create(AccountDTO accountDTO) throws StripeException;
    public ResponseEntity<Map<String, String>> createConnectedAccount(
            @RequestHeader("Authorization") String authorizationHeader,
            String userId,
            String email) throws StripeException;
    public ResponseEntity<Map<String, String>> refreshStripe(String userId, String token);
    public ResponseEntity<Map<String, String>> returnStripe(String userId, String token);


//    public ResponseEntity<Map<String, String>> createAccountLink() throws StripeException;
}
