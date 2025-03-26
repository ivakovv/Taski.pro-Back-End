package com.project.taskipro.dto.mapper.task;

import com.project.taskipro.dto.task.TaskResponseDto;
import com.project.taskipro.model.tasks.Tasks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToTaskResponseDto {
    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "taskName", source = "taskName")
    @Mapping(target = "taskDescription", source = "taskDescription")
    @Mapping(target = "taskCreateDate", source = "taskCreateDate")
    @Mapping(target = "taskFinishDate", source = "taskFinishDate")
    @Mapping(target = "priorityType", source = "priorityType")
    @Mapping(target = "statusType", source = "statusType")
    TaskResponseDto mapToTaskResponseDto(Tasks task);
}
