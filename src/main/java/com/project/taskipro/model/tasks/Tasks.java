package com.project.taskipro.model.tasks;

import java.time.LocalDateTime;
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

    @Column(name = "create_date")
    private LocalDateTime taskCreateDate;
    @PrePersist
    public void createDate(){
        if (taskCreateDate == null){
            //taskCreateDate = new Date();
            taskCreateDate = LocalDateTime.now();
        }
        if (taskFinishDate == null){
            //Calendar currentDate = Calendar.getInstance();
            //currentDate.add(Calendar.DATE, 14);
            //taskFinishDate = currentDate.getTime();
            taskFinishDate = LocalDateTime.now().plusDays(14);
        }
    }

    @Column(name = "finish_date")
    private LocalDateTime taskFinishDate;

    @Column(name = "start_date")
    private LocalDateTime taskStartDate;

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
    private PriorityType priorityType = PriorityType.COMMON;
}
