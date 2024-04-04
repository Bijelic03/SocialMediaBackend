package com.internship.socialnetwork.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Value("${max.friends.number}")
    private int maxNumberOfFriends;

}
