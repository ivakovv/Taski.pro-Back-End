package com.project.taskipro.dto.desk;

import java.time.LocalDateTime;

public record DeskUpdateDto(
        String deskName,
        String deskDescription,
        LocalDateTime deskFinishDate
) {}
