package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.CredentialNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService {

    CredentialsMapper credentialsMapper;
    EncryptionService encryptionService;


    public CredentialsService(EncryptionService encryptionService, CredentialsMapper credentialsMapper) {
        this.encryptionService = encryptionService;
        this.credentialsMapper = credentialsMapper;
    }

    public List<Credential> getByUserId(Long userId) {
        List<Credential> credentials = credentialsMapper.findAllByUserId(userId);
        return credentials.stream().map(credential -> {
            String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            credential.setDecryptedPassword(decryptedPassword);
            return credential;
        }).collect(Collectors.toList());
    }

    public Credential save(Credential credential, User user) {
        credential.setUserId(user.getUserId());
        updatePasswordEncryption(credential);
        credentialsMapper.save(credential);

        return credential;
    }

    public Credential update(Credential credential, User user) throws CredentialNotFoundException {
        Optional<Credential> c = credentialsMapper.findById(credential.getCredentialid());
        if(c.isEmpty() || c.get().getUserId() != user.getUserId()) {
            throw new CredentialNotFoundException();
        }

        updatePasswordEncryption(credential);
        credentialsMapper.update(credential, credential.getCredentialid());

        return credential;
    }

    public void delete(Long credentialId, User user) throws CredentialNotFoundException {
        Optional<Credential> credential = credentialsMapper.findById(credentialId);
        if(credential.isEmpty() || credential.get().getUserId() != user.getUserId()) {
            throw new CredentialNotFoundException();
        }

        credentialsMapper.delete(credential.get().getCredentialid());
    }

    private void updatePasswordEncryption(Credential entity) {
        String encodedKey = encryptionService.generateEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(entity.getPassword(), encodedKey);
        entity.setKey(encodedKey);
        entity.setPassword(encryptedPassword);
    }

    public void deleteAll() {
        credentialsMapper.deleteAll();
    }
}
