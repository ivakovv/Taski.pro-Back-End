package com.project.taskipro.model.tasks;

import java.time.LocalDateTime;

import com.project.taskipro.model.desks.Desks;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_stack")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStack {

    @Id
    @Column(name = "task_id")
    private long taskId;

    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desks desk;

    @OneToOne
    @MapsId
    @JoinColumn(name = "task_id")
    private Tasks tasks;

    @Column(name = "task_stack")
    private String taskStack;

    @Column(name = "task_recommendation")
    private String taskRecommendation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
