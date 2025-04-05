package com.project.taskipro.controller;

import com.project.taskipro.constants.Constants;
import com.project.taskipro.service.StorageService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/storage")
@Tag(name = "File Storage", description = "API для управления аватарами пользователей и документами досок")
public class StorageController {

    private final StorageService storageService;


    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Аватар успешно загружен"),
            @ApiResponse(responseCode = "400", description = "Недопустимый формат файла"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping(value = "/avatars", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadUserAvatar(@RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > Constants.MAX_PHOTO_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("Размер фото не должен превышать 20 МБ");
        }
        storageService.uploadUserAvatar(avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/avatars/{userId}")
    public ResponseEntity<byte[]> downloadUserAvatar(@PathVariable Long userId) throws IOException {
        return storageService.downloadUserAvatar(userId);
    }

    @GetMapping("/avatars/batch")
    public ResponseEntity<Map<String, String>> downloadUsersAvatars(@RequestParam Long[] userIds) {
        return storageService.downloadUsersAvatars(userIds);
    }

    @PutMapping(value = "/desks/{deskId}/documents", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadDeskDocument(
            @PathVariable Long deskId,
            @RequestParam MultipartFile document) throws IOException {

        if (document.getSize() > Constants.MAX_DOCUMENT_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("Размер документа не должен превышать 50 МБ");
        }
        storageService.uploadDeskDocument(deskId, document);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/desks/{deskId}/documents/{filename}")
    public ResponseEntity<byte[]> downloadDeskDocument(
            @PathVariable Long deskId,
            @PathVariable String filename) throws IOException {
        return storageService.downloadDeskDocument(deskId, filename);
    }

    @GetMapping("/desks/documents/batch")
    public ResponseEntity<Map<String, Map<String, String>>> downloadDeskDocuments(
            @RequestParam Long[] deskIds) {
        return storageService.downloadDeskDocuments(deskIds);
    }
}