package com.udacity.jwdnd.course1.cloudstorage.models;

public class Credential {
    private Long credentialid;
    private String url;
    private String username;
    private String key;
    private String password;
    private Long userId;
    private String decryptedPassword;

    public Credential(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Long getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(Long credentialid) {
        this.credentialid = credentialid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }
}
