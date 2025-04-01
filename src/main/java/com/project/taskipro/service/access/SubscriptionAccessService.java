package com.project.taskipro.service.access;

import com.project.taskipro.model.user.User;
import com.project.taskipro.model.user.UserSubscription;
import com.project.taskipro.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscriptionAccessService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final int DEFAULT_LIMIT_OF_USERS = 2;

    public int getLimitOfUsersOnDesk(User user){
        Optional<UserSubscription> creatorSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(user.getId());
        return creatorSubscription.map(userSubscription -> userSubscription.getSubscriptionTypeEntity().getDeskLimit()).orElse(DEFAULT_LIMIT_OF_USERS);
    }
}
