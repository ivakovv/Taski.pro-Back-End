package com.project.taskipro.repository;

import com.project.taskipro.model.tasks.TaskExecutors;
import com.project.taskipro.model.tasks.Tasks;
import com.project.taskipro.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskExecutorsRepository extends JpaRepository<TaskExecutors, Long> {
    Optional<TaskExecutors> findByTaskAndUser(Tasks task, User user);
    List<TaskExecutors> findByTask(Tasks task);
}
