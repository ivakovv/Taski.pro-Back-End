package com.project.taskipro.repository;

import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRightsRepository extends JpaRepository<UserRights, Long> {
    Optional<UserRights> findByDeskAndUser(Desks desks, User user);
    List<UserRights> findUsersByDeskId(Long deskId);
    List<UserRights> findByUser(User user);
}
