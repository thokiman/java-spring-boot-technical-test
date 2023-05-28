package com.springboot.demo.exception;

public class DemoNotFoundException extends RuntimeException {


    public DemoNotFoundException(String message) {
        super(message);
    }

    public DemoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DemoNotFoundException(Throwable cause) {
        super(cause);
    }
}
