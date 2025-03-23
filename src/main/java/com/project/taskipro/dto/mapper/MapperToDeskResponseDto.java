package com.project.taskipro.dto.mapper;

import com.project.taskipro.dto.desk.DeskResponseDto;
import com.project.taskipro.model.desks.Desks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToDeskResponseDto {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "deskName", target = "deskName")
    @Mapping(source = "deskDescription", target = "deskDescription")
    @Mapping(source = "deskCreateDate", target = "deskCreateDate")
    @Mapping(source = "deskFinishDate", target = "deskFinishDate")
    @Mapping(source = "userLimit", target = "userLimit")
    DeskResponseDto mapToDeskResponseDto(Desks desk);
}
