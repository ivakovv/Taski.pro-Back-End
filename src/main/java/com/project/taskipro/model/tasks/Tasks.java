package com.project.taskipro.model.tasks;

import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.tasks.enums.PriorityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "create_date")
    private Date taskCreateDate;

    @Column(name = "finish_date")
    private Date taskFinishDate;

    @Column(name = "start_date")
    private Date taskStartDate;

    @Column(name = "task_comment")
    private String taskComment;

    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desks desk;

    @Column(name = "priority_type")
    @Enumerated(EnumType.STRING)
    private PriorityType priorityType;
}
