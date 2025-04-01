package com.project.taskipro.dto.mapper.subscription;

import com.project.taskipro.dto.subscription.UserSubscriptionResponseDto;
import com.project.taskipro.model.user.UserSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToUserSubscriptionResponse {
    @Mapping(target = "subscriptionType", source = "userSubscription.subscriptionTypeEntity.subscriptionType")
    @Mapping(target = "startDate", source = "userSubscriptionStartDate")
    @Mapping(target = "finishDate", source = "userSubscriptionFinishDate")
    UserSubscriptionResponseDto mapToUserSubscriptionResponse(UserSubscription userSubscription);
}
