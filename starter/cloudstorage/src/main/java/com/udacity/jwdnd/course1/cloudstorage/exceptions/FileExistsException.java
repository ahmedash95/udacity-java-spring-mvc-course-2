package com.udacity.jwdnd.course1.cloudstorage.exceptions;

public class FileExistsException extends ApplicationException {
    public FileExistsException(String name) {
        super(String.format("File [%s] already uploaded!", name));
    }
}
