package com.workintech.twitterapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "username cannot be blank")
    @Size(min = 3, max = 30, message = "username must be between 3 and 30")
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 100, message = "password must be at least 8 chars")
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
