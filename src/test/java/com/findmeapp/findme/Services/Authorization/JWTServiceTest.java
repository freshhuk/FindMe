package com.findmeapp.findme.Services.Authorization;


import com.findmeapp.findme.Models.Entities.User;
import com.findmeapp.findme.Models.Enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    @InjectMocks
    private  JWTService service;
    private Key secretKey;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = Decoders.BASE64.decode("YmFzZTY0RW5jb2RlZFJhbmRvbVNlY3JldEtleTEyMzQ1Ng==");
        secretKey = Keys.hmacShaKeyFor(keyBytes);

    }
    @Test
    void extractUserNameTest(){
        String expectedUserName = "testUser";
        String token = Jwts.builder()
                .setSubject(expectedUserName)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(secretKey)
                .compact();

        String actualUserName = service.extractUserName(token);
        Assertions.assertEquals(expectedUserName, actualUserName);
    }

    @Test
    void generateTokenTest(){

        User user = User.builder()
                .id(1)
                .username("Test")
                .email("test@gmail.com")
                .password("12345678")
                .role(Role.USER)
                .build();

        String token = service.generateToken(user);

        Assertions.assertNotNull(token);

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Assertions.assertEquals(user.getUsername(), claims.getSubject());
        Assertions.assertEquals(user.getId(), claims.get("id"));
        Assertions.assertEquals(user.getEmail(), claims.get("email"));
    }


    @Test
    void isTokenValidTest(){

        String token = Jwts.builder()
                .setSubject("Test")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(secretKey)
                .compact();

        User user = User.builder()
                .id(1)
                .username("Test")
                .email("test@gmail.com")
                .password("12345678")
                .role(Role.USER)
                .build();

        var result = service.isTokenValid(token, user);

        Assertions.assertTrue(result);
    }
}
