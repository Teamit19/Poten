package com.example.poten.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CommentException extends RuntimeException {
    public CommentException(String message) {
        super(message);
    }
}
