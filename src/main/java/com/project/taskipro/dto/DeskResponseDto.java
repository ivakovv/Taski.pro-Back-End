package com.project.taskipro.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
@Builder
@RequiredArgsConstructor
public class DeskResponseDto {

    private final Long id;
    private final String deskName;
    private final String deskDescription;
    private final Date deskCreateDate;
    private final Date deskFinishDate;
    private final int userLimit;

}
