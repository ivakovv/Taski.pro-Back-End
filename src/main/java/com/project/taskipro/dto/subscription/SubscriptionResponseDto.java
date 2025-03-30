package com.project.taskipro.dto.subscription;

import com.project.taskipro.model.user.enums.SubscriptionType;

import java.time.LocalDateTime;

public record SubscriptionResponseDto(SubscriptionType subscriptionType, LocalDateTime startDate, LocalDateTime finishDate) {
}
