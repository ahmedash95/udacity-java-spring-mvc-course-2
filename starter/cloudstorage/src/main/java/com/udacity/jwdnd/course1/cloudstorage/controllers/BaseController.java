package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.UserNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {
    UserService userService;

    public BaseController(UserService userService) {
        this.userService = userService;
    }

    protected User getUser() throws UserNotFoundException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUser(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException(username);
        }

        return user.get();
    }
}
