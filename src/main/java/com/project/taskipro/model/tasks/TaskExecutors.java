package com.project.taskipro.model.tasks;

import com.project.taskipro.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_executors")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskExecutors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Tasks task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
