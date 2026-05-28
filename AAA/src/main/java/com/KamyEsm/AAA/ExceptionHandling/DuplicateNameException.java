package com.KamyEsm.AAA.ExceptionHandling;

public class DuplicateNameException extends RuntimeException {
    public DuplicateNameException(String message) {
        super(message);
    }
}
