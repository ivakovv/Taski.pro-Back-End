package com.project.taskipro.model.user;

import java.util.Collection;
import java.util.List;

import com.project.taskipro.model.user.email.ValidEmail;
import com.project.taskipro.model.user.email.EmailDomains;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.taskipro.model.auth.ResetCodes;
import com.project.taskipro.model.auth.Token;
import com.project.taskipro.model.desks.Teams;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.model.tasks.Tasks;
import com.project.taskipro.model.user.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users", indexes = {
        @Index(name = "users_email_hidx", columnList = "email"),
        @Index(name = "users_username_hidx", columnList = "username")
})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 25)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String username;

    @Column(name = "firstname", nullable = false, length = 25)
    @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]+$")
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 25)
    @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]+$")
    private String lastname;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    @ValidEmail
    private String email;

    @Column(name = "password", nullable = false)
    @Min(15)
    @Max(20)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "user")
    private List<UserSubscription> subscriptions;

    @OneToMany(mappedBy = "user")
    private List<ResetCodes> resetCodes;

    @OneToMany(mappedBy = "user")
    private List<UserRights> userRights;
    
    @OneToMany(mappedBy = "user")
    private List<Teams> teams;

    @OneToMany(mappedBy = "user")
    private List<Tasks> tasks;

    @Column(name = "role_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
