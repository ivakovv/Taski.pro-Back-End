package com.project.taskipro.dto.desk;

import lombok.Builder;

@Builder
public record DeskCreateDto(String userName, String deskName, String deskDescription) {}
