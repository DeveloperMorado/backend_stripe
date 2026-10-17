package com.spartadata.stripebackend.service;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;

public class PaymentService {
    public PaymentService(@Value("${stripe.secret-key}") String secretKey) {
        Stripe.apiKey = secretKey;
    }

//    public PaymentIntent createPaymentIntent(Long amount) throws StripeException {
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(amount)
//                .setCurrency("usd")
//                .build();
//        return PaymentIntent.create(params);
//    }

}
