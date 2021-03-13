package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.CredentialNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.UserNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialsController extends BaseController {
    CredentialsService credentialsService;

    public CredentialsController(UserService userService, CredentialsService credentialsService) {
        super(userService);
        this.credentialsService = credentialsService;
    }

    @PostMapping("/credentials")
    public String save(@ModelAttribute Credential credential) throws UserNotFoundException {
        credentialsService.save(credential, getUser());

        return "redirect:/home?credentials";
    }


    @PostMapping("/credentials/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute Credential credential) throws UserNotFoundException, CredentialNotFoundException {
        credential.setCredentialid(id);
        credentialsService.update(credential, getUser());

        return "redirect:/home?credentials";
    }

    @DeleteMapping("/credentials/{id}")
    public String delete(@PathVariable("id") Long id) throws UserNotFoundException, CredentialNotFoundException {
        credentialsService.delete(id, getUser());

        return "redirect:/home?credentials";
    }
}
