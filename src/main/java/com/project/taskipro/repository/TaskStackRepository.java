package com.project.taskipro.repository;

import com.project.taskipro.model.tasks.TaskStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStackRepository extends JpaRepository<TaskStack, Long> {
    Optional<TaskStack> findByTaskId(Long taskId);
}
