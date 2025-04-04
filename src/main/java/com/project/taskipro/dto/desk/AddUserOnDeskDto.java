package com.project.taskipro.dto.desk;

import com.project.taskipro.model.desks.RightType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddUserOnDeskDto(

        @NotNull
        @Size(min = 1, max = 25)
        String username,

        @NotNull
        @Enumerated(EnumType.STRING)
        RightType rightType) {

}
