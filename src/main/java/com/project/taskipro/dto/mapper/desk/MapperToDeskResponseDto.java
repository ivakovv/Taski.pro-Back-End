package com.project.taskipro.dto.mapper.desk;

import com.project.taskipro.dto.desk.DeskResponseDto;
import com.project.taskipro.model.desks.Desks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToDeskResponseDto {
    @Mapping(source = "desk.id", target = "id")
    @Mapping(source = "desk.deskName", target = "deskName")
    @Mapping(source = "desk.deskDescription", target = "deskDescription")
    @Mapping(source = "desk.deskCreateDate", target = "deskCreateDate")
    @Mapping(source = "desk.deskFinishDate", target = "deskFinishDate")
    @Mapping(source = "desk.userLimit", target = "userLimit")
    @Mapping(source = "username", target = "username")
    DeskResponseDto mapToDeskResponseDto(Desks desk, String username);
}
