package com.project.taskipro.model.desks;

import com.project.taskipro.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_rights")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "right_type")
    @Enumerated(EnumType.STRING)
    private RightType rightType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desks desk;
}
