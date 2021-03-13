package com.udacity.jwdnd.course1.cloudstorage.exceptions;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String name) {
        super(String.format("User %s not found!", name));
    }
}
