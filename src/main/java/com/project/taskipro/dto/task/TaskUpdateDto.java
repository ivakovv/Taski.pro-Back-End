package com.project.taskipro.dto.task;

import com.project.taskipro.model.tasks.enums.PriorityType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskUpdateDto(String taskName, String taskDescription, LocalDateTime taskFinishDate, PriorityType priorityType, String userName) {
}
