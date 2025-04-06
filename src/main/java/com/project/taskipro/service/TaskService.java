package com.project.taskipro.service;

import com.project.taskipro.dto.mapper.task.MapperToTask;
import com.project.taskipro.dto.mapper.task.MapperToTaskResponseDto;
import com.project.taskipro.dto.mapper.task.MapperToTaskStack;
import com.project.taskipro.dto.mapper.task.MapperUpdateTask;
import com.project.taskipro.dto.task.TaskCreateDto;
import com.project.taskipro.dto.task.TaskResponseDto;
import com.project.taskipro.dto.task.TaskStackDto;
import com.project.taskipro.dto.task.TaskUpdateDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.RightType;
import com.project.taskipro.model.tasks.TaskExecutors;
import com.project.taskipro.model.tasks.TaskStack;
import com.project.taskipro.model.tasks.TaskStatuses;
import com.project.taskipro.model.tasks.Tasks;
import com.project.taskipro.model.tasks.enums.StatusType;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.DeskRepository;
import com.project.taskipro.repository.TaskExecutorsRepository;
import com.project.taskipro.repository.TaskRepository;
import com.project.taskipro.repository.TaskStackRepository;
import com.project.taskipro.repository.TaskStatusesRepository;
import com.project.taskipro.repository.UserRightsRepository;
import com.project.taskipro.service.access.UserAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final DeskRepository deskRepository;
    private final TaskExecutorsRepository taskExecutorsRepository;
    private final TaskStatusesRepository taskStatusesRepository;
    private final UserRightsRepository userRightsRepository;
    private final TaskStackRepository taskStackRepository;
    private final MapperToTaskResponseDto mapperToTaskResponseDto;
    private final MapperToTask mapperToTask;
    private final MapperUpdateTask mapperUpdateTask;
    private final MapperToTaskStack mapperToTaskStack;
    private final UserServiceImpl userService;
    private final UserAccessService userAccessService;

    public List<TaskResponseDto> getAllTasks(Long deskId) {
        userAccessService.checkUserAccess(findDeskById(deskId), RightType.MEMBER);
        findDeskById(deskId);
        List<Tasks> tasks = taskRepository.findTasksByDeskId(deskId);
        List<Long> taskIds = tasks.stream().map(Tasks::getId).toList();
        Map<Long, StatusType> latestTaskStatuses = getLatestTaskStatuses(taskIds);
        return tasks.stream()
                .map(task -> mapperToTaskResponseDto.mapToTaskResponseDto(task, latestTaskStatuses.get(task.getId()), getTaskExecutorUsernames(task)))
                .collect(Collectors.toList());
    }

    public TaskResponseDto getTask(Long deskId, Long taskId) {
        userAccessService.checkUserAccess(findDeskById(deskId), RightType.MEMBER);
        Tasks task = findTaskById(taskId);
        if (task.getDesk().getId() != deskId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    String.format("Задача с id: %d не принадлежит доске с id: %d", taskId, deskId));
        }
        return mapperToTaskResponseDto.mapToTaskResponseDto(task, getTaskStatus(taskId), getTaskExecutorUsernames(task));
    }

    public TaskResponseDto createTask(TaskCreateDto taskCreateDto, Long deskId) {
        userAccessService.checkUserAccess(findDeskById(deskId), RightType.MEMBER);
        Desks desk = findDeskById(deskId);
        User user = userService.getCurrentUser();
        Tasks task = mapperToTask.mapToTask(taskCreateDto, desk, user);
        TaskStatuses taskStatuses = TaskStatuses.builder()
                .task(task)
                .statusType(taskCreateDto.statusType())
                .createdDttm(LocalDateTime.now())
                .build();
        taskRepository.save(task);
        taskStatusesRepository.save(taskStatuses);
        return mapperToTaskResponseDto.mapToTaskResponseDto(task, getTaskStatus(task.getId()), getTaskExecutorUsernames(task));
    }

    public void deleteTask(Long deskId, Long taskId) {
        Tasks task = findTaskById(taskId);
        if (!task.getUser().getId().equals(userService.getCurrentUser().getId())) {
            userAccessService.checkUserAccess(findDeskById(deskId), RightType.CONTRIBUTOR);
        }
        taskRepository.delete(task);
    }

    public TaskResponseDto updateTask(Long deskId, Long taskId, TaskUpdateDto taskUpdateDto) {
        Tasks task = findTaskById(taskId);
        User user = userService.getCurrentUser();
        if (!task.getUser().getId().equals(user.getId()) && taskExecutorsRepository.findByTaskAndUser(task, user).isEmpty()) {
            userAccessService.checkUserAccess(findDeskById(deskId), RightType.CONTRIBUTOR);
        }
        if (taskUpdateDto.executorUsernames() != null && !taskUpdateDto.executorUsernames().isEmpty()) {
            addExecutorsToTask(task, deskId, taskUpdateDto.executorUsernames());
        }
        if (taskUpdateDto.removeExecutorUsernames() != null && !taskUpdateDto.removeExecutorUsernames().isEmpty()) {
            removeExecutorsFromTask(task, taskUpdateDto.removeExecutorUsernames());
        }
        if (taskUpdateDto.statusType() != null) {
            TaskStatuses taskStatuses = TaskStatuses.builder()
                    .task(task)
                    .statusType(taskUpdateDto.statusType())
                    .createdDttm(LocalDateTime.now())
                    .build();
            taskStatusesRepository.save(taskStatuses);
        }
        mapperUpdateTask.updateTaskFromDto(taskUpdateDto, task);
        taskRepository.save(task);
        return mapperToTaskResponseDto.mapToTaskResponseDto(task, getTaskStatus(taskId), getTaskExecutorUsernames(task));
    }

    public List<TaskResponseDto> getAllTasksForUser(){
        User currentUser = userService.getCurrentUser();
        List<Tasks> tasks = taskRepository.findTasksByUserId(currentUser.getId());
        List<Long> taskIds = tasks.stream().map(Tasks::getId).toList();
        Map<Long, StatusType> latestTaskStatuses = getLatestTaskStatuses(taskIds);
        return tasks.stream()
                .map(task -> mapperToTaskResponseDto.mapToTaskResponseDto(task, latestTaskStatuses.get(task.getId()), getTaskExecutorUsernames(task)))
                .collect(Collectors.toList());
    }

    public void updateStackForTask(Long taskId, Long deskId, TaskStackDto taskStackDto){
        Tasks task = findTaskById(taskId);
        User user = userService.getCurrentUser();
        if (!task.getUser().getId().equals(user.getId()) && taskExecutorsRepository.findByTaskAndUser(task, user).isEmpty()) {
            userAccessService.checkUserAccess(findDeskById(deskId), RightType.CONTRIBUTOR);
        }
        TaskStack existingStack = taskStackRepository.findByTaskId(taskId).orElse(null);
        if (existingStack != null) {
            existingStack.setTaskStack(taskStackDto.taskStack());
            taskStackRepository.save(existingStack);
        } else {
            TaskStack taskStack = mapperToTaskStack.mapToTaskStack(task, taskStackDto.taskStack());
            taskStackRepository.save(taskStack);
        }
    }

    private Desks findDeskById(Long deskId) {
        return deskRepository.findById(deskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Доска с id: %d не найдена!", deskId)));
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

    private StatusType getTaskStatus(Long taskId) {
        TaskStatuses latestStatus = taskStatusesRepository.findLatestStatusOfTask(taskId);
        return latestStatus.getStatusType();
    }

    private void addExecutorsToTask(Tasks task, Long deskId, List<String> executorUsernames) {
        for (String username : executorUsernames) {
            User executor = userService.findByUsername(username);
            if (!isUserOnDesk(findDeskById(deskId), executor)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Пользователь %s не состоит на данной доске", username));
            }
            if (taskExecutorsRepository.findByTaskAndUser(task, executor).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        String.format("Пользователь %s уже выполняет эту задачу", username));
            }
            TaskExecutors taskExecutor = TaskExecutors.builder()
                    .task(task)
                    .user(executor)
                    .build();
            taskExecutorsRepository.save(taskExecutor);
        }
    }

    private void removeExecutorsFromTask(Tasks task, List<String> executorUsernames) {
        for (String username : executorUsernames) {
            User executor = userService.findByUsername(username);
            Optional<TaskExecutors> taskExecutor = taskExecutorsRepository
                    .findByTaskAndUser(task, executor);
            taskExecutor.ifPresent(taskExecutorsRepository::delete);
        }
    }

    private List<String> getTaskExecutorUsernames(Tasks task) {
        List<TaskExecutors> taskExecutors = taskExecutorsRepository.findByTask(task);
        return taskExecutors.stream()
                .map(te -> te.getUser().getUsername())
                .collect(Collectors.toList());
    }

    private boolean isUserOnDesk(Desks desk, User user) {
        return userRightsRepository.findByDeskAndUser(desk, user).isPresent();
    }
}