package com.project.taskipro.model.tasks;

import java.util.Date;

import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.tasks.enums.PriorityType;
import com.project.taskipro.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "priority_type")
    @Enumerated(EnumType.STRING)
    private PriorityType priorityType;
}
