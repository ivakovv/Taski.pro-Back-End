package com.project.taskipro.dto.mapper.desk;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.project.taskipro.dto.desk.DeskCreateDto;
import com.project.taskipro.model.desks.Desks;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, imports = LocalDateTime.class)
public interface MapperToDesk {
    @Mapping(target = "deskName", source = "request.deskName")
    @Mapping(target = "deskDescription", source = "request.deskDescription")
    @Mapping(target = "deskCreateDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "deskFinishDate", expression = "java(LocalDateTime.now().plusYears(1))")
    @Mapping(target = "userLimit", source = "userLimit")
    Desks deskCreateDtoToDesks(DeskCreateDto request, int userLimit);
}
