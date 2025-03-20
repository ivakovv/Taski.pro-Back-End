package com.project.taskipro.dto.desk;

import java.util.Date;

public record DeskUpdateDto(
        String deskName,
        String deskDescription,
        Date deskFinishDate
) {}
