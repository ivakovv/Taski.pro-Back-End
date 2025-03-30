package com.project.taskipro.dto;

import jakarta.validation.constraints.NotNull;

public record UserCredentialsResetDto(

        @NotNull
        Long userId) {
}
