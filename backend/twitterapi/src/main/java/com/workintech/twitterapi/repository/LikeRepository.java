package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUser_IdAndTweet_Id(Long userId, Long tweetId);

    Optional<Like> findByUser_IdAndTweet_Id(Long userId, Long tweetId);
}
