package com.project.taskipro.dto.desk;

import com.project.taskipro.model.desks.RightType;
import lombok.Builder;


@Builder
public record UsersOnDeskResponseDto(Long id, String userName, RightType rightType) {}
