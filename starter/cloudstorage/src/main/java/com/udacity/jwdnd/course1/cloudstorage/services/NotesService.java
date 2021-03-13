package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoteNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class NotesService {
    NoteMapper noteMapper;

    public NotesService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getByUserId(Long userId) {
        List<Note> notes = noteMapper.findAllByUserId(userId);
        return notes;
    }

    public Note insert(Note note, User user) {
        note.setUserId(user.getUserId());
        noteMapper.save(note);

        return note;
    }

    public void delete(Long noteId, User user) throws NoteNotFoundException {
        Optional<Note> note = noteMapper.find(noteId);
        if(note.isEmpty() || note.get().getUserId() != user.getUserId()) {
            throw new NoteNotFoundException();
        }

        noteMapper.delete(noteId);
    }
}
