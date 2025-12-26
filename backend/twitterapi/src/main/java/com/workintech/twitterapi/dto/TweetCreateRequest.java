package com.workintech.twitterapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TweetCreateRequest {

    @NotBlank(message = "content boş olamaz")
    @Size(max = 280, message = "content en fazla 280 karakter olabilir")
    private String content;

    public TweetCreateRequest() {}

    public TweetCreateRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
