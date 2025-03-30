package com.project.taskipro.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticationResponseDto(

        @NotNull
        String accessToken,

        @NotNull
        String refreshToken) {
}
