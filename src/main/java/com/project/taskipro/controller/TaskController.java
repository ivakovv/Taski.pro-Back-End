package com.project.taskipro.controller;

import com.project.taskipro.dto.task.TaskCreateDto;
import com.project.taskipro.dto.task.TaskResponseDto;
import com.project.taskipro.dto.task.TaskUpdateDto;
import com.project.taskipro.service.TaskService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/desk/{deskId}/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены!"),
            @ApiResponse(responseCode = "403", description = "У пользователя нет доступа к доске!"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена!")
    })
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(@PathVariable Long deskId){
        return ResponseEntity.ok(taskService.getAllTasks(deskId));
    }

    @GetMapping("/{taskId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно получена!"),
            @ApiResponse(responseCode = "403", description = "У пользователя нет доступа к доске!"),
            @ApiResponse(responseCode = "404", description = "Доска или задача не найдена!")
    })
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long deskId, @PathVariable Long taskId){
        return ResponseEntity.ok(taskService.getTask(deskId, taskId));
    }

    @PostMapping("/create")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены!"),
            @ApiResponse(responseCode = "403", description = "У пользователя нет доступа к доске!"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена!")
    })
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable Long deskId, @RequestBody TaskCreateDto taskCreateDto){
        return ResponseEntity.ok(taskService.createTask(taskCreateDto, deskId));
    }

    @DeleteMapping("/{taskId}")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Задача успешно удалена!"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для удаления задачи!"),
            @ApiResponse(responseCode = "404", description = "Доска или задача не найдена!")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long deskId, @PathVariable Long taskId){
        taskService.deleteTask(deskId, taskId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{taskId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена!"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для обновления задачи!"),
            @ApiResponse(responseCode = "404", description = "Доска или задача не найдена!")
    })
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long deskId, @PathVariable Long taskId, @RequestBody TaskUpdateDto taskUpdateDto){
        return ResponseEntity.ok(taskService.updateTask(deskId, taskId, taskUpdateDto));
    }
}
