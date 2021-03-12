package com.udacity.jwdnd.course1.cloudstorage.exceptions;

public class FileExistsException extends Exception {
    public FileExistsException(String name) {
        super(String.format("File [%s] already uploaded!", name));
    }
}
