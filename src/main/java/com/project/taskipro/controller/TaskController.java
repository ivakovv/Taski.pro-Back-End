package com.project.taskipro.controller;


import com.project.taskipro.dto.task.TaskCreateDto;
import com.project.taskipro.dto.task.TaskResponseDto;
import com.project.taskipro.service.TaskService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
            @ApiResponse(responseCode = "404", description = "Доска не найдена!")
    })
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(@PathVariable Long deskId){
        return ResponseEntity.ok(taskService.getAllTasks(deskId));
    }

    @PostMapping("/create")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены!"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена!")
    })
    public ResponseEntity<Long> createTask(@PathVariable Long deskId, @RequestBody TaskCreateDto taskCreateDto){
        return ResponseEntity.ok(taskService.createTask(taskCreateDto, deskId));
    }
}
