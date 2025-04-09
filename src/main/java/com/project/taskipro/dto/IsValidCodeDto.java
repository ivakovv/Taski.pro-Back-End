package com.project.taskipro.dto;

import com.project.taskipro.model.codes.CodeType;

public record IsValidCodeDto(
        String email,
        String code,
        CodeType type
) {
}
