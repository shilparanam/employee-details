package org.demo.employeedetails.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret:secret-key-which-should-be-changed}")
    private String secret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long expirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] decoded = Base64.getEncoder().encode(secret.getBytes());
        key = Keys.hmacShaKeyFor(decoded);
    }

    public String generateToken(String username, List<String> roles) {
        long now = System.currentTimeMillis();
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date issuedAt = new Date(now);
        Date exp = new Date(now + expirationMs);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
