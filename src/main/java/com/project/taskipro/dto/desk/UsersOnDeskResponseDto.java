package com.project.taskipro.dto.desk;

import com.project.taskipro.model.desks.RightType;
import com.project.taskipro.model.user.enums.RoleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;


@Builder
public record UsersOnDeskResponseDto(

        @NotNull
        Long id,

        @NotNull
        @Size(min = 1, max = 25)
        String userName,

        @NotNull
        @Enumerated(EnumType.STRING)
        RightType rightType,

        @Enumerated(EnumType.STRING)
        RoleType roleType
        ) {}
