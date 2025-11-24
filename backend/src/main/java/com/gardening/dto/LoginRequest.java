package com.gardening.dto;

public class LoginRequest {
    private String email;
    private String password;
    private String userType;
    private Boolean rememberMe = false;

    // Constructors
    public LoginRequest() {
    }

    public LoginRequest(String email, String password, String userType, Boolean rememberMe) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.rememberMe = rememberMe;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
