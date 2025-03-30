package com.project.taskipro.dto.mapper.task;

import com.project.taskipro.dto.task.TaskUpdateDto;
import com.project.taskipro.model.tasks.Tasks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {})
public interface MapperUpdateTask {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskCreateDate", ignore = true)
    @Mapping(target = "taskName", source = "taskName")
    @Mapping(target = "taskDescription", source = "taskDescription")
    @Mapping(target = "taskFinishDate", source = "taskFinishDate")
    @Mapping(target = "priorityType", source = "priorityType")
    @Mapping(target = "user", ignore = true)
    void updateTaskFromDto(TaskUpdateDto taskUpdateDto, @MappingTarget Tasks task);
}
