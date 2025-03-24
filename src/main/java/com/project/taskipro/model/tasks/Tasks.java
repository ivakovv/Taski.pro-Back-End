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
@Table(name="tasks", indexes = {
        @Index(name = "tasks_desk_id_hidx", columnList = "desk_id"),
        @Index(name = "tasks_user_id_hidx", columnList = "user_id"),
        @Index(name = "tasks_priority_type_hidx", columnList = "priority_type")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "task_name", nullable = false, length = 75)
    private String taskName;

    @Column(name = "task_description", length = 200)
    private String taskDescription;

    //Почему не LocalDate?
    @Column(name = "create_date")
    @PrePersist
    public void createDate(){
        if (taskCreateDate == null){
            taskCreateDate = new Date();
            //tasksCreateDate = LocalDate.now();
        }
        if (taskFinishDate == null){
            Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.DATE, 14);
            taskFinishDate = currentDate.getTime();
            //taskFinishDate = LocalDate.now().plusDays(14);
        }
    }
    private Date taskCreateDate;

    //Почему не LocalDate?
    @Column(name = "finish_date")
    private Date taskFinishDate;

    //Поле на удаление (отказались от подтверждения таски исполнителем)
    @Column(name = "start_date")
    private Date taskStartDate;

    @Column(name = "task_comment", length = 200)
    private String taskComment;

    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desks desk;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "priority_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PriorityType priorityType;
}
