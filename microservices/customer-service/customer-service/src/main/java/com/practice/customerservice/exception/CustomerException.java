package com.practice.customerservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomerException extends RuntimeException{
    private final HttpStatus statusCode;
    public CustomerException(String message, HttpStatus statusCode){
        super(message);
        this.statusCode = statusCode;
    }
}
