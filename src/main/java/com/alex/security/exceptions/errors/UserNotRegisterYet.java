package com.alex.security.exceptions.errors;

public class UserNotRegisterYet extends RuntimeException{

    public UserNotRegisterYet( final String message) {
        super(message);
    }
}
