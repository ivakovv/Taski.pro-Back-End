package com.project.taskipro.model.desks;

import java.time.LocalDateTime;
import java.util.List;

import com.project.taskipro.model.audit.EntityAuditable;
import com.project.taskipro.model.tasks.Tasks;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="desks")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Desks implements EntityAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "desk_name")
    private String deskName;

    @Column(name = "desk_description")
    private String deskDescription;

    @Column(name = "create_date")
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

    @Override
    public void setCreationDate(LocalDateTime creationDate) {
        this.deskCreateDate = creationDate;
    }
}
