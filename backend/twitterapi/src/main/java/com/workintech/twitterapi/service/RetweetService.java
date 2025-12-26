package com.workintech.twitterapi.service;

import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.repository.TweetRepository;
import com.workintech.twitterapi.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RetweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public RetweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new IllegalArgumentException("Unauthorized");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Tweet retweet(Long tweetId) {
        User current = getCurrentUser();

        Tweet original = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new IllegalArgumentException("Tweet not found"));

        Tweet rt = new Tweet();
        rt.setUser(current);
        rt.setOriginalTweet(original);

        // content NOT NULL ise boş string kullan
        rt.setContent("");

        return tweetRepository.save(rt);
    }

    public void deleteRetweet(Long retweetId) {
        User current = getCurrentUser();

        Tweet rt = tweetRepository.findById(retweetId)
                .orElseThrow(() -> new IllegalArgumentException("Retweet not found"));

        if (rt.getOriginalTweet() == null) {
            throw new IllegalArgumentException("This tweet is not a retweet");
        }

        if (!rt.getUser().getId().equals(current.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this retweet");
        }

        tweetRepository.delete(rt);
    }
}
