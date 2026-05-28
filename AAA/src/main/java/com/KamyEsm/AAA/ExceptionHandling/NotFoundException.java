package com.KamyEsm.AAA.ExceptionHandling;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
