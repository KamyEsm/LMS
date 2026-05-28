package com.kamyesm.filemanagementservice.ExceptionHandeling;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
