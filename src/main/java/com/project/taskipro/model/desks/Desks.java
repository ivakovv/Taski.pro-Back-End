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
@Table(name="desks", indexes = {
        @Index(name = "desks_desk_name_hidx", columnList = "desk_name"),
        @Index(name = "desks_create_date_hidx", columnList = "create_date")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Desks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "desk_name", nullable = false, length = 50)
    private String deskName;

    @Column(name = "desk_description", length = 200)
    private String deskDescription;

    //Может для доски достаточно хранить только дату?
    @Column(name = "create_date")
    @PrePersist
    public void setCreateDate(){
        if (deskCreateDate == null){
            deskCreateDate = LocalDateTime.now();
        }
    }
    private LocalDateTime deskCreateDate;

    @Column(name = "finish_date")
    private LocalDateTime deskFinishDate;

    @Column(name = "user_limit", nullable = false)
    @Min(1)
    @Max(20)
    private int userLimit;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
    private List<UserRights> userRights;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
    private List<Tasks> tasks;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
    private List<Teams> teams;
}
