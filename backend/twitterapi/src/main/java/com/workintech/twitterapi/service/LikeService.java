package com.workintech.twitterapi.service;

import com.workintech.twitterapi.entity.Like;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.repository.LikeRepository;
import com.workintech.twitterapi.repository.TweetRepository;
import com.workintech.twitterapi.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository,
                       TweetRepository tweetRepository,
                       UserRepository userRepository) {
        this.likeRepository = likeRepository;
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

    public void like(Long tweetId) {
        User current = getCurrentUser();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new IllegalArgumentException("Tweet not found"));

        if (likeRepository.existsByUser_IdAndTweet_Id(current.getId(), tweetId)) {
            throw new IllegalArgumentException("Already liked");
        }

        Like like = new Like();
        like.setUser(current);
        like.setTweet(tweet);

        likeRepository.save(like);
    }

    public void dislike(Long tweetId) {
        User current = getCurrentUser();

        likeRepository.findByUser_IdAndTweet_Id(current.getId(), tweetId)
                .ifPresent(likeRepository::delete);
    }
}
