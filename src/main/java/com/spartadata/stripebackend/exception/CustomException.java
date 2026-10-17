package com.spartadata.stripebackend.exception;

import com.stripe.exception.StripeException;

public class CustomException extends StripeException {
    public CustomException(String message, String requestId, String code, Integer statusCode) {
        super(message, requestId, code, statusCode);
    }
}
