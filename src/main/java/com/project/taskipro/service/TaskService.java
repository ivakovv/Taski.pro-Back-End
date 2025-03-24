package com.project.taskipro.service;

import com.project.taskipro.dto.mapper.MapperToTask;
import com.project.taskipro.dto.mapper.MapperToTaskResponseDto;
import com.project.taskipro.dto.task.TaskCreateDto;
import com.project.taskipro.dto.task.TaskResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.tasks.Tasks;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.DeskRepository;
import com.project.taskipro.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final DeskRepository deskRepository;
    private final MapperToTaskResponseDto mapperToTaskResponseDto;
    private final MapperToTask mapperToTask;
    private final UserServiceImpl userService;

    //Нужно просмотреть как лучше передавать владельцев задач
    public List<TaskResponseDto> getAllTasks(Long deskId){
        findDeskById(deskId);
        List<Tasks> tasks = taskRepository.findTasksByDeskId(deskId);
        return tasks.stream()
                .map(mapperToTaskResponseDto::mapToTaskResponseDto)
                .collect(Collectors.toList());
    }

    public Long createTask(TaskCreateDto taskCreateDto, Long deskId){
        Desks desk = findDeskById(deskId);
        User user = userService.getCurrentUser();
        Tasks task = mapperToTask.mapToTask(taskCreateDto, desk, user);
        taskRepository.save(task);
        return task.getId();
    }

    private Desks findDeskById(Long deskId) {
        return deskRepository.findById(deskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Доска с id: %d не найдена!", deskId)));
    }
    private Tasks findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Задача с id: %d не найдена!", taskId)));
    }
}
