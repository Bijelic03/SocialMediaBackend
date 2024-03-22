package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {

    @NonNull
    private String username;

    private String name;

    private String surname;

    private String email;

    public static UserDto convertToDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .build();
    }

    public User covertToModel() {
        return User.builder()
                .username(getUsername())
                .name(getName())
                .surname(getSurname())
                .email(getEmail())
                .build();
    }

}
