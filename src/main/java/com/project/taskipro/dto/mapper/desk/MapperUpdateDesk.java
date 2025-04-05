package com.project.taskipro.dto.mapper.desk;

import com.project.taskipro.dto.desk.DeskUpdateDto;
import com.project.taskipro.model.desks.Desks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapperUpdateDesk {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deskCreateDate", ignore = true)
    @Mapping(target = "userLimit", ignore = true)
    @Mapping(target = "deskName", source = "deskName")
    @Mapping(target = "deskDescription", source = "deskDescription")
    @Mapping(target = "deskFinishDate", source = "deskFinishDate")
    void updateDeskFromDto(DeskUpdateDto deskUpdateDto, @MappingTarget Desks desk);
}
