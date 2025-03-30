package com.project.taskipro.repository;

import com.project.taskipro.model.user.SubscriptionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionTypeEntity, Long> {
}
