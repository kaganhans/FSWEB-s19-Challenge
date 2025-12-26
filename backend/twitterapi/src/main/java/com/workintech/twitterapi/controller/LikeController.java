package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // POST /like?tweetId=123
    @PostMapping
    public ResponseEntity<Void> like(@RequestParam Long tweetId) {
        likeService.like(tweetId);
        return ResponseEntity.ok().build();
    }

    // DELETE /like?tweetId=123
    @DeleteMapping
    public ResponseEntity<Void> dislike(@RequestParam Long tweetId) {
        likeService.dislike(tweetId);
        return ResponseEntity.noContent().build();
    }
}
