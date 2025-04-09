package com.project.taskipro.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${s3.key_id}")
    private String keyId;

    @Value("${s3.secret_key}")
    private String secretKey;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.bucket}")
    private String bucket;

    private S3Client s3Client;
    private final UserServiceImpl userService;
    private final DeskService deskService;
    private final TaskService taskService;

    @PostConstruct
    public void init() {
        if (keyId == null || secretKey == null) {
            throw new IllegalStateException("S3 credentials are not set!");
        }

        AwsCredentials credentials = AwsBasicCredentials.create(keyId, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public void uploadFile(String filePath, MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(filePath)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
    }

    public ResponseEntity<byte[]> downloadFile(String filePath) throws IOException {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(filePath)
                .build();

        var inputStream = s3Client.getObject(objectRequest);
        byte[] data = inputStream.readAllBytes();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
        headers.add(HttpHeaders.CONTENT_TYPE, inputStream.response().contentType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    public void uploadUserAvatar(MultipartFile photo) throws IOException {
        String key = String.format("user_avatars/%s", userService.getCurrentUser().getUsername());
        uploadFile(key, photo);
    }

    public ResponseEntity<byte[]> downloadUserAvatar(String username) throws IOException {
        userService.findByUsername(username);
        return downloadFile(String.format("user_avatars/%s", username));
    }

    public ResponseEntity<Map<String, String>> downloadUsersAvatars(@RequestParam String[] usernames) {
        Map<String, String> response = new HashMap<>();

        for (String username : usernames) {
            try {
                try {
                    userService.findByUsername(username);
                } catch (ResponseStatusException e) {
                    log.warn("Пользователь {} не найден: {}", username, e.getMessage());
                    response.put(username, null);
                    continue;
                }

                GetObjectRequest objectRequest = GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(String.format("user_avatars/%s", username))
                        .build();

                try (ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(objectRequest)) {
                    byte[] avatarBytes = inputStream.readAllBytes();
                    String base64Avatar = Base64.getEncoder().encodeToString(avatarBytes);
                    String contentType = inputStream.response().contentType();
                    response.put(username, String.format("data:%s;base64,%s", contentType, base64Avatar));
                }
            } catch (NoSuchKeyException e) {
                log.warn("Аватар не найден для пользователя {}", username);
                response.put(username, null);
            } catch (S3Exception | IOException e) {
                log.error(String.format("Ошибка при загрузке аватара для пользователя %s ", username), e);
                response.put(username, null);
            }
        }

        return ResponseEntity.ok().body(response);
    }

    public void uploadDeskDocument(Long deskId, MultipartFile document) throws IOException {

        deskService.getDeskById(deskId);

        String key = String.format("desk_documents/%d/%s", deskId, document.getOriginalFilename());
        uploadFile(key, document);
    }

    public ResponseEntity<byte[]> downloadDeskDocument(Long deskId, String filename) throws IOException {

        deskService.getDeskById(deskId);

        return downloadFile(String.format("desk_documents/%d/%s", deskId, filename));
    }

    public ResponseEntity<Map<String, Map<String, String>>> downloadDeskDocuments(@RequestParam Long[] deskIds) {
        Map<String, Map<String, String>> response = new HashMap<>();

        for (Long deskId : deskIds) {
            try {
                deskService.getDeskById(deskId);
                ListObjectsResponse listResponse = s3Client.listObjects(ListObjectsRequest.builder()
                        .bucket(bucket)
                        .prefix(String.format("desk_documents/%d/", deskId))
                        .build());

                if (listResponse.contents().isEmpty()) {
                    response.put(deskId.toString(), null);
                    continue;
                }

                Map<String, String> deskDocuments = new HashMap<>();
                for (S3Object s3Object : listResponse.contents()) {
                    String documentKey = s3Object.key();
                    if (!documentKey.endsWith("/")) {
                        String documentName = documentKey.substring(documentKey.lastIndexOf('/') + 1);
                        try (ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(GetObjectRequest.builder()
                                .bucket(bucket)
                                .key(documentKey)
                                .build())) {
                            byte[] documentBytes = inputStream.readAllBytes();
                            String base64Document = Base64.getEncoder().encodeToString(documentBytes);
                            deskDocuments.put(documentName, "data:" + inputStream.response().contentType() + ";base64," + base64Document);
                        }
                    }
                }

                response.put(deskId.toString(), deskDocuments.isEmpty() ? null : deskDocuments);
            } catch (ResponseStatusException | NoSuchKeyException e) {
                log.warn("Документы не найдены для доски {}", deskId);
                response.put(deskId.toString(), null);
            } catch (S3Exception | IOException e) {
                log.error("Ошибка при загрузке документов для доски {}", deskId, e);
                response.put(deskId.toString(), null);
            }
        }

        return ResponseEntity.ok().body(response);
    }

    public void uploadTaskDocument(Long deskId, Long taskId, MultipartFile document) throws IOException {
        taskService.getTask(deskId, taskId);
        String key = String.format("task_documents/%d/%s", taskId, document.getOriginalFilename());
        uploadFile(key, document);
    }

    public ResponseEntity<Map<String, Map<String, String>>> downloadTaskDocuments(Long deskId, @RequestParam Long[] taskIds) {
        Map<String, Map<String, String>> response = new HashMap<>();

        for (Long taskId : taskIds) {
            try {

                taskService.getTask(deskId, taskId);

                ListObjectsResponse listResponse = s3Client.listObjects(ListObjectsRequest.builder()
                        .bucket(bucket)
                        .prefix(String.format("task_documents/%d/", taskId))
                        .build());

                if (listResponse.contents().isEmpty()) {
                    response.put(taskId.toString(), null);
                    continue;
                }

                Map<String, String> taskDocuments = new HashMap<>();
                for (S3Object s3Object : listResponse.contents()) {
                    String documentKey = s3Object.key();
                    if (!documentKey.endsWith("/")) {
                        String documentName = documentKey.substring(documentKey.lastIndexOf('/') + 1);
                        try (ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(GetObjectRequest.builder()
                                .bucket(bucket)
                                .key(documentKey)
                                .build())) {
                            byte[] documentBytes = inputStream.readAllBytes();
                            String base64Document = Base64.getEncoder().encodeToString(documentBytes);
                            taskDocuments.put(documentName, "data:" + inputStream.response().contentType() + ";base64," + base64Document);
                        }
                    }
                }

                response.put(taskId.toString(), taskDocuments.isEmpty() ? null : taskDocuments);
            } catch (ResponseStatusException | NoSuchKeyException e) {
                log.warn("Документы не найдены для задачи {}", taskId);
                response.put(taskId.toString(), null);
            } catch (S3Exception | IOException e) {
                log.error("Ошибка при загрузке документов для задачи {}", taskId, e);
                response.put(taskId.toString(), null);
            }
        }

        return ResponseEntity.ok().body(response);
    }
}