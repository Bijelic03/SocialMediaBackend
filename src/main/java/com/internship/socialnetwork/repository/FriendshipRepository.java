package com.internship.socialnetwork.repository;

import com.internship.socialnetwork.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT friendship " +
            "FROM Friendship friendship " +
            "WHERE (friendship.user.id = :userId AND friendship.friendshipStatus = ACCEPTED) OR " +
            "(friendship.friend.id = :userId AND friendship.friendshipStatus = ACCEPTED) ")
    List<Friendship> findAllFriends(Long userId);

    @Query("SELECT friendship " +
            "FROM Friendship friendship " +
            "WHERE friendship.friend.id = :userId AND friendship.friendshipStatus = PENDING")
    List<Friendship> findAllFriendRequests(Long userId);

    @Query("SELECT friendship " +
            "FROM Friendship friendship " +
            "WHERE friendship.user.id = :userId AND friendship.friendshipStatus = PENDING")
    List<Friendship> findAllSentFriendRequests(Long userId);

    @Query("SELECT friendship " +
            "FROM Friendship friendship " +
            "WHERE (friendship.user.id = :userId AND friendship.friend.id = :friendId) " +
            "OR (friendship.user.id = :friendId AND friendship.friend.id = :userId)")
    Optional<Friendship> findFriendship(Long userId, Long friendId);

}