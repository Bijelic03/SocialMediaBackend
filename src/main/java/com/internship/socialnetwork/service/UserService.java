package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.UserDto;
import com.internship.socialnetwork.model.User;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    List<UserDto> searchUsers(String searchParam);

    UserDto getUser(Long id);

    User getUserModel(String username);

    User getUserModel(Long id);

    UserDto getUser(String username);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long id);

    UserDto updateUser(Long id, UserDto userDto);

}