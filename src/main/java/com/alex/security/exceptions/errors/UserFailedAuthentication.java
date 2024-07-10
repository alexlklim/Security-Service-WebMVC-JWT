package com.alex.security.exceptions.errors;

public class UserFailedAuthentication extends RuntimeException{

    public UserFailedAuthentication( final String message) {
        super(message);
    }

}
