package com.internship.socialnetwork.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank
    private String username;

    private String name;

    private String surname;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}