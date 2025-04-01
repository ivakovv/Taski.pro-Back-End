package com.project.taskipro.dto.mapper.subscription;

import com.project.taskipro.dto.subscription.SubscriptionInfoDto;
import com.project.taskipro.model.user.SubscriptionTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToSubscriptionInfoDto {
    @Mapping(target = "subscriptionType", source = "subscriptionType")
    @Mapping(target = "deskLimit", source = "deskLimit")
    @Mapping(target = "daysLimit", source = "daysLimit")
    @Mapping(target = "price", source = "price")
    SubscriptionInfoDto mapToSubscriptionInfoDto(SubscriptionTypeEntity subscriptionTypeEntity);
}
