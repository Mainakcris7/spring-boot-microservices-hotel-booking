package com.practice.hotelservice.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class HotelException extends RuntimeException{
    private HttpStatus statusCode;
    public HotelException(String errorMessage, HttpStatus statusCode){
        super(errorMessage);
        this.statusCode = statusCode;
    }
}
