package com.project.taskipro.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="password_reset")
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reset_code")
    private String resetCode;

    @Column(name = "reset_code_expire_time")
    private Calendar resetCodeExpireTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
