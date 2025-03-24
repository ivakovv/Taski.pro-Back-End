package com.project.taskipro.repository;

import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeskRepository extends JpaRepository<Desks, Long> {

    Optional<Desks> findByDeskName(String deskName);

    @Query("SELECT d FROM Desks d JOIN d.userRights ur WHERE ur.user = :user")
    List<Desks> findDesksByUser(@Param("user") User user);
}
