package com.project.taskipro.dto.task;

import com.project.taskipro.model.tasks.enums.PriorityType;
import com.project.taskipro.model.tasks.enums.StatusType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskUpdateDto(String taskName, String taskDescription, LocalDateTime taskFinishDate, PriorityType priorityType, StatusType statusType, String userName) {
}
