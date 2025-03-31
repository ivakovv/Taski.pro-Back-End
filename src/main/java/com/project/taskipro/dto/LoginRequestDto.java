package com.project.taskipro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(

        @NotNull
        @Size(min = 8, max = 25)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$")
        String username,

        @NotNull
        @Size(min = 15, max = 20)
        String password) {
}
