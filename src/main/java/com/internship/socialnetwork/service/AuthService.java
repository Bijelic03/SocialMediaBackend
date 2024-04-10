package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.AuthenticationRequestDto;
import com.internship.socialnetwork.dto.AuthenticationResponseDto;
import com.internship.socialnetwork.dto.RegisterRequestDto;
import com.internship.socialnetwork.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;

public interface AuthService {

    UserDto register(RegisterRequestDto request);

    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);

    AuthenticationResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response);

    boolean isAuthorized(Long userId) throws BadRequestException;

}