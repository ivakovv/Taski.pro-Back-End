package com.project.taskipro.dto.desk;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record DeskCreateDto(
        @NotNull
        @Size(min = 1, max = 50)
        String deskName,

        @Size(min = 0, max = 200)
        String deskDescription) {}
