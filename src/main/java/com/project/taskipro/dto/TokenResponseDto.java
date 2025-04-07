package com.project.taskipro.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {}
