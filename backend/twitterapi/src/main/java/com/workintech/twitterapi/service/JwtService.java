package com.workintech.twitterapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // application.properties:
    // jwt.secret=THIS_IS_A_DEMO_SECRET_CHANGE_IT_TO_A_LONG_RANDOM_STRING_1234567890
    @Value("${jwt.secret}")
    private String jwtSecret;

    // application.properties:
    // jwt.expirationMs=86400000
    @Value("${jwt.expirationMs:86400000}")
    private long expirationMs;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // --- helpers ---

    private boolean isTokenExpired(String token) {
        Date exp = extractClaim(token, Claims::getExpiration);
        return exp.before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        // ✅ Base64 decode yok → direkt string bytes
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        // ✅ Güvenlik: HS256 için en az 32 byte önerilir.
        // Senin secret uzun görünüyor, sorun yok.
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
