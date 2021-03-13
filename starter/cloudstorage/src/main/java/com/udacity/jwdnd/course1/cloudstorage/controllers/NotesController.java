package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoteNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.UserNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NotesController extends BaseController {
    NotesService notesService;

    public NotesController(NotesService notesService, UserService userService) {
        super(userService);
        this.notesService = notesService;
    }

    @PostMapping("/notes")
    public String store(Model model, @ModelAttribute Note note, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        notesService.insert(note, getUser());
        model.addAttribute("note", note);

        redirectAttributes.addFlashAttribute("message", "Note has been stored successfully!")
                .addFlashAttribute("message_type", "success");

        return "redirect:/home?notes";
    }

    @DeleteMapping("/notes/{id}")
    public String delete(@PathVariable("id") Long noteId, RedirectAttributes redirectAttributes) throws UserNotFoundException, NoteNotFoundException {
        notesService.delete(noteId, getUser());

        redirectAttributes.addFlashAttribute("message", "Note has been deleted!")
                .addFlashAttribute("message_type", "success");

        return "redirect:/home?notes";
    }
}
