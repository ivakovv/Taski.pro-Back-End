package com.project.taskipro.repository;

import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRightsRepository extends JpaRepository<UserRights, Long> {
    Optional<UserRights> findByDeskAndUser(Desks desks, User user);
    List<UserRights> findUsersByDeskId(Long deskId);
    List<UserRights> findByUser(User user);

    @Query("SELECT COUNT(ur) FROM UserRights ur WHERE ur.desk.id = :deskId")
    int countByDeskId(@Param("deskId") Long deskId);

    @Query("SELECT ur.user FROM UserRights ur WHERE ur.desk.id = :deskId AND ur.rightType = 'CREATOR'")
    Optional<User> findCreatorByDeskId(@Param("deskId") Long deskId);

    @Query("SELECT ur FROM UserRights ur WHERE ur.user = :user AND ur.rightType = 'CREATOR'")
    List<UserRights> findCreatedDesksForUser(@Param("user") User user);
}
