package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    @Autowired
    private CredentialsMapper mapper;
    @Autowired
    private EncryptionService encryptionService;

    public void createCredential(Credential credential) {
        this.mapper.insert(this.updateKeyAndPassword(credential));
    }

    public void updateCredential(Credential credential) {
        this.mapper.updateCredential(this.updateKeyAndPassword(credential));
    }

    public void deleteCredential(int credentialId) {
        this.mapper.deleteCredential(credentialId);
    }

    public List<Credential> getUserCredentials(int userId) {
        List<Credential> credentialList = this.mapper.getCredentials(userId);
        return credentialList.stream().map(this::buildCredential).collect(Collectors.toList());
    }

    private Credential updateKeyAndPassword(Credential credential) {
        String key = encryptionService.createKey();
        credential.setKey(key);
        credential.setPassword(encryptionService.encryptValue(credential.getDecodedPassword(), key));
        return credential;
    }

    private Credential buildCredential(Credential credential) {
        return Credential.builder()
                .credentialId(credential.getCredentialId())
                .url(credential.getUrl())
                .username(credential.getUsername())
                .password(credential.getPassword())
                .userId(credential.getUserId())
                .decodedPassword(getDecodedPassword(credential))
                .build();
    }

    private String getDecodedPassword(Credential credential) {
        return this.encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

    public Credential getCredentialById(int credentialId) {
        return buildCredential(this.mapper.getCredentialById(credentialId));
    }
}
