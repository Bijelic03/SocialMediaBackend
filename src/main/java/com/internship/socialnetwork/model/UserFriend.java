package com.internship.socialnetwork.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

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