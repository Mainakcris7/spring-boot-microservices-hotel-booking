package com.practice.apigateway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, String>> handleAuthException(AuthException ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", ex.getMessage());
        errorMap.put("statusCode", ex.getStatusCode().toString());

        return ResponseEntity.status(ex.getStatusCode()).body(errorMap);
    }

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<Map<String, String>> handleWebClientException(WebClientException ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(errorMap);
    }
}
