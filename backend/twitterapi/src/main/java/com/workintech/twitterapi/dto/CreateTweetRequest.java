package com.workintech.twitterapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTweetRequest {

    @NotBlank(message = "content cannot be blank")
    @Size(max = 280, message = "content cannot exceed 280 chars")
    private String content;

    @NotNull(message = "userId boş değer olamaz")
    private Long userId;

    public CreateTweetRequest() {}

    public CreateTweetRequest(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
