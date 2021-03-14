package com.udacity.jwdnd.course1.cloudstorage.exceptions;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationExceptions(ApplicationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("error", e.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadExceptions(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
        redirectAttributes
                .addFlashAttribute("message", "File is too big. max size is: 2MB")
                .addFlashAttribute("message_type", "danger");
        return "redirect:/home?files";
    }
}
