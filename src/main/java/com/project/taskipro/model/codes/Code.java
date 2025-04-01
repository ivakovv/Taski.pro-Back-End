package com.project.taskipro.model.codes;

import com.project.taskipro.model.user.User;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="codes")
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "code_expire_time")
    private LocalDateTime codeExpireTime;

    @Column(name = "code_type")
    @Enumerated(EnumType.STRING)
    private CodeType codeType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
