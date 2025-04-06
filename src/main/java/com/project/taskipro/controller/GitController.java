package com.project.taskipro.controller;

import com.project.taskipro.dto.git.AddGitRepositoryDto;
import com.project.taskipro.dto.git.GitCommitResponseDto;
import com.project.taskipro.dto.git.GitRepositoryResponseDto;
import com.project.taskipro.service.git.GitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GitController {

    private final GitService gitService;

    @PostMapping("/desk/{deskId}/repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Репозиторий успешно добавлен на доску"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена"),
            @ApiResponse(responseCode = "409", description = "Данный репозиторий уже добавлен к доске")
    })
    public ResponseEntity<GitRepositoryResponseDto> addRepositoryOnDesk(@PathVariable("deskId") Long deskId, @RequestBody AddGitRepositoryDto request){
        return ResponseEntity.ok(gitService.addRepositoryOnDesk(deskId, request));
    }

    @DeleteMapping("/desk/{deskId}/repositories/{repositoryId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Репозиторий успешно удален с доски"),
            @ApiResponse(responseCode = "404", description = "Доска или репозиторий не найдены")
    })
    public ResponseEntity<Void> removeRepositoryFromDesk(@PathVariable("deskId") Long deskId, @PathVariable("repositoryId") Long repositoryId){
        gitService.removeRepositoryFromDesk(deskId, repositoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/desk/{deskId}/repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Репозитории успешно получены"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена"),
    })
    public ResponseEntity<List<GitRepositoryResponseDto>> getRepositoriesByDeskId(@PathVariable("deskId") Long deskId){
        return ResponseEntity.ok(gitService.getRepositoriesByDeskId(deskId));
    }

    @GetMapping("/desk/{deskId}/repositories/{repositoryId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Коммиты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Репозиторий не найден"),
    })
    public ResponseEntity<List<GitCommitResponseDto>> getCommitsByRepositoryId(@PathVariable("repositoryId") Long repositoryId){
        return ResponseEntity.ok(gitService.getCommitsByRepositoryId(repositoryId));
    }

    @PostMapping("/desk/{deskId}/repositories/{repositoryId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Репозиторий успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Репозиторий не найден"),
            @ApiResponse(responseCode = "503", description = "Не удалось синхронизировать репозиторий")
    })
    public ResponseEntity<Void> syncRepository(@PathVariable("repositoryId") Long repositoryId){
        gitService.syncRepository(repositoryId);
        return ResponseEntity.ok().build();
    }
}
