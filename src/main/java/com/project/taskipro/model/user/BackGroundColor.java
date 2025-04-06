package com.project.taskipro.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "background_colors")
public class BackGroundColor {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "theme")
    private String theme;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}