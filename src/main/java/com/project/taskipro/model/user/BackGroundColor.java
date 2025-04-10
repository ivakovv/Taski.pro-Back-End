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

    @Column(name = "bg_theme")
    private Integer bgTheme;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}