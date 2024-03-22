package com.internship.socialnetwork.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserFriendId implements Serializable {

    private long userId;

    private long friendId;

    public int hashCode() {
        return (int)(userId + friendId);
    }

    public boolean equals(Object object) {
        if (object instanceof UserFriendId) {
            UserFriendId otherId = (UserFriendId) object;
            return (otherId.userId == this.userId) && (otherId.friendId == this.friendId);
        }
        return false;
    }

}
