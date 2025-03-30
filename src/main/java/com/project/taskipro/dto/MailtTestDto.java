package com.project.taskipro.dto;

import jakarta.validation.constraints.NotNull;

public record MailtTestDto(

        @NotNull
        Long userId) {
}
