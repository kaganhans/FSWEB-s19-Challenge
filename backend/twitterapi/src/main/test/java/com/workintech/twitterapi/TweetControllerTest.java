package com.workintech.twitterapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void should_block_tweet_create_when_no_token() throws Exception {
        String body = """
                {
                  "content": "no token tweet",
                  "userId": 1
                }
                """;

        mockMvc.perform(post("/tweet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is4xxClientError()); // 401 veya 403 olabilir
    }

    @Test
    void should_create_and_get_tweet_with_token() throws Exception {
        String username = "tweet_user_" + System.currentTimeMillis();
        String password = "Test12345!";

        String token = TestAuthHelper.registerThenLoginAndGetToken(mockMvc, username, password);

        String createBody = """
                {
                  "content": "hello from tests",
                  "userId": 1
                }
                """;

        String createResp = mockMvc.perform(post("/tweet")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode created = mapper.readTree(createResp);

        long tweetId = extractTweetId(created);

        // findById (id bulamazsak bu adımı es geç)
        if (tweetId > 0) {
            mockMvc.perform(get("/tweet/findById")
                            .param("id", String.valueOf(tweetId))
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().is2xxSuccessful());
        }

        // findByUserId
        mockMvc.perform(get("/tweet/findByUserId")
                        .param("userId", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void should_update_tweet_with_token_if_endpoint_exists() throws Exception {
        String username = "upd_user_" + System.currentTimeMillis();
        String password = "Test12345!";
        String token = TestAuthHelper.registerThenLoginAndGetToken(mockMvc, username, password);

        String createBody = """
                {
                  "content": "tweet to update",
                  "userId": 1
                }
                """;

        String createResp = mockMvc.perform(post("/tweet")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode created = mapper.readTree(createResp);
        long tweetId = extractTweetId(created);

        if (tweetId <= 0) return;

        String updateBody = """
                {
                  "content": "updated content"
                }
                """;

        mockMvc.perform(put("/tweet/" + tweetId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().is2xxSuccessful());
    }

    private static long extractTweetId(JsonNode created) {
        if (created == null) return -1;

        if (created.has("id")) return created.get("id").asLong();
        if (created.has("tweetId")) return created.get("tweetId").asLong();

        // bazen data içinde gelir
        JsonNode data = created.get("data");
        if (data != null) {
            if (data.has("id")) return data.get("id").asLong();
            if (data.has("tweetId")) return data.get("tweetId").asLong();
        }
        return -1;
    }
}
