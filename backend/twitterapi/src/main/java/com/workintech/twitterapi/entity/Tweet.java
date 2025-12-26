package com.workintech.twitterapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tweets")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "content cannot be blank")
    @Size(max = 280, message = "content cannot exceed 280 chars")
    @Column(nullable = false, length = 280)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ Retweet desteği: Bu tweet bir retweet ise, orijinal tweet burada tutulur.
    // Normal tweet'lerde null olur.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_tweet_id")
    private Tweet originalTweet;

    public Tweet() {}

    public Tweet(Long id, String content, User user) {
        this.id = id;
        this.content = content;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Tweet getOriginalTweet() { return originalTweet; }
    public void setOriginalTweet(Tweet originalTweet) { this.originalTweet = originalTweet; }
}
