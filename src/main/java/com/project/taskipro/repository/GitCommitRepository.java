package com.project.taskipro.repository;

import com.project.taskipro.model.git.GitCommit;
import com.project.taskipro.model.git.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GitCommitRepository extends JpaRepository<GitCommit, Long> {
    List<GitCommit> findByRepositoryId(Long repositoryId);

    boolean existsByCommitHashAndRepository(String name, GitRepository repository);
}
