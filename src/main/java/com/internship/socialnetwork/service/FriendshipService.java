package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.FriendshipDto;

import java.util.List;

public interface FriendshipService {

    List<FriendshipDto> getFriends(Long userId);

    List<FriendshipDto> getFriendRequests(Long userId, Boolean sent);

    FriendshipDto createFriendRequest(Long userId, Long friendId);

    void deleteFriendship(Long userId, Long friendId);

    FriendshipDto acceptFriendRequest(Long userId, Long friendId);

}