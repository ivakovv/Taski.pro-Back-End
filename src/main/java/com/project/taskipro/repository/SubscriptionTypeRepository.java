package com.project.taskipro.repository;

import com.project.taskipro.model.user.SubscriptionTypeEntity;
import com.project.taskipro.model.user.enums.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionTypeEntity, Long> {
    Optional<SubscriptionTypeEntity> findBySubscriptionType(SubscriptionType subscriptionType);
}
