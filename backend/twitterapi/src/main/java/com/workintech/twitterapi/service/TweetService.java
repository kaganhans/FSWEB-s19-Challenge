package com.workintech.twitterapi.service;

import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.repository.TweetRepository;
import com.workintech.twitterapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public Tweet createTweet(String content, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User bulunamadı: " + username));

        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user);

        return tweetRepository.save(tweet);
    }

    public Tweet findById(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet bulunamadı: " + id));
    }

    public List<Tweet> findByUserId(Long userId) {
        return tweetRepository.findByUserId(userId);
        // Eğer yukarıdaki çalışmazsa repository'de şu methodu açıp bunu kullan:
        // return tweetRepository.findByUser_Id(userId);
    }

    public Tweet updateTweet(Long tweetId, String newContent, String username) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet bulunamadı: " + tweetId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User bulunamadı: " + username));

        // sahiplik kontrolü (güvenlik)
        if (tweet.getUser() == null || tweet.getUser().getId() == null ||
                !tweet.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu tweet'i güncelleme yetkiniz yok.");
        }

        tweet.setContent(newContent);
        return tweetRepository.save(tweet);
    }

    // ✅ DELETE için eklendi
    public void deleteTweet(Long tweetId, String username) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet bulunamadı: " + tweetId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User bulunamadı: " + username));

        // sahiplik kontrolü: sadece tweet sahibi silebilir
        if (tweet.getUser() == null || tweet.getUser().getId() == null ||
                !tweet.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu tweet'i silme yetkiniz yok.");
        }

        tweetRepository.delete(tweet);
        // Alternatif: tweetRepository.deleteById(tweetId);
    }
}
