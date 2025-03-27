package com.project.taskipro.model.desks;

import com.project.taskipro.model.user.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teams", indexes = {
        @Index(name = "teams_desk_id_user_id_hidx", columnList = "desk_id, user_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desks desk;
} 