package com.internship.socialnetwork.service.impl;

import static com.internship.socialnetwork.dto.UserDto.convertToDto;
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

    private static final String NOT_FOUND_EXCEPTION_MSG = "User with id %s not found!";

    private final User user = createUser(1L, "username");

    private final User otherUser = createUser(2L, "otherUsername");

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldGetUser_whenGetUser_ifUserExists() {
        // Given: Mocking new user
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When: Getting the user
        UserDto userDto = userService.getUser(user.getId());

        // Then: Validate userDto
        assertNotNull(userDto);
        assertEquals(user.getUsername(), userDto.getUsername());

        // Verify that userRepository.findById(...) was called exactly once with userId1
        verify(userRepository, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetUser_ifUserDoesNotExist() {
        // Given: Mocking the scenario where the user does not exist
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.empty());

        // When: Getting user
        NotFoundException exception = assertThrows(NotFoundException.class,() -> userService.getUser(otherUser.getId()));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals(String.format(NOT_FOUND_EXCEPTION_MSG, otherUser.getId()), exception.getMessage());

        // Verify that userRepository.findById(...) was called exactly once with userId2
        verify(userRepository, times(1)).findById(otherUser.getId());
        verifyNoMoreInteractions(userRepository);
    }
    @Test
    void shouldGetMatchingUsers_whenSearchUsers_ifUsersExist() {
        // Given: Mocking list of users
        when(userRepository.findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(user));

        // When: Searching for users
        List<UserDto> userDtos = userService.searchUsers(user.getUsername());

        // Then: Verify that correct number of users is returned
        assertEquals(1, userDtos.size());

        // Verify that userRepository.findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(...)
        // was called exactly once
        verify(userRepository).findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(anyString(), anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldGetAllUsers_whenGetAllUsers_ifUsersExist() {
        // Given: Mocking list of users
        when(userRepository.findAll()).thenReturn(List.of(user, otherUser));

        // When: Getting all users
        List<UserDto> userDtos = userService.getAllUsers();

        // Then: Verify that correct number of users is returned
        assertEquals(2, userDtos.size());

        // Verify that userRepository.findAll() was called exactly once
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldCreateUser_whenCreateUser_ifUserDoesNotExist() {
        // Given: Mocking the scenario where the user does not exist
        UserDto userDto = convertToDto(user);

        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());


        when(userRepository.save(any(User.class))).thenReturn(user);

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
    void shouldThrowBadRequestException_whenCreateUser_ifUserExists() {
        // Given: Mocking existing user
        UserDto existingUserDto = convertToDto(otherUser);

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
    void shouldThrowBadRequestException_whenCreateUser_ifUsernameIsNull() {
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
    void shouldGetUserModel_whenGetUserModel_ifUserExists() {
        // Given: Mocking the scenario where the user exists
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When: Getting the user model
        User resultUser = userService.getUserModel(user.getId());

        // Then: Verify that the correct user model is returned
        assertNotNull(resultUser);
        assertEquals(user.getUsername(), resultUser.getUsername());

        // Verify that userRepository.findById(...) was called exactly once with userId1
        verify(userRepository, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetUserByModel_ifUserDoesNotExist() {
        // Given: Mocking the scenario where the user does not exist
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.empty());

        // When: Getting the user model
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserModel(otherUser.getId()));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals(String.format(NOT_FOUND_EXCEPTION_MSG, otherUser.getId()), exception.getMessage());

        // Verify that userRepository.findById(...) was called exactly once with userId2
        verify(userRepository, times(1)).findById(otherUser.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldDeleteUser_whenDeleteUser_ifUserExists() {
        // Given: Mocking the scenario where the user exists
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When: Deleting the user
        userService.deleteUser(user.getId());

        // Then: Verify that the user is deleted
        assertEquals(0, userService.getAllUsers().size());

        // Verify that userRepository.deleteById(...) was called exactly once with the user ID
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteUser_ifUserDoesNotExist() {
        // Given: Mocking the scenario where the user does not exist
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // When: Deleting the user
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteUser(user.getId()));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals(String.format(NOT_FOUND_EXCEPTION_MSG, user.getId()), exception.getMessage());

        // Verify that userRepository.deleteById(...) was not called
        verify(userRepository, never()).deleteById(any());
    }
    
    private User createUser(Long id, String username) {
        return User.builder()
                .id(id)
                .username(username)
                .name("Alice")
                .surname("Johnson")
                .email("user@email.com")
                .build();
    }

}