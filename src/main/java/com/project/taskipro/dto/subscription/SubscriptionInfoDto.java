package com.project.taskipro.dto.subscription;

import com.project.taskipro.model.user.enums.SubscriptionType;

public record SubscriptionInfoDto(SubscriptionType subscriptionType, Integer deskLimit, Integer daysLimit, Double price) {
}
