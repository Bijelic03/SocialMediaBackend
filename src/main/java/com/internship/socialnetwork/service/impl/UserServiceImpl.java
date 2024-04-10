package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.UserDto;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.internship.socialnetwork.dto.UserDto.convertToDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::convertToDto)
                .toList();
    }

    @Override
    public List<UserDto> searchUsers(String searchParam) {
        return userRepository.findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(searchParam, searchParam)
                .stream()
                .map(UserDto::convertToDto)
                .toList();
    }

    @Override
    public User getUserModel(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found!", id)));
    }

    @Override
    public User getUserModel(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username %s not found!", username)));
    }

    @Override
    public UserDto getUser(Long id) {
        return convertToDto(getUserModel((id)));
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        // TODO: Hash password

        userRepository.findByUsernameOrEmail(userDto.getUsername(), userDto.getEmail())
                .ifPresent(user -> {
                    throw new BadRequestException("User already exists in the system");
                });
        return convertToDto(userRepository.save(userDto.convertToModel()));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = getUserModel(id);
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        return convertToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(getUserModel(id));
    }

}