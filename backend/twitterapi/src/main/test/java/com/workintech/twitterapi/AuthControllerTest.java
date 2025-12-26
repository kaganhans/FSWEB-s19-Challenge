package com.workintech.twitterapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Register + Login -> JWT token dönmeli")
    void should_register_and_login_and_return_token() throws Exception {
        String username = "test_user_" + System.currentTimeMillis();
        String password = "Test12345!";

        String token = TestAuthHelper.registerThenLoginAndGetToken(mockMvc, username, password);

        assertThat(token)
                .as("Token null/blank olmamalı")
                .isNotNull()
                .isNotBlank();

        // JWT genelde 3 parçadan oluşur: header.payload.signature
        assertThat(token)
                .as("Token JWT formatında olmalı (xxx.yyy.zzz)")
                .matches("^[^.]+\\.[^.]+\\.[^.]+$");
    }
}
