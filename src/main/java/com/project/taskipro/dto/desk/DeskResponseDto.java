package com.project.taskipro.dto.desk;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class DeskResponseDto {

    private final Long id;
    private final String deskName;
    private final String deskDescription;
    private final LocalDateTime deskCreateDate;
    private final LocalDateTime deskFinishDate;
    private final int userLimit;

}
