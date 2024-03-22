package com.internship.socialnetwork.model;

import jakarta.persistence.*;

@Entity
@Table(name="user_friend")
public class UserFriend {

    @EmbeddedId
    private UserFriendId id;

    @Enumerated(EnumType.STRING)
    @Column(name="friendship_status", nullable = false)
    private FriendshipStatus friendshipStatus;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false, insertable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friendId", updatable = false, insertable = false)
    private User friend;
}