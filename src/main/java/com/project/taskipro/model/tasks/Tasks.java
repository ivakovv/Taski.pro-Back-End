package com.project.taskipro.model.tasks;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.tasks.enums.PriorityType;
import com.project.taskipro.model.user.User;

import jakarta.persistence.*;
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

    //Почему не LocalDate?
    @Column(name = "create_date")
    private Date taskCreateDate;

    //Почему не LocalDate?
    @Column(name = "finish_date")
    private Date taskFinishDate;

    //Поле на удаление (отказались от подтверждения таски исполнителем)
    @Column(name = "start_date")
    private Date taskStartDate;

    @Column(name = "task_comment", length = 200)
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
