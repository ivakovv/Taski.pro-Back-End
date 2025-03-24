package com.project.taskipro.dto.mapper;

import com.project.taskipro.dto.task.TaskCreateDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.tasks.Tasks;
import com.project.taskipro.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface MapperToTask {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskName", source = "taskCreateDto.taskName")
    @Mapping(target = "taskDescription", source = "taskCreateDto.taskDescription")
    @Mapping(target = "taskCreateDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "taskFinishDate", source = "taskCreateDto.taskFinishDate")
    @Mapping(target = "desk", source = "desk")
    @Mapping(target = "user", source = "user")
    Tasks mapToTask(TaskCreateDto taskCreateDto, Desks desk, User user);
}
