package com.project.taskipro.dto;

import com.project.taskipro.model.user.email.ValidEmail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(

        @NotNull
        @Size(min = 8, max = 25)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$")
        String username,

        @NotNull
        @ValidEmail
        @Size(min = 11, max = 50)
        String email,

        @NotNull
        @Size(min  = 15, max = 20)
        String password) {
}
