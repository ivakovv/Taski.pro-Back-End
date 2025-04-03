package com.project.taskipro.dto.git;

import java.time.LocalDateTime;

public record GitCommitResponseDto(Long id, String commitHash, String shortMessage, String fullMessage,
                                   String authorName, String authorEmail, LocalDateTime commitDate, Long repositoryId, String repositoryName,String commitUrl) {
}
