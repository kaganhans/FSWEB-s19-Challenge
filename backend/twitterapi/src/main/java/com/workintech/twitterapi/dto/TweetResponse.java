package com.workintech.twitterapi.dto;

public class TweetResponse {
    private Long id;
    private String content;
    private Long userId;
    private String username;

    public TweetResponse() {}

    public TweetResponse(Long id, String content, Long userId, String username) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
