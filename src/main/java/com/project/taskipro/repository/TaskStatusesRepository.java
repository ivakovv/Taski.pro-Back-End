package com.project.taskipro.repository;

import com.project.taskipro.model.tasks.TaskStatuses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusesRepository extends JpaRepository<TaskStatuses, Long> {
}
