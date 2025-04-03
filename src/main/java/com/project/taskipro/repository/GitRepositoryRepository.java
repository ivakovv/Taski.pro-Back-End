package com.project.taskipro.repository;

import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.git.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GitRepositoryRepository extends JpaRepository<GitRepository, Long> {
    Optional<GitRepository> findByRepositoryUrlAndDesk(String url, Desks desk);
    Optional<GitRepository> findByIdAndDesk(Long id, Desks desk);
    List<GitRepository> findByDeskId(Long deskId);
}
