package com.project.taskipro.dto;

import com.project.taskipro.model.user.email.ValidEmail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record UserFieldsDto(
        @Size(min = 8, max = 25)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$")
        Optional<String> username,

        @Size(min = 1, max = 25)
        @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]+$")
        Optional<String> firstname,

        @Size(min = 1, max = 25)
        @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]+$")
        Optional<String> lastname,
        @NotNull
        @ValidEmail
        @Size(min = 7, max = 50)
        Optional<String> email,

        Optional<String> oldPassword,

        Optional<String> newPassword
) {}
