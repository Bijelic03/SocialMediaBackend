package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.config.AppConfig;
import com.internship.socialnetwork.dto.FriendshipDto;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Friendship;
import com.internship.socialnetwork.repository.FriendshipRepository;
import com.internship.socialnetwork.service.FriendshipService;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.internship.socialnetwork.dto.FriendshipDto.convertToDto;

import static com.internship.socialnetwork.model.FriendshipStatus.ACCEPTED;
import static com.internship.socialnetwork.model.FriendshipStatus.PENDING;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private static final String FRIENDSHIP_EXISTS_MSG  = "Friendship with ids: %s , %s already exists!";

    private static final String FRIENDSHIP_DOES_NOT_EXIST_MSG  = "Friendship with ids: %s , %s does not exist!";

    private final FriendshipRepository friendshipRepository;

    private final UserService userService;

    private final AppConfig appConfig;

    @Override
    public List<FriendshipDto> getFriends(Long userId) {
        return friendshipRepository.findAllFriends(userId)
                .stream()
                .map(FriendshipDto::convertToDto)
                .toList();
    }

    @Override
    public List<FriendshipDto> getFriendRequests(Long userId, Boolean sent) {
        List<Friendship> friendRequests = sent != null && sent ?
                friendshipRepository.findAllSentFriendRequests(userId) :
                friendshipRepository.findAllFriendRequests(userId);

        return friendRequests
                .stream().map(FriendshipDto::convertToDto)
                .toList();
    }

    @Override
    public FriendshipDto createFriendRequest(Long userId, Long friendId) {
        friendshipRepository.findFriendship(userId, friendId)
                .ifPresent(friendship -> {
                    throw new BadRequestException(String.format(FRIENDSHIP_EXISTS_MSG, userId, friendId));
                });
        checkNumberOfFriends(userId);

        return convertToDto(friendshipRepository.save(
                Friendship.builder()
                        .user(userService.getUserModel(userId))
                        .friend(userService.getUserModel(friendId))
                        .friendshipStatus(PENDING)
                        .build()));
    }

    @Override
    public void deleteFriendship(Long userId, Long friendId) {
        friendshipRepository.delete(findFriendship(userId, friendId));
    }

    @Override
    public FriendshipDto acceptFriendRequest(Long userId, Long friendId) {
        checkNumberOfFriends(userId);

        Friendship friendship = findFriendship(userId, friendId);
        friendship.setFriendshipStatus(ACCEPTED);

        return convertToDto(friendshipRepository.save(friendship));
    }

    private Friendship findFriendship(Long userId, Long friendId) {
        return friendshipRepository.findFriendship(userId, friendId)
                .orElseThrow(() -> new NotFoundException(String.format(FRIENDSHIP_DOES_NOT_EXIST_MSG,
                        userId, friendId)));
    }

    private void checkNumberOfFriends(Long userId) {
        if (friendshipRepository.countNumberOfFriends(userId) >= appConfig.getMaxNumberOfFriends()) {
            throw new BadRequestException("Maximum number of friends exceeded!");
        }
    }

}