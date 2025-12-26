package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.TweetCreateRequest;
import com.workintech.twitterapi.dto.TweetUpdateRequest;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.service.TweetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity<Tweet> createTweet(@Valid @RequestBody TweetCreateRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Tweet created = tweetService.createTweet(req.getContent(), username);
        return ResponseEntity.ok(created);
    }

    // GET /tweet/findById?id=7
    @GetMapping("/findById")
    public ResponseEntity<Tweet> findById(@RequestParam Long id) {
        return ResponseEntity.ok(tweetService.findById(id));
    }

    // GET /tweet/findByUserId?userId=1
    @GetMapping("/findByUserId")
    public ResponseEntity<List<Tweet>> findByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(tweetService.findByUserId(userId));
    }

    // PUT /tweet/8
    @PutMapping("/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable Long id,
                                             @Valid @RequestBody TweetUpdateRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(tweetService.updateTweet(id, req.getContent(), username));
    }

    // DELETE /tweet/8
    // Sadece tweet sahibi silebilmelidir.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        tweetService.deleteTweet(id, username); // TweetService içinde bu metodu ekleyeceğiz
        return ResponseEntity.noContent().build(); // 204
    }
}
