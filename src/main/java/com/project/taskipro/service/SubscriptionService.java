package com.project.taskipro.service;

import com.project.taskipro.dto.mapper.subscription.MapperToSubscriptionResponse;
import com.project.taskipro.dto.subscription.SubscriptionResponseDto;
import com.project.taskipro.model.user.User;
import com.project.taskipro.model.user.UserSubscription;
import com.project.taskipro.model.user.enums.SubscriptionType;
import com.project.taskipro.repository.SubscriptionTypeRepository;
import com.project.taskipro.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserServiceImpl userService;
    private final MapperToSubscriptionResponse mapperToSubscriptionResponse;

    public SubscriptionResponseDto getUserSubscription(){
        User user = userService.getCurrentUser();
        UserSubscription userSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("У пользователя с id: %d нет подписки", user.getId())));
        return mapperToSubscriptionResponse.mapToSubscriptionResponse(userSubscription, SubscriptionType.COMMON);
    }

}
