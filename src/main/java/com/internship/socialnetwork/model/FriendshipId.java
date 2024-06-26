package com.internship.socialnetwork.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class FriendshipId implements Serializable {

    private Long userId;

    private Long friendId;

}