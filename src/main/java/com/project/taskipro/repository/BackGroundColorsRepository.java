package com.project.taskipro.repository;


import com.project.taskipro.model.user.BackGroundColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BackGroundColorsRepository extends JpaRepository<BackGroundColor, Long> {

}
