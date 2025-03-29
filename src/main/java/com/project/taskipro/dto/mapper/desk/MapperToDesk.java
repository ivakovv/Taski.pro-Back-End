package com.project.taskipro.dto.mapper.desk;

import com.project.taskipro.dto.desk.DeskCreateDto;
import com.project.taskipro.model.desks.Desks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface MapperToDesk {
    @Mapping(target = "deskName", source = "deskName")
    @Mapping(target = "deskDescription", source = "deskDescription")
    @Mapping(target = "deskCreateDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "deskFinishDate", expression = "java(LocalDateTime.now())")
    Desks deskCreateDtoToDesks(DeskCreateDto request);
}
