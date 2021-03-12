package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.FileExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.UserNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FilesController extends BaseController {

    FileService fileService;

    public FilesController(FileService fileService, UserService userService) {
        super(userService);
        this.fileService = fileService;
    }

    @GetMapping("/files")
    public List<File> showFiles() throws UserNotFoundException {
        return fileService.getAll(getUser().getUserId());
    }

    @PostMapping("/files")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, RedirectAttributes redirectAttributes) throws UserNotFoundException, IOException {
        try {
            fileService.storeFile(file, getUser());
            redirectAttributes
                    .addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename());
        } catch (IOException | FileExistsException ex) {
            redirectAttributes
                    .addFlashAttribute("message", "File upload failed, an error occurred: " + ex.getMessage());
        }

        return "redirect:/home?file";
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> showFile(@PathVariable("id") Long fileId) throws FileNotFoundException, UserNotFoundException {
        File file = fileService.getFileById(fileId, getUser());

        byte[] media = file.getFileData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getContentType()));

        return new ResponseEntity<>(media, headers, HttpStatus.OK);
    }

    @DeleteMapping("/files/{id}")
    public String deleteFile(@PathVariable("id") Long fileId, RedirectAttributes redirectAttributes) throws FileNotFoundException, UserNotFoundException {
        File file = fileService.removeFileById(fileId, getUser());

        redirectAttributes
                .addFlashAttribute("message", String.format("Format [%s] has been removed!", file.getFilename()));

        return "redirect:/home?file";
    }
}
