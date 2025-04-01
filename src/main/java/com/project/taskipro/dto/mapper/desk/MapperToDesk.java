package com.project.taskipro.dto.mapper.desk;

import com.project.taskipro.dto.desk.DeskCreateDto;
import com.project.taskipro.model.desks.Desks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface MapperToDesk {
    @Mapping(target = "deskName", source = "request.deskName")
    @Mapping(target = "deskDescription", source = "request.deskDescription")
    @Mapping(target = "deskCreateDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "deskFinishDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "userLimit", source = "userLimit")
    Desks deskCreateDtoToDesks(DeskCreateDto request, int userLimit);
}
