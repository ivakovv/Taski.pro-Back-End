package com.project.taskipro.dto.task;

import com.project.taskipro.model.tasks.enums.PriorityType;
import com.project.taskipro.model.tasks.enums.StatusType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TaskUpdateDto(

        @NotNull
        @Size(min = 1, max = 75)
        String taskName,

        @Size(min = 1, max = 200)
        String taskDescription,

        LocalDateTime taskFinishDate,

        @NotNull
        @Enumerated(EnumType.STRING)
        PriorityType priorityType,

        @NotNull
        @Enumerated(EnumType.STRING)
        StatusType statusType,

        @NotNull
        @Size(min = 1, max = 25)
        String userName,

        List<String> executorUsernames,
        List<String> removeExecutorUsernames,

        @NotNull
        LocalDateTime updateTime) {
}
