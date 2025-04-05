package com.project.taskipro.dto.git;

import java.time.LocalDateTime;

public record GitRepositoryResponseDto(Long id, String repositoryUrl, String repositoryName, String repositoryDescription, String branchName, LocalDateTime lastSyncDate, LocalDateTime createDate) {}
