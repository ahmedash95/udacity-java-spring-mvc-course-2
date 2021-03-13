package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.FileExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.UserNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public File storeFile(MultipartFile file, User user) throws IOException, UserNotFoundException, FileExistsException {
        if(fileMapper.findByNameAndUser(file.getOriginalFilename(), user.getUserId()).isPresent()) {
            throw new FileExistsException(file.getOriginalFilename());
        }

        File model = new File();
        model.setFilename(file.getOriginalFilename());
        model.setContentType(file.getContentType());
        model.setFileSize(String.valueOf(file.getSize()));
        model.setFileData(file.getBytes());
        model.setUserId(user.getUserId());

        fileMapper.save(model);

        return model;
    }

    public List<File> getAll(Long userId) {
        return fileMapper.findAllByUserId(userId);
    }

    public List<File> getByUserId(Long userId) {
        return fileMapper.findAllByUserId(userId);
    }

    public File getFileById(Long fileId, User user) throws FileNotFoundException {
        Optional<File> file = fileMapper.findById(fileId);
        if(file.isEmpty()) {
            throw new FileNotFoundException();
        }

        if(file.get().getUserId() != user.getUserId()) {
            throw new FileNotFoundException();
        }

        return file.get();
    }

    public File removeFileById(Long fileId, User user) throws FileNotFoundException {
        File file = getFileById(fileId, user);
        fileMapper.delete((long) file.getFileId());

        return file;
    }
}
