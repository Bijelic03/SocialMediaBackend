package com.internship.socialnetwork.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserFriendId implements Serializable {

    private long userId;

    private long friendId;

}
