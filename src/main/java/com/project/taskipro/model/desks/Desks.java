package com.project.taskipro.model.desks;

import java.util.Date;
import java.util.List;

import com.project.taskipro.model.tasks.Tasks;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="desks")
@Getter
@Setter
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
    private Date deskCreateDate;

    @Column(name = "finish_date")
    private Date deskFinishDate;

    @Column(name = "user_limit")
    private int userLimit;

    @OneToMany(mappedBy = "desk")
    private List<UserRights> userRights;

    @OneToMany(mappedBy = "desk")
    private List<Tasks> tasks;

    @OneToMany(mappedBy = "desk")
    private List<Teams> teams;
}
