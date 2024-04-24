package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Friendship;
import com.internship.socialnetwork.model.FriendshipStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipDto {

    private UserDto user;

    private UserDto friend;

    private FriendshipStatus status;

    @NotBlank
    private String username;

    private String friendUsername;

    public static FriendshipDto convertToDto(Friendship friendship) {
        return FriendshipDto.builder()
                .status(friendship.getFriendshipStatus())
                .user(UserDto.convertToDto(friendship.getUser()))
                .friend(UserDto.convertToDto(friendship.getFriend()))
                .username(friendship.getUser().getUsername())
                .friendUsername(friendship.getFriend().getUsername())
                .build();
    }

}