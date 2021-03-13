package com.udacity.jwdnd.course1.cloudstorage.exceptions;

public class CredentialNotFoundException extends Exception {
    public CredentialNotFoundException() {
        super("Credential not found!");
    }
}
