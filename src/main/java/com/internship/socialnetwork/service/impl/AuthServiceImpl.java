package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.AuthenticationRequestDto;
import com.internship.socialnetwork.dto.AuthenticationResponseDto;
import com.internship.socialnetwork.dto.RegisterRequestDto;
import com.internship.socialnetwork.dto.UserDto;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.UnauthorizedException;
import com.internship.socialnetwork.model.Role;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.AuthService;
import com.internship.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtServiceImpl jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    @Override
    public UserDto register(RegisterRequestDto request) {
        userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail())
                .ifPresent(user -> {
                    throw new BadRequestException("User already exists in the system");
                });
        User user = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()).toCharArray())
                .role(Role.USER)
                .build();

        return UserDto.convertToDto(userRepository.save(user));
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        String username = request.getUsername();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return AuthenticationResponseDto.builder()
                .accessToken(jwtService.generateToken(userDetails))
                .refreshToken(jwtService.generateRefreshToken(userDetails))
                .build();
    }

    @Override
    public AuthenticationResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = authHeader.substring(7);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(refreshToken));

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            return AuthenticationResponseDto.builder()
                    .accessToken(jwtService.generateToken(userDetails))
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UnauthorizedException("Token is not valid");
        }
    }

    @Override
    public boolean isAuthorized(Long userId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername().equals(userService.getUserModel(userId).getUsername());
    }

}