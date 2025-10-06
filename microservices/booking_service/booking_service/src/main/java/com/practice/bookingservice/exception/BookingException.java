package com.practice.bookingservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BookingException extends RuntimeException{
    private HttpStatus statusCode;
    public BookingException(String errorMessage, HttpStatus statusCode){
        super(errorMessage);
        this.statusCode = statusCode;
    }
}
