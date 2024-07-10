package com.alex.security.exceptions.errors;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException( final String message) {
        super(message);
    }
}
