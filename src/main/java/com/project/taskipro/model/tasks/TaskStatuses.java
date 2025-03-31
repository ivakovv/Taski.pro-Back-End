package com.project.taskipro.model.tasks;


import com.project.taskipro.model.audit.EntityAuditable;
import com.project.taskipro.model.tasks.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="task_statuses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatuses implements EntityAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "status_type")
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Tasks task;

    @Column(name = "created_dttm")
    private LocalDateTime createdDttm;

    @Override
    public void setCreationDate(LocalDateTime creationDate) {
        this.createdDttm = creationDate;
    }
}
