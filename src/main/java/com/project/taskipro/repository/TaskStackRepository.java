package com.project.taskipro.repository;

import com.project.taskipro.model.tasks.TaskStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskStackRepository extends JpaRepository<TaskStack, Long> {
    Optional<TaskStack> findByTaskId(Long taskId);

    @Modifying
    @Transactional
    @Query("UPDATE TaskStack SET taskRecommendation = :recommendation, createdAt = :current_time WHERE taskId = :task_id AND createdAt < :current_time")
    void updateTaskRecommendation(@Param("task_id")long taskId, @Param("recommendation") String recommendation, @Param("current_time")LocalDateTime currentTime);

    @Query("SELECT taskRecommendation FROM TaskStack WHERE taskId = :task_id AND createdAt >= :current_time")
    List<String> getRecommendationByTime(@Param("task_id") Long taskId, @Param("current_time") LocalDateTime currentTime);
}
