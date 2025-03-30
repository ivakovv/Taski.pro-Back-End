package com.project.taskipro.dto;

public record AuthenticationResponseDto(String accessToken,
                                        String refreshToken) {
}
