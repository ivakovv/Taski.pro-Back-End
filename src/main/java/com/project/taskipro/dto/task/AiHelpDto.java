package com.project.taskipro.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class AiHelpDto {
    @NotNull
    String text;
    @NotNull
    String status;
}
