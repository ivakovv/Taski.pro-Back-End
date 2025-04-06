package com.project.taskipro.dto;

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

        Optional<String> oldPassword,

        Optional<String> newPassword
) {}
