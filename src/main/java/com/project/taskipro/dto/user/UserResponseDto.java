package com.project.taskipro.dto.user;

public record UserResponseDto(Long id,
                            String username,
                            String email,
                            String firstname,
                            String lastname) {
}
