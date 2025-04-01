package com.project.taskipro.repository;

import com.project.taskipro.model.user.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    @Query("SELECT us FROM UserSubscription us " +
            "WHERE us.user.id = :userId " +
            "AND us.userSubscriptionFinishDate > CURRENT_TIMESTAMP " +
            "AND us.userSubscriptionStartDate < CURRENT_TIMESTAMP")
    Optional<UserSubscription> findActiveSubscriptionByUserId(@Param("userId") Long userId);
}
