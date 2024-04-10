package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.AuthenticationRequestDto;
import com.internship.socialnetwork.dto.AuthenticationResponseDto;
import com.internship.socialnetwork.dto.RegisterRequestDto;
import com.internship.socialnetwork.dto.UserDto;
import com.internship.socialnetwork.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.status(CREATED).body(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(service.refreshToken(request, response));
    }

}