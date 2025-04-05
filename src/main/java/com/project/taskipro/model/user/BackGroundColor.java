package com.project.taskipro.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "background_colors")
public class BackGroundColor {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "color_code")
    private String colorCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}