package com.springboot.demo.exception;

public class MissingRequestBodyException extends RuntimeException {
    public MissingRequestBodyException(String message) {
        super(message);
    }
}