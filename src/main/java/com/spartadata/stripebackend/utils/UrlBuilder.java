package com.spartadata.stripebackend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class UrlBuilder {

    @Value("${app.base-url}")
    private String baseUrl;

    public String build(ApiEndpoint endpoint, Map<String, String> pathVariables, Map<String, String> queryParams) {
        // Replace path variables
        String path = endpoint.getPath();
        if (pathVariables != null) {
            for (Map.Entry<String, String> entry : pathVariables.entrySet()) {
                path = path.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        // Build query string
        String queryString = "";
        if (queryParams != null && !queryParams.isEmpty()) {
            StringJoiner joiner = new StringJoiner("&");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
                String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
                joiner.add(key + "=" + value);
            }
            queryString = "?" + joiner.toString();
        }

        return baseUrl + path + queryString;
    }

    public String build(ApiEndpoint endpoint) {
        return build(endpoint, null, null);
    }

    public String build(ApiEndpoint endpoint, Map<String, String> pathVariables) {
        return build(endpoint, pathVariables, null);
    }
}

//TODO Use these later
//String url = urlBuilder.build(
//        ApiEndpoint.GET_USER,
//        Map.of("id", "123"),
//        Map.of("include", "profile", "verbose", "true")
//);
//  System.out.println(url);