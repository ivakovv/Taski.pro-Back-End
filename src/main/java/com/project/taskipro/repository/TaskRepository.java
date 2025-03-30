package com.project.taskipro.repository;

import com.project.taskipro.model.tasks.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findTasksByDeskId(Long deskId);
}
