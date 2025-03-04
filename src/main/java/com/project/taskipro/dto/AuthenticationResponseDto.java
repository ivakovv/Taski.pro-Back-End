package com.project.taskipro.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthenticationResponseDto {

    private final String accessToken;

    private final String refreshToken;


}
