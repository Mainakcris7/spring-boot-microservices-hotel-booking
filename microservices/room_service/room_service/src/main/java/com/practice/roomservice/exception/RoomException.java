package com.practice.roomservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RoomException extends RuntimeException{
    private HttpStatus statusCode;
    public RoomException(String errorMessage, HttpStatus statusCode){
        super(errorMessage);
        this.statusCode = statusCode;
    }
}
