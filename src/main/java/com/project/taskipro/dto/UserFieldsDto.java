package com.project.taskipro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserFieldsDto(

        @NotNull
        Long id,

        @NotNull
        @Size(min = 8, max = 25)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$")
        String username,

        @NotNull
        @Size(min = 1, max = 25)
        @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]+$")
        String firstname,

        @NotNull
        @Size(min = 1, max = 25)
        @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]+$")
        String lastname,

        @NotNull
        @Size(min  = 15, max = 20)
        String password
) {
}
