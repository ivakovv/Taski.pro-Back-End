package com.project.taskipro.dto.desk;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

public record DeskResponseDto(

        @NotNull
        Long id,

        @NotNull
        @Size(min = 1, max = 50)
        String deskName,

        @Size(min = 0, max = 200)
        String deskDescription,

        @NotNull
        LocalDateTime deskCreateDate,

        LocalDateTime deskFinishDate,
        String username,
        @NotNull
        @Size(min = 1, max = 20)
        int userLimit) {}