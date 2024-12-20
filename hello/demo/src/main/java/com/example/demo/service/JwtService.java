package com.example.demo.service;


import io.jsonwebtoken.Jwts; 

import io.jsonwebtoken.io.Decoders; 
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;


import java.security.Key; 
import java.util.Date; 

  
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(User user) {
        return Jwts.builder()
                .claim("id", user.getUserId())
                .claim("username", user.getUsername())
                .claim("password", user.getPassword())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("role", user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 minutes
                .signWith(getSigningKey())
                .compact();
    }

    public User decodeToken(String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        User user = new User();
        user.setUserId(claims.get("id", Integer.class));
        user.setUsername(claims.get("username", String.class));
        user.setPassword(claims.get("password", String.class));
        user.setFirstName(claims.get("firstName", String.class));
        user.setLastName(claims.get("lastName", String.class));
        user.setRole(claims.get("role", String.class));

        System.out.println(user.toString());

        return user;
    }
    
    public Integer getIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Integer.class);
    }

    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
