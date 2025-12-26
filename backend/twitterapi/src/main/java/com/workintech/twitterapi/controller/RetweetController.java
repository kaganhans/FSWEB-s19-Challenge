package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.service.RetweetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final RetweetService retweetService;

    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    // POST /retweet?tweetId=123
    @PostMapping
    public ResponseEntity<Tweet> retweet(@RequestParam Long tweetId) {
        return ResponseEntity.ok(retweetService.retweet(tweetId));
    }

    // DELETE /retweet/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        retweetService.deleteRetweet(id);
        return ResponseEntity.noContent().build();
    }
}
