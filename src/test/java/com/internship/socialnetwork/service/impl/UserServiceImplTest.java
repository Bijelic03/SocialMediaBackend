package com.internship.socialnetwork.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.internship.socialnetwork.dto.UserDto;

import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final Long userId1 = 1L;

    private final Long userId2 = 2L;

    private final String username1 = "user1";

    private final String username2 = "user2";

    private final String name1 = "John";

    private final String name2 = "Alice";

    private final String surname1 = "Doe";

    private final String surname2 = "Johnson";

    private final String email = "test@email.com";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldGetUser() {
        // Given: Mocking new user
        User user = new User();
        user.setId(userId1);
        user.setUsername(username1);

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));

        // When: Getting the user
        UserDto userDto = userService.getUser(userId1);

        // Then: Validate userDto
        assertNotNull(userDto);
        assertEquals(username1, userDto.getUsername());

        // Verify that userRepository.findById(...) was called exactly once with userId1
        verify(userRepository, times(1)).findById(userId1);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetUser_ifUserDoesNotExist() {
        // Given: Mocking the scenario where the user does not exist
        when(userRepository.findById(userId2)).thenReturn(Optional.empty());

        // When: Getting user
        NotFoundException exception = assertThrows(NotFoundException.class,() -> userService.getUser(userId2));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals("User with id: 2 not found!", exception.getMessage());

        // Verify that userRepository.findById(...) was called exactly once with userId2
        verify(userRepository, times(1)).findById(userId2);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldGetAllUsers() {
        // Given: Mocking list of users
        List<User> users = new ArrayList<>();
        users.add(User.builder().username(username1).build());
        users.add(User.builder().username(username2).build());

        when(userRepository.findAll()).thenReturn(users);

        // When: Getting all users
        List<UserDto> userDtos = userService.getAllUsers();

        // Then: Verify that correct number of users is returned
        assertEquals(2, userDtos.size());

        // Verify that userRepository.findAll() was called exactly once
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldCreateUser() {
        // Given: Mocking the scenario where the user does not exist
        UserDto userDto = UserDto.builder()
                .username(username1)
                .name(name1)
                .surname(surname1)
                .email(email)
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        User savedUser = User.builder()
                .username(username1)
                .name(name1)
                .surname(surname1)
                .email(email)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When: Creating the user
        UserDto createdUserDto = userService.createUser(userDto);

        // Then: Verify that userDto is created correctly
        assertEquals(userDto.getUsername(), createdUserDto.getUsername());
        assertEquals(userDto.getName(), createdUserDto.getName());
        assertEquals(userDto.getSurname(), createdUserDto.getSurname());
        assertEquals(userDto.getEmail(), createdUserDto.getEmail());

        // Verify that userRepository.findByUsernameOrEmail(...) was called exactly once with anyString() arguments
        verify(userRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());

        // Verify that userRepository.save(...) was called exactly once with any(User.class) argument
        verify(userRepository, times(1)).save(any(User.class));

        // Verify that there are no more interactions with userRepository
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenUserAlreadyExists() {
        // Given: Mocking existing user
        UserDto existingUserDto = UserDto.builder()
                .username(username1)
                .name(name1)
                .surname(surname1)
                .email(email)
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(existingUserDto.convertToModel()));

        // When: User is created with existing username
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.createUser(existingUserDto));

        // Then: Verify that BadRequestException is thrown with appropriate message
        assertEquals("User already exists in the system", exception.getMessage());

        // Verify that userRepository.findByUsernameOrEmail(...) was called exactly once with anyString() arguments
        verify(userRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());

        // Verify that userRepository.save(...) was never called
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowBadRequestException_whenUsernameIsNull() {
        // Given: Creating a UserDto with null username
        UserDto userDto = new UserDto();
        userDto.setUsername(null);

        // When: Invoking createUser method with UserDto having null username
        // Then: Verify that NullPointerException is thrown
        NullPointerException exception = assertThrows(NullPointerException.class, () -> userService.createUser(userDto));

        // Verify that userRepository.findByUsernameOrEmail(...) was not called
        verify(userRepository, never()).findByUsernameOrEmail(anyString(), anyString());
    }

    @Test
    void shouldGetUserModel() {
        // Given: Mocking the scenario where the user exists
        User user = new User();
        user.setId(userId1);
        user.setUsername(username1);
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));

        // When: Getting the user model
        User resultUser = userService.getUserModel(userId1);

        // Then: Verify that the correct user model is returned
        assertNotNull(resultUser);
        assertEquals(username1, resultUser.getUsername());

        // Verify that userRepository.findById(...) was called exactly once with userId1
        verify(userRepository, times(1)).findById(userId1);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetUserByModel_ifUserDoesNotExist() {
        // Given: Mocking the scenario where the user does not exist
        when(userRepository.findById(userId2)).thenReturn(Optional.empty());

        // When: Getting the user model
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserModel(userId2));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals("User with id: 2 not found!", exception.getMessage());

        // Verify that userRepository.findById(...) was called exactly once with userId2
        verify(userRepository, times(1)).findById(userId2);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldDeleteUser() {
        // Given: Mocking the scenario where the user exists
        User user = User.builder().id(userId1).build();
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));

        // When: Deleting the user
        userService.deleteUser(userId1);

        // Then: Verify that the user is deleted
        assertEquals(0, userService.getAllUsers().size());

        // Verify that userRepository.deleteById(...) was called exactly once with the user ID
        verify(userRepository, times(1)).deleteById(userId1);
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteUser_ifUserDoesNotExist() {
        // Given: Mocking the scenario where the user does not exist
        when(userRepository.findById(userId1)).thenReturn(Optional.empty());

        // When: Deleting the user
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteUser(userId1));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals("User with id: 1 not found!", exception.getMessage());

        // Verify that userRepository.deleteById(...) was not called
        verify(userRepository, never()).deleteById(any());
    }

}