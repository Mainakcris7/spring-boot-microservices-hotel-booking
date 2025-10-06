package com.practice.authservice.exception;

import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleFieldValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Field Validation Error");
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errorResponse.put(err.getField(), err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Map<String, String>> handleJwtValidateException(SignatureException ex){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.UNAUTHORIZED.toString());
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Something went wrong");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
