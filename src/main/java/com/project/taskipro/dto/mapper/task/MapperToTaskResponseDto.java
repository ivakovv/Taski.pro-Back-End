package com.project.taskipro.dto.mapper.task;

import com.project.taskipro.dto.task.TaskResponseDto;
import com.project.taskipro.model.tasks.Tasks;
import com.project.taskipro.model.tasks.enums.StatusType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToTaskResponseDto {
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "userName", source = "task.user.username")
    @Mapping(target = "taskName", source = "task.taskName")
    @Mapping(target = "taskDescription", source = "task.taskDescription")
    @Mapping(target = "taskCreateDate", source = "task.taskCreateDate")
    @Mapping(target = "taskFinishDate", source = "task.taskFinishDate")
    @Mapping(target = "priorityType", source = "task.priorityType")
    @Mapping(target = "statusType", source = "statusType")
    TaskResponseDto mapToTaskResponseDto(Tasks task, StatusType statusType);

}
