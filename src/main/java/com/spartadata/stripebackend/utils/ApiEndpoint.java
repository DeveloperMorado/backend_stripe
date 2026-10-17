package com.spartadata.stripebackend.utils;

import lombok.Getter;

@Getter
public enum ApiEndpoint {
    GET_USER_ACCOUNT("/api/account/{id}"),
    STRIPE_RETURN("/api/account/return"),
    STRIPE_REFRESH("/api/account/refresh");

    private final String path;

    ApiEndpoint(String path) {
        this.path = path;
    }

}
