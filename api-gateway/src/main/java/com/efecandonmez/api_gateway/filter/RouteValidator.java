package com.efecandonmez.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
    public class RouteValidator {

        public static final List<String> openApiEndpoints = List.of(
                "/api/v1/auth/register",
                "/api/v1/auth/login",
                "/eureka",
                "/swagger-ui",
                "/swagger-ui.html",
                "/v3/api-docs",
                "/api/v1/auth/v3/api-docs",
                "/api/v1/accounts/v3/api-docs",
                "/api/v1/transaction/v3/api-docs",
                "/webjars"
        );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));



}


