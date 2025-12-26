package com.workintech.twitterapi.dto;

import jakarta.validation.constraints.NotBlank;

public class TweetUpdateRequest {

    @NotBlank
    private String content;

    public TweetUpdateRequest() {}

    public TweetUpdateRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
