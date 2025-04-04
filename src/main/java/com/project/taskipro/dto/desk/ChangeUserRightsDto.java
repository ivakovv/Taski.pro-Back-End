package com.project.taskipro.dto.desk;

import com.project.taskipro.model.desks.RightType;

public record ChangeUserRightsDto(Long userId, RightType rightType) {
}
