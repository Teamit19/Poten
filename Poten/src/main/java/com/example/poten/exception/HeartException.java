package com.example.poten.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class HeartException extends RuntimeException {
    public HeartException(String message) {
        super(message);
    }
}