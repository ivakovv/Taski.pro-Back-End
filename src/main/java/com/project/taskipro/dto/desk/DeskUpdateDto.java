package com.project.taskipro.dto.desk;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record DeskUpdateDto(

        @NotNull
        @Size(min = 1, max = 50)
        String deskName,

        @Size(min = 0, max = 200)
        String deskDescription,

        LocalDateTime deskFinishDate
) {}
