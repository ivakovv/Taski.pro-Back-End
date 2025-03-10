package com.project.taskipro.entity.tasks;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="task_statuses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatuses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Tasks task;
}
