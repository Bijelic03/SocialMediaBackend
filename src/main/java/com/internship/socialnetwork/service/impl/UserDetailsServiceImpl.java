package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return createUserDetails(userService.getUserModel(username));
    }

    private UserDetails createUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), String.valueOf(user.getPassword()),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }

}