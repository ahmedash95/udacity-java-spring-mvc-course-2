package com.udacity.jwdnd.course1.cloudstorage.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationExceptions(ApplicationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("error", e.getMessage());
        return "redirect:/error";
    }
}
