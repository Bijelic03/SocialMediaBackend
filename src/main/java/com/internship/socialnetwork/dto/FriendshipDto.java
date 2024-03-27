package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Friendship;
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

    private Long userId;

    private Long friendId;

    @NotBlank
    private String username;

    private String friendUsername;

    public static FriendshipDto convertToDto(Friendship friendship) {
        return FriendshipDto.builder()
                .userId(friendship.getUser().getId())
                .friendId(friendship.getFriend().getId())
                .username(friendship.getUser().getUsername())
                .friendUsername(friendship.getFriend().getUsername())
                .build();
    }

}