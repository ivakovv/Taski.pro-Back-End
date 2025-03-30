package com.project.taskipro.dto;

import jakarta.validation.constraints.NotNull;

public record ChangeEntityDto(

        @NotNull
        Long userId,

        String resetCode) {
}
