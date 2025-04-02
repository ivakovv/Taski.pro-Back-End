package com.project.taskipro.service;

import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class PhotoService {

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
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

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

    public void uploadFile(MultipartFile photo) throws IOException {

        String key = "user_avatars/" + userService.getCurrentUser().getId();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(photo.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(photo.getBytes()));

    }

    public ResponseEntity<byte[]> downloadFile(Long userId) throws IOException {

          userRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден!"));

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key("user_avatars/" + userId)
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
}
