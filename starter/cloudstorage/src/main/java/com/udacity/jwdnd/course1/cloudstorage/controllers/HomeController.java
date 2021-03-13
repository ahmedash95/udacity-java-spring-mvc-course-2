package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.UserNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    UserService userService;
    FileService fileService;
    NotesService notesService;

    public HomeController(UserService userService, FileService fileService, NotesService notesService) {
        this.userService = userService;
        this.fileService = fileService;
        this.notesService = notesService;
    }

    @GetMapping("/home")
    public String homeView(Model model) throws UserNotFoundException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUser(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException(username);
        }

        model.addAttribute("files", fileService.getByUserId(user.get().getUserId()));
        model.addAttribute("notes", notesService.getByUserId(user.get().getUserId()));

        return "home";
    }
}
