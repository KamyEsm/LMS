package com.KamyEsm.AAA.ExceptionHandling;

public class PasswordSecureException extends RuntimeException {
    public PasswordSecureException(String message) {
        super(message);
    }
}
