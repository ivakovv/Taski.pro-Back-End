package com.project.taskipro.dto.mapper.subscription;

import com.project.taskipro.dto.subscription.SubscriptionResponseDto;
import com.project.taskipro.model.user.UserSubscription;
import com.project.taskipro.model.user.enums.SubscriptionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToSubscriptionResponse {
    @Mapping(target = "subscriptionType", source = "subscriptionType")
    @Mapping(target = "startDate", source = "userSubscription.userSubscriptionStartDate")
    @Mapping(target = "finishDate", source = "userSubscription.userSubscriptionFinishDate")
    SubscriptionResponseDto mapToSubscriptionResponse(UserSubscription userSubscription, SubscriptionType subscriptionType);
}
