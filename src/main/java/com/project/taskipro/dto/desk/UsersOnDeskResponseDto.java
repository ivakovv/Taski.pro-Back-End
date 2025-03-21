package com.project.taskipro.dto.desk;

import com.project.taskipro.model.desks.RightType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UsersOnDeskResponseDto {

    private final String userName;
    private final RightType rightType;

}
