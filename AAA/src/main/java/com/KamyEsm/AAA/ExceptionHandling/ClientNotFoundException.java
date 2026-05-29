package com.KamyEsm.AAA.ExceptionHandling;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
