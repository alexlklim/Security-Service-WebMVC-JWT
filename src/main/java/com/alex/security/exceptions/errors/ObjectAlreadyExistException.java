package com.alex.security.exceptions.errors;

public class ObjectAlreadyExistException extends RuntimeException{


    public ObjectAlreadyExistException( final String message) {
        super(message);
    }
}
