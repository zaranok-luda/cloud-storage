package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credential {
    private int credentialId;
    private String url;
    private String username;
    private String password;
    private String decodedPassword;
    private String key;
    private int userId;

    public Credential() {
    }

    public Credential(int credentialId, String url, String username, String password, String decodedPassword, String key, int userId) {
        this.credentialId = credentialId;
        this.url = url;
        this.username = username;
        this.password = password;
        this.decodedPassword = decodedPassword;
        this.key = key;
        this.userId = userId;
    }

    public int getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(int credentialId) {
        this.credentialId = credentialId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDecodedPassword() {
        return decodedPassword;
    }

    public void setDecodedPassword(String decodedPassword) {
        this.decodedPassword = decodedPassword;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static CredentialBuilder builder() {
        return new CredentialBuilder();
    }

    public static class CredentialBuilder {
        private int credentialId;
        private String url;
        private String username;
        private String password;
        private String decodedPassword;
        private String key;
        private int userId;

        public CredentialBuilder credentialId(int credentialId) {
            this.credentialId = credentialId;
            return this;
        }

        public CredentialBuilder url(String url) {
            this.url = url;
            return this;
        }

        public CredentialBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CredentialBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CredentialBuilder decodedPassword(String decodedPassword) {
            this.decodedPassword = decodedPassword;
            return this;
        }

        public CredentialBuilder key(String key) {
            this.key = key;
            return this;
        }

        public CredentialBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public Credential build() {
            return new Credential(credentialId, url, username, password, decodedPassword, key, userId);
        }
    }
}