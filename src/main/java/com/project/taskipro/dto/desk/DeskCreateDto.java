package com.project.taskipro.dto.desk;

public record DeskCreateDto(
        String userName,
        String deskName,
        String deskDescription
) {}
