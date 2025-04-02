package com.project.taskipro.controller;

import com.project.taskipro.service.PhotoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping("/photos")
@Tag(name = "Photos")
@ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
public class PhotoController {

    private final PhotoService photoService;

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(@RequestParam MultipartFile photo) throws IOException {

        photoService.uploadFile(photo);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<byte[]> downloadFile(@RequestParam Long userId) throws IOException {

        return photoService.downloadFile(userId);

    }
}