package com.KamyEsm.AAA.ExceptionHandling;

public class JwtGenerationException extends RuntimeException {
    public JwtGenerationException(String message) {
        super(message);
    }
}
