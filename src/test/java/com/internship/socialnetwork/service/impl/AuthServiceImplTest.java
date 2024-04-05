package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.AuthenticationRequestDto;
import com.internship.socialnetwork.dto.AuthenticationResponseDto;
import com.internship.socialnetwork.dto.RegisterRequestDto;
import com.internship.socialnetwork.dto.UserDto;
import com.internship.socialnetwork.exception.UnauthorizedException;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private static final String JWT_TOKEN = "jwtToken";

    private static final String REFRESH_TOKEN = "refreshToken";

    private static final String AUTH_HEADER = "Bearer " + REFRESH_TOKEN;

    private static final String USERNAME = "user";

    private static final String PASSWORD = "password123";

    private static final String NAME = "name";

    private static final String SURNAME = "surname";

    private static final String EMAIL = "test@example.com";

    private final User user = createUser(1L, USERNAME);

    private final UserDetails userDetails = createUserDetails(user);

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authenticationService;

    @Test
    void shouldCreateUser_whenRegister_ifRequestExists() {
        // Given: Mocking registerRequestDto
        RegisterRequestDto request = RegisterRequestDto.builder()
                .username("user")
                .name("Test")
                .surname("User")
                .email("test@example.com")
                .password("password123")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Mock password encoder behavior
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When: Registering user
        UserDto userDto = authenticationService.register(request);

        // Verify that the returned UserDto is correct
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(NAME, userDto.getName());
        assertEquals(SURNAME, userDto.getSurname());
        assertEquals(EMAIL, userDto.getEmail());

        // Verify that passwordEncoder.encode() is called once
        verify(passwordEncoder).encode(anyString());

        // Verify that userRepository.save() is called once
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldAuthenticateUser_whenAuthenticate_ifUserExists() {
        // Given: Mocking authentication request
        AuthenticationRequestDto request = AuthenticationRequestDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        UserDetails userDetails = createUserDetails(user);

        // Mock user details service behavior
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        // Mock jwt service behavior
        when(jwtService.generateToken(userDetails)).thenReturn(JWT_TOKEN);
        when(jwtService.generateRefreshToken(userDetails)).thenReturn(REFRESH_TOKEN);

        // When: Authenticating user
        AuthenticationResponseDto responseDto = authenticationService.authenticate(request);

        // Then: Verify that the returned AuthenticationResponseDto is correct
        assertEquals(JWT_TOKEN, responseDto.getAccessToken());
        assertEquals(REFRESH_TOKEN, responseDto.getRefreshToken());

        // Verify that authenticationManager.authenticate() is called once
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Verify that userDetailsService.loadUserByUsername() is called once
        verify(userDetailsService).loadUserByUsername(user.getUsername());

        // Verify that jwtService.generateToken() is called once
        verify(jwtService).generateToken(userDetails);

        // Verify that jwtService.generateRefreshToken() is called once
        verify(jwtService).generateRefreshToken(userDetails);
    }

    @Test
    void shouldThrowUnauthorizedException_whenRefreshToken_ifRefreshTokenIsValid() {
        // Given: Mocking HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mocking Authorization header
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(AUTH_HEADER);

        // Mocking username extraction
        when(jwtService.extractUsername(REFRESH_TOKEN)).thenReturn(USERNAME);

        // Mocking UserDetails
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);

        // Mocking token validation
        when(jwtService.isTokenValid(REFRESH_TOKEN, userDetails)).thenReturn(true);

        // Mocking token generation
        String accessToken = "fakeAccessToken";
        when(jwtService.generateToken(userDetails)).thenReturn(accessToken);

        // When: Refreshing token
        AuthenticationResponseDto responseDto = authenticationService.refreshToken(request, response);

        // Then: Verify that the returned AuthenticationResponseDto is correct
        assertEquals(accessToken, responseDto.getAccessToken());
        assertEquals(REFRESH_TOKEN, responseDto.getRefreshToken());

        // Verify that jwtService.extractUsername() is called once
        verify(jwtService).extractUsername(REFRESH_TOKEN);

        // Verify that userDetailsService.loadUserByUsername() is called once
        verify(userDetailsService).loadUserByUsername(USERNAME);

        // Verify that jwtService.isTokenValid() is called once
        verify(jwtService).isTokenValid(REFRESH_TOKEN, userDetails);

        // Verify that jwtService.generateToken() is called once
        verify(jwtService).generateToken(userDetails);

        // Verify that jwtService.generateToken() is called once
        verify(request).getHeader(anyString());
    }

    @Test
    void shouldThrowUnauthorizedException_whenRefreshToken_ifRefreshTokenIsInvalid() {
        // Given: Mocking HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mocking Authorization header
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(AUTH_HEADER);

        // Mocking username extraction
        when(jwtService.extractUsername(REFRESH_TOKEN)).thenReturn(user.getUsername());

        // Mocking UserDetails
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(userDetails);

        // Mocking token validation
        when(jwtService.isTokenValid(REFRESH_TOKEN, userDetails)).thenReturn(false);

        // When: Calling refreshToken
        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> authenticationService.refreshToken(request, response));

        // Then: Verify that UnauthorizedException is thrown
        assertEquals("Token is not valid", exception.getMessage());

        // Verify that jwtService.extractUsername() is called once
        verify(jwtService).extractUsername(REFRESH_TOKEN);

        // Verify that userDetailsService.loadUserByUsername() is called once
        verify(userDetailsService).loadUserByUsername(anyString());

        // Verify that jwtService.isTokenValid() is called once
        verify(jwtService).isTokenValid(REFRESH_TOKEN, userDetails);
    }

    @Test
    void shouldReturnTrue_whenIsAuthorized_ifUserIsAuthorized() throws UnauthorizedException {
        // Given: Mocking SecurityContextHolder and UserService
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mocking user model
        when(userService.getUserModel(1L)).thenReturn(user);

        // When: Checking if user is authorized
        boolean isAuthorized = authenticationService.isAuthorized(1L);

        // Then: Verify that the result is true
        assertTrue(isAuthorized);

        // Verify that securityContext.getAuthentication() is called once
        verify(securityContext).getAuthentication();

        // Verify that userService.getUserModel() is called once
        verify(userService).getUserModel(anyLong());
    }

    @Test
    void shouldThrowUnauthorizedException_whenIsAuthorized_ifUserIsNotAuthorized() {
        // Given: Mocking SecurityContextHolder and UserService
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Mocking userDetails and authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mocking user
        when(userService.getUserModel(anyLong())).thenReturn(createUser(2L, "otherUser"));

        // When: Calling isAuthorized
        boolean isAuthorized = authenticationService.isAuthorized(1L);

        // Then: Verify that the result is false
        assertFalse(authenticationService.isAuthorized(1L));

        // Verify that securityContext.getAuthentication() is called once
        verify(securityContext, times(2)).getAuthentication();

        // Verify that userService.getUserModel() is called once
        verify(userService, times(2)).getUserModel(anyLong());
    }

    private User createUser(Long id, String username) {
        return User.builder()
                .id(id)
                .username(username)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .build();
    }

    private UserDetails createUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                PASSWORD,
                new ArrayList<>());
    }

}