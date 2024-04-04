package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.config.AppConfig;
import com.internship.socialnetwork.dto.FriendshipDto;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Friendship;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.FriendshipRepository;
import com.internship.socialnetwork.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.internship.socialnetwork.dto.FriendshipDto.convertToDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceImplTest {

    private static final String FRIENDSHIP_EXISTS_MSG  = "Friendship with ids: %s , %s already exists!";

    private static final String FRIENDSHIP_DOES_NOT_EXIST_MSG  = "Friendship with ids: %s , %s does not exist!";

    private static final String NUMBER_OF_FRIENDS_EXCEEDED_MSG  = "Maximum number of friends exceeded!";

    private final User user = createUser(1L, "username");

    private final Friendship friendship = createFriendship();

    private static final int maxNumberOfFriends = 50;

    @Mock
    private AppConfig appConfig;

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FriendshipServiceImpl friendshipService;

    @Test
    void shouldReturnFriends_whenGetFriends_ifPostsExist() {
        // Given: Mocking new friendship
        when(friendshipRepository.findAllFriends(user.getId()))
                .thenReturn(List.of(friendship));

        // When: Getting friends
        List<FriendshipDto> friends = friendshipService.getFriends(friendship.getUser().getId());

        // Then: Verify that correct number of friends is returned
        assertEquals(1, friends.size());

        // Verify that friendshipRepository.findAllFriends() was called exactly once
        verify(friendshipRepository, times(1)).findAllFriends(user.getId());

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldReturnFriendRequests_whenGetFriendRequests_ifRequestsExist() {
        // Given: Mocking new friendship
        when(friendshipRepository.findAllFriendRequests(user.getId()))
                .thenReturn(List.of(friendship));

        // When: Getting friend requests
        List<FriendshipDto> friendRequests = friendshipService.getFriendRequests(user.getId(), false);

        // Then: Verify that correct number of friend requests is returned
        assertEquals(1, friendRequests.size());

        // Verify that friendshipRepository.findAllFriendRequests() was called exactly once
        verify(friendshipRepository, times(1)).findAllFriendRequests(user.getId());

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldReturnSentFriendRequests_whenGetFriendRequests_ifSentRequestsExist() {
        // Given: Mocking new friendship
        when(friendshipRepository.findAllSentFriendRequests(user.getId()))
                .thenReturn(List.of(friendship));

        // When: Getting friend requests
        List<FriendshipDto> sentFriendRequests = friendshipService.getFriendRequests(user.getId(), true);

        // Then: Verify that correct number of sent friend requests is returned
        assertEquals(1, sentFriendRequests.size());

        // Verify that friendshipRepository.findAllSentFriendRequests() was called exactly once
        verify(friendshipRepository, times(1)).findAllSentFriendRequests(user.getId());

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldCreateFriendRequest_whenCreateFriendRequest_ifFriendshipDoesNotExist() {
        // Given: Mocking new friendshipDto
        FriendshipDto friendshipDto = convertToDto(friendship);

        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);
        when(userService.getUserModel(anyLong())).thenReturn(user);
        when(friendshipRepository.findFriendship(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(friendshipRepository.countNumberOfFriends(anyLong())).thenReturn(1);
        when(appConfig.getMaxNumberOfFriends()).thenReturn(maxNumberOfFriends);

        // When: Creating new friend request
        friendshipService.createFriendRequest(friendship.getUser().getId(), friendship.getFriend().getId());

        // Then: Verify that friendshipDto is created correctly
        assertEquals(friendshipDto.getUserId(), friendship.getUser().getId());
        assertEquals(friendshipDto.getFriendId(), friendship.getFriend().getId());
        assertEquals(friendshipDto.getFriendUsername(), friendship.getFriend().getUsername());
        assertEquals(friendshipDto.getUsername(), friendship.getUser().getUsername());

        // Verify that friendshipRepository.findFriendship(...) is called exactly once
        verify(friendshipRepository, times(1)).findFriendship(anyLong(), anyLong());

        // Verify that friendshipRepository.countNumberOfFriends(...) is called exactly once
        verify(friendshipRepository, times(1)).countNumberOfFriends(anyLong());

        // Verify that friendshipRepository.save(...) is called exactly once
        verify(friendshipRepository, times(1)).save(any(Friendship.class));

        // Verify that userService.getUserModel(...) is called exactly twice
        verify(userService, times(2)).getUserModel(anyLong());

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenCreateFriendRequest_ifFriendshipExists() {
        // Given: Mocking new friendshipDto
        when(friendshipRepository.findFriendship(anyLong(), anyLong()))
                .thenReturn(Optional.of(friendship));

        // When: Checking if friendship exists
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> friendshipService.createFriendRequest(friendship.getUser().getId(),
                        friendship.getFriend().getId()));

        // Then: Verify that BadRequestException is thrown with appropriate message
        assertEquals(String.format(FRIENDSHIP_EXISTS_MSG,
                friendship.getUser().getId(), friendship.getFriend().getId()), exception.getMessage());

        // Verify that friendshipRepository.findFriendship(...) is called exactly once
        verify(friendshipRepository, times(1)).findFriendship(anyLong(), anyLong());

        // Verify that friendshipRepository.save(...) was not called
        verify(friendshipRepository, times(0)).save(any(Friendship.class));

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenCreateFriendRequest_ifNumberOfFriendsExceeded() {
        // Given: Mocking the scenario where number of friends is exceeded
        when(friendshipRepository.findFriendship(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(friendshipRepository.countNumberOfFriends(anyLong())).thenReturn(maxNumberOfFriends);

        // When: Checking if number of friends is exceeded
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> friendshipService.createFriendRequest(friendship.getUser().getId(),
                        friendship.getFriend().getId()));

        // Then: Verify that BadRequestException is thrown with appropriate message
        assertEquals(NUMBER_OF_FRIENDS_EXCEEDED_MSG, exception.getMessage());

        // Verify that friendshipRepository.findFriendship(...) is called exactly once
        verify(friendshipRepository, times(1)).findFriendship(anyLong(), anyLong());

        // Verify that friendshipRepository.countNumberOfFriends(...) is called exactly once
        verify(friendshipRepository, times(1)).countNumberOfFriends(anyLong());

        // Verify that friendshipRepository.save(...) was not called
        verify(friendshipRepository, times(0)).save(any(Friendship.class));

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldDeleteFriendship_whenDeleteFriendship_ifFriendshipExists() {
        // Given: Mocking the scenario where friendship exists
        when(friendshipRepository.findFriendship(anyLong(), anyLong())).thenReturn(Optional.of(friendship));

        // When: Deleting friendship
        friendshipService.deleteFriendship(friendship.getUser().getId(), friendship.getFriend().getId());

        // Then: Verify that the friendship is deleted
        assertEquals(0, friendshipService.getFriends(friendship.getUser().getId()).size());

        // Verify that friendshipRepository.delete(...) was called exactly once
        verify(friendshipRepository, times(1)).delete(friendship);

        // Verify that friendshipRepository.findFriendship(...) is called exactly once
        verify(friendshipRepository, times(1)).findFriendship(anyLong(), anyLong());
    }

    @Test
    void shouldThrowBadRequestException_whenDeleteFriendship_ifFriendshipDoesNotExist() {
        // Given: Mocking the scenario where friendship does not exist
        when(friendshipRepository.findFriendship(anyLong(), anyLong())).thenReturn(Optional.empty());

        // When: Checking if friendship exists
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> friendshipService.deleteFriendship(friendship.getUser().getId(), friendship.getFriend().getId()));

        // Then: Verify that BadRequestException is thrown with appropriate message
        assertEquals(String.format(FRIENDSHIP_DOES_NOT_EXIST_MSG,
                friendship.getUser().getId(), friendship.getFriend().getId()), exception.getMessage());

        // Verify that friendshipRepository.findFriendship(...) is called exactly once
        verify(friendshipRepository, times(1)).findFriendship(anyLong(), anyLong());

        // Verify that friendshipRepository.delete(...) was not called
        verify(friendshipRepository, times(0)).delete(any(Friendship.class));

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldAcceptFriendRequest_whenAcceptFriendRequest_ifRequestExists() {
        // Given: Mocking new friendshipDto
        when(friendshipRepository.findFriendship(anyLong(), anyLong())).thenReturn(Optional.of(friendship));
        when(friendshipRepository.countNumberOfFriends(anyLong())).thenReturn(1);
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);
        when(appConfig.getMaxNumberOfFriends()).thenReturn(50);

        // When: Accepting friend request
       friendshipService.acceptFriendRequest(friendship.getUser().getId(), friendship.getFriend().getId());

        // Verify that friendshipRepository.findFriendship(...) is called exactly once
        verify(friendshipRepository, times(1)).findFriendship(anyLong(), anyLong());

        // Verify that friendshipRepository.countNumberOfFriends(...) is called exactly once
        verify(friendshipRepository, times(1)).countNumberOfFriends(anyLong());

        // Verify that friendshipRepository.save(...) is called exactly once
        verify(friendshipRepository, times(1)).save(any(Friendship.class));

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldAcceptFriendRequest_whenAcceptFriendRequest_ifNumberOfFriendsExceeded() {
        // Given: Mocking the scenario where number of friends is exceeded
        when(friendshipRepository.countNumberOfFriends(anyLong())).thenReturn(maxNumberOfFriends);

        // When: Checking if number of friends is exceeded
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> friendshipService.acceptFriendRequest(friendship.getUser().getId(),
                        friendship.getFriend().getId()));

        // Then: Verify that BadRequestException is thrown with appropriate message
        assertEquals(NUMBER_OF_FRIENDS_EXCEEDED_MSG, exception.getMessage());

        // Verify that friendshipRepository.countNumberOfFriends(...) is called exactly once
        verify(friendshipRepository, times(1)).countNumberOfFriends(anyLong());

        // Verify that friendshipRepository.save(...) was not called
        verify(friendshipRepository, times(0)).save(any(Friendship.class));

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    @Test
    void shouldAcceptFriendRequest_whenAcceptFriendRequest_ifRequestDoesNotExist() {
        // Given: Mocking the scenario where friendship does not exist
        when(friendshipRepository.findFriendship(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(friendshipRepository.countNumberOfFriends(anyLong())).thenReturn(1);
        when(appConfig.getMaxNumberOfFriends()).thenReturn(maxNumberOfFriends);

        // When: Checking if friendship exists
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> friendshipService.acceptFriendRequest(friendship.getUser().getId(),
                        friendship.getFriend().getId()));

        // Then: Verify that BadRequestException is thrown with appropriate message
        assertEquals(String.format(FRIENDSHIP_DOES_NOT_EXIST_MSG,
                friendship.getUser().getId(), friendship.getFriend().getId()), exception.getMessage());

        // Verify that friendshipRepository.findFriendship(...) is called exactly once
        verify(friendshipRepository, times(1)).findFriendship(anyLong(), anyLong());

        // Verify that friendshipRepository.countNumberOfFriends(...) is called exactly once
        verify(friendshipRepository, times(1)).countNumberOfFriends(anyLong());

        // Verify that friendshipRepository.save(...) was not called
        verify(friendshipRepository, times(0)).save(any(Friendship.class));

        // Verify that there are no more interactions with friendshipRepository
        verifyNoMoreInteractions(friendshipRepository);
    }

    private Friendship createFriendship() {
        return Friendship.builder()
                .user(user)
                .friend(createUser(2L, "otherUser"))
                .build();
    }

    private User createUser(Long userId, String username) {
        return User.builder()
                .id(userId)
                .username(username)
                .build();
    }

}