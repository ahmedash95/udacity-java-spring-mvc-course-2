package com.udacity.jwdnd.course1.cloudstorage.exceptions;

public class NoteNotFoundException extends ApplicationException {
    public NoteNotFoundException() {
        super("Note not found");
    }
}
