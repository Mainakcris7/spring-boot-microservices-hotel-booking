package com.practice.apigateway.fillter;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouteFilter {
    private final List<String> openEndPoints = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/validate"
    );

    private final List<String> adminEndPoints = List.of(
            "/hotels",
            "/rooms"
    );

    private final Set<HttpMethod> adminRequestMethods = Set.of(
      HttpMethod.PUT,
      HttpMethod.POST,
      HttpMethod.DELETE
    );

    Predicate<ServerHttpRequest> isSecured = (req) ->
            openEndPoints
                    .stream()
                    .noneMatch(uri -> req.getURI().getPath().contains(uri));

    Predicate<ServerHttpRequest> isAdminEndPoint = (req) ->
            adminEndPoints
                    .stream()
                    .anyMatch(uri -> req.getURI().getPath().contains(uri));

    Predicate<ServerHttpRequest> isAdminRequestMethod = (req) ->{
            return adminRequestMethods.contains(req.getMethod());
    };
}
