package com.project.taskipro.model.tasks;


import com.project.taskipro.model.tasks.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="task_statuses", indexes = {
        @Index(name = "task_statuses_task_id_hidx", columnList = "task_id"),
        @Index(name = "task_statuses_status_type_hidx", columnList = "status_type")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatuses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "status_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Tasks task;

    @Column(name = "created_dttm")
    private LocalDateTime createdDttm;
}
