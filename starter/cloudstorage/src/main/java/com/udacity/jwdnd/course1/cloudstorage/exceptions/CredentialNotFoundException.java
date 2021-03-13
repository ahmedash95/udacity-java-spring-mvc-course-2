package com.udacity.jwdnd.course1.cloudstorage.exceptions;

public class CredentialNotFoundException extends ApplicationException {
    public CredentialNotFoundException() {
        super("Credential not found!");
    }
}
