package com.project.taskipro.model.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubscriptionType {
    COMMON("COMMON", 3, 30, 10),
    PRO("PRO", 7, 30, 20),
    ULTRA("ULTRA", 15, 30, 35);

    private final String subscriptionTypeName;
    private final int subscriptionTypeDeskLimit;
    private final int subscriptionTypeDaysLimit;
    private final double subscriptionTypePrice;
}
