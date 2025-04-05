package com.project.taskipro.dto.mapper.task;

import com.project.taskipro.model.tasks.TaskStack;
import com.project.taskipro.model.tasks.Tasks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToTaskStack {
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "desk", source = "task.desk")
    @Mapping(target = "taskStack", source = "taskStack")
    @Mapping(target = "taskRecommendation", ignore = true)
    TaskStack mapToTaskStack(Tasks task, String taskStack);
}
