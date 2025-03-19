package com.project.taskipro.model.auth;

import com.project.taskipro.model.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="reset_codes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "reset_code")
    private String resetCode;

    @Column(name = "expire_time")
    private Date codeExpireTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
