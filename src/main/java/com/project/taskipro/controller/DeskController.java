package com.project.taskipro.controller;

import com.project.taskipro.dto.DeskCreateDto;
import com.project.taskipro.dto.DeskResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.repository.DeskRepository;
import com.project.taskipro.service.DeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor

@RestController
@RequestMapping("/desk")
public class DeskController {
    private final DeskService deskService;
    private final DeskRepository deskRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createDesk(@RequestBody DeskCreateDto deskCreateDto){
        deskService.createDesk(deskCreateDto);
        return ResponseEntity.ok("Доска успешно создана!");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDesk(@PathVariable Long id){
        deskService.deleteDesk(id);
        return ResponseEntity.ok("Доска успешно удалена!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeskResponseDto> getDeskById(@PathVariable Long id){
        Desks desk = deskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Доска с id: " + id + " не найдена"));

        return ResponseEntity.ok(deskService.mapToResponseDto(desk));
    }
}
