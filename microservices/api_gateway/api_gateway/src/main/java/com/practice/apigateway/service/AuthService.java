package com.practice.apigateway.service;

import com.practice.apigateway.dto.JwtDto;
import com.practice.apigateway.dto.JwtValidationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final WebClient webClient;

    AuthService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    public Mono<JwtValidationDto> validateToken(JwtDto jwtDto){
        return webClient
                .post()
                .uri("http://auth-service/auth/validate")
                .bodyValue(jwtDto)
                .retrieve()
                .bodyToMono(JwtValidationDto.class);
    }
}
