package com.project.taskipro.model.desks;

import java.time.LocalDateTime;
import java.util.List;

import com.project.taskipro.model.tasks.Tasks;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name="desks")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Desks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "desk_name")
    private String deskName;

    @Column(name = "desk_description")
    private String deskDescription;

    @Column(name = "create_date")
    /*@PrePersist
    public void setCreateDate(){
        if (deskCreateDate == null){
            deskCreateDate = LocalDateTime.now();
        }
    }*/
    private LocalDateTime deskCreateDate;

    @Column(name = "finish_date")
    private LocalDateTime deskFinishDate;

    @Column(name = "user_limit")
    private int userLimit;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
    private List<UserRights> userRights;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
    private List<Tasks> tasks;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
    private List<Teams> teams;
}
