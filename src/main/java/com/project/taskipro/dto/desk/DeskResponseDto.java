package com.project.taskipro.dto.desk;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DeskResponseDto(Long id, String deskName, String deskDescription, LocalDateTime deskCreateDate, LocalDateTime deskFinishDate, String username, int userLimit) {}
