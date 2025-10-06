package com.practice.apigateway.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AuthException extends RuntimeException{
    private HttpStatus statusCode;

    public AuthException(String message, HttpStatus statusCode){
        super(message);
        this.statusCode = statusCode;
    }
}
