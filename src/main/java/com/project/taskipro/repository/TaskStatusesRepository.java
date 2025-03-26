package com.project.taskipro.repository;

import com.project.taskipro.model.tasks.TaskStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskStatusesRepository extends JpaRepository<TaskStatuses, Long> {

    @Query("SELECT ts FROM TaskStatuses ts WHERE ts.task.id IN :taskIds " +
            "AND ts.createdAt = (SELECT MAX(ts2.createdAt) FROM TaskStatuses ts2 WHERE ts2.task.id = ts.task.id)")
    List<TaskStatuses> findLatestStatusesByTaskIds(@Param("taskIds") List<Long> taskIds);
}
