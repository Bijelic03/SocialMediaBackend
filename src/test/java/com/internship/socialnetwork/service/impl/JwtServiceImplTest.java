package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    private static final String SECRET_KEY = "15a090c039afc96c84380bd1a04f7fa62615d965aa1b6928260243ee33bb68d2";

    private static final Long EXPIRATION = 86400000L;

    private final UserDetails userDetails = createUserDetails();

    @Mock
    private AppConfig appConfig;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Test
    void shouldExtractUsername_whenExtractUsername_ifSubjectExists() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(EXPIRATION);

        // Generating token
        String token = jwtService.generateToken(userDetails);

        // When: Extracting username
        String username = jwtService.extractUsername(token);

        // Then: Verify that username is extracted correctly
        assertEquals(userDetails.getUsername(), username);

        // Verify that AppConfig.getJwtSecretKey() is called twice
        verify(appConfig, times(2)).getJwtSecretKey();

        // Verify that AppConfig.getJwtExpiration() is called once
        verify(appConfig).getJwtExpiration();
    }

    @Test
    void shouldExtractClaim_whenExtractClaim_ifTokenIsValid() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(EXPIRATION);

        // Generating token
        String token = jwtService.generateToken(userDetails);

        // When: Extracting claim
        String subject = jwtService.extractClaim(token, Claims::getSubject);

        // Then: Verify that subject is extracted correctly
        assertEquals(userDetails.getUsername(), subject);

        // Verify that AppConfig.getJwtSecretKey() is called twice
        verify(appConfig, times(2)).getJwtSecretKey();

        // Verify that AppConfig.getJwtExpiration() is called once
        verify(appConfig).getJwtExpiration();
    }

    @Test
    void shouldGenerateToken_whenGenerateToken_ifUserDetailsProvided() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(EXPIRATION);

        // When: Generating token
        String token = jwtService.generateToken(userDetails);

        // Then: Verify that token is not null
        assertNotNull(token);

        // Verify that AppConfig.getJwtSecretKey() is called twice
        verify(appConfig).getJwtSecretKey();

        // Verify that AppConfig.getJwtExpiration() is called once
        verify(appConfig).getJwtExpiration();
    }

    @Test
    void shouldReturnTrue_whenIsTokenValid_ifTokenIsValid() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(EXPIRATION);

        // Generating token
        String token = jwtService.generateToken(userDetails);

        // When: Validating token
        boolean isTokenValid = jwtService.isTokenValid(token, userDetails);

        // Then: Verify that token is valid
        assertTrue(isTokenValid);

        // Verify that AppConfig.getJwtSecretKey() is called twice
        verify(appConfig, times(3)).getJwtSecretKey();

        // Verify that AppConfig.getJwtExpiration() is called once
        verify(appConfig).getJwtExpiration();
    }

    @Test
    void shouldReturnFalse_whenIsTokenValid_ifTokenIsNotValid() {
        // Given: Mocking secret key
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);

        // Generating invalid token
        String token = Jwts.builder()
                .setSubject("nonExistingUser")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)))
                .compact();

        // When: Validating token
        boolean isTokenValid = jwtService.isTokenValid(token, userDetails);

        // Then: Verify that token is not valid
        assertFalse(isTokenValid);

        // Verify that AppConfig.getJwtSecretKey() is called twice
        verify(appConfig).getJwtSecretKey();
    }

    @Test
    void shouldReturnFalse_whenIsTokenValid_ifTokenIsExpired() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(0L);

        // Generating expired token
        String token = jwtService.generateToken(userDetails);

        // When: Calling refreshToken
        // Then: Verify that token is expired
        ExpiredJwtException exception = assertThrows(ExpiredJwtException.class,
                () -> jwtService.isTokenValid(token, userDetails));

        // Verify that AppConfig.getJwtSecretKey() is called twice
        verify(appConfig, times(2)).getJwtSecretKey();

        // Verify that AppConfig.getJwtExpiration() is called once
        verify(appConfig).getJwtExpiration();
    }

    private UserDetails createUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                "username",
                "password123",
                new ArrayList<>());
    }

}