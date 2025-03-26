package com.project.taskipro.service;

import com.project.taskipro.dto.mapper.task.MapperToTask;
import com.project.taskipro.dto.mapper.task.MapperToTaskResponseDto;
import com.project.taskipro.dto.mapper.task.MapperUpdateTask;
import com.project.taskipro.dto.task.TaskCreateDto;
import com.project.taskipro.dto.task.TaskResponseDto;
import com.project.taskipro.dto.task.TaskUpdateDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.RightType;
import com.project.taskipro.model.tasks.TaskStatuses;
import com.project.taskipro.model.tasks.Tasks;
import com.project.taskipro.model.tasks.enums.StatusType;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.DeskRepository;
import com.project.taskipro.repository.TaskRepository;
import com.project.taskipro.repository.TaskStatusesRepository;
import com.project.taskipro.service.access.UserAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final DeskRepository deskRepository;
    private final TaskStatusesRepository taskStatusesRepository;
    private final MapperToTaskResponseDto mapperToTaskResponseDto;
    private final MapperToTask mapperToTask;
    private final MapperUpdateTask mapperUpdateTask;
    private final UserServiceImpl userService;
    private final UserAccessService userAccessService;

    public List<TaskResponseDto> getAllTasks(Long deskId){
        userAccessService.checkUserAccess(findDeskById(deskId), RightType.MEMBER);
        findDeskById(deskId);
        List<Tasks> tasks = taskRepository.findTasksByDeskId(deskId);
        List<Long> taskIds = tasks.stream().map(Tasks::getId).toList();
        Map<Long, StatusType> latestTaskStatuses = getLatestTaskStatuses(taskIds);
        return tasks.stream()
                .map(task -> mapperToTaskResponseDto.mapToTaskResponseDto(task, latestTaskStatuses.get(task.getId())))
                .collect(Collectors.toList());
    }

    public TaskResponseDto getTask(Long deskId, Long taskId){
        userAccessService.checkUserAccess(findDeskById(deskId), RightType.MEMBER);
        Tasks task = findTaskById(taskId);
        if(task.getDesk().getId() != deskId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Задача с id: %d не принадлежит доске с id: %d", taskId, deskId));
        }
        return mapperToTaskResponseDto.mapToTaskResponseDto(task, getTaskStatus(taskId));
    }

    public Long createTask(TaskCreateDto taskCreateDto, Long deskId){
        userAccessService.checkUserAccess(findDeskById(deskId), RightType.MEMBER);
        Desks desk = findDeskById(deskId);
        User user = userService.getCurrentUser();
        Tasks task = mapperToTask.mapToTask(taskCreateDto, desk, user);
        TaskStatuses taskStatuses = TaskStatuses.builder()
                .task(task)
                .statusType(StatusType.BACKLOG)
                .createdAt(LocalDateTime.now())
                .build();
        taskRepository.save(task);
        taskStatusesRepository.save(taskStatuses);
        return task.getId();
    }
    public void deleteTask(Long deskId, Long taskId){
        Tasks task = findTaskById(taskId);
        if(!task.getUser().getId().equals(userService.getCurrentUser().getId())){
            userAccessService.checkUserAccess(findDeskById(deskId), RightType.CONTRIBUTOR);
        }
        taskRepository.delete(task);
    }

    public TaskResponseDto updateTask(Long deskId, Long taskId, TaskUpdateDto taskUpdateDto){
        Tasks task = findTaskById(taskId);
        if (!task.getUser().getId().equals(userService.getCurrentUser().getId())){
            userAccessService.checkUserAccess(findDeskById(deskId), RightType.CONTRIBUTOR);
        }
        if(taskUpdateDto.userName() != null){
            User user = userService.findByUsername(taskUpdateDto.userName());
            task.setUser(user);
        }
        if(taskUpdateDto.statusType() != null){
            TaskStatuses taskStatuses = TaskStatuses.builder()
                    .task(task)
                    .statusType(taskUpdateDto.statusType())
                    .createdAt(LocalDateTime.now())
                    .build();
            taskStatusesRepository.save(taskStatuses);
        }
        mapperUpdateTask.updateTaskFromDto(taskUpdateDto, task);
        taskRepository.save(task);
        return mapperToTaskResponseDto.mapToTaskResponseDto(task, getTaskStatus(taskId));
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

    private Map<Long, StatusType> getLatestTaskStatuses(List<Long> taskIds) {
        List<TaskStatuses> latestStatuses = taskStatusesRepository.findLatestStatusesByTaskIds(taskIds);
        return latestStatuses.stream()
                .collect(Collectors.toMap(
                        status -> status.getTask().getId(),
                        TaskStatuses::getStatusType
                ));
    }
    private StatusType getTaskStatus(Long taskId){
        TaskStatuses latestStatus = taskStatusesRepository.findLatestStatusOfTask(taskId);
        return latestStatus.getStatusType();
    }
}
