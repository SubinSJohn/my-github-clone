package com.subin.github_clone.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.expiration}")
    private long expiration;
    
    public String generateToken(String username) {
    	Key key = Keys.hmacShaKeyFor(secret.getBytes());
         
        return Jwts.builder()
        		.subject(username)
    			.issuedAt(new Date())
    			.expiration(new Date(System.currentTimeMillis() + expiration))
        		.signWith(key)
                .compact();
        		
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
 }
