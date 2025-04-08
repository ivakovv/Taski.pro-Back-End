package com.project.taskipro.service;

import com.project.taskipro.dto.mapper.subscription.MapperToSubscriptionInfoDto;
import com.project.taskipro.dto.mapper.subscription.MapperToUserSubscriptionResponse;
import com.project.taskipro.dto.subscription.SubscriptionInfoDto;
import com.project.taskipro.dto.subscription.UserSubscriptionResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.model.user.SubscriptionTypeEntity;
import com.project.taskipro.model.user.User;
import com.project.taskipro.model.user.UserSubscription;
import com.project.taskipro.model.user.enums.SubscriptionType;
import com.project.taskipro.repository.DeskRepository;
import com.project.taskipro.repository.SubscriptionTypeRepository;
import com.project.taskipro.repository.UserRightsRepository;
import com.project.taskipro.repository.UserSubscriptionRepository;
import com.project.taskipro.service.access.SubscriptionAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final DeskRepository deskRepository;
    private final UserRightsRepository userRightsRepository;
    private final UserServiceImpl userService;
    private final SubscriptionAccessService subscriptionAccessService;
    private final MapperToUserSubscriptionResponse mapperToSubscriptionResponse;
    private final MapperToSubscriptionInfoDto mapperToSubscriptionInfoDto;

    public UserSubscriptionResponseDto getUserSubscription(){
        User currentUser = userService.getCurrentUser();
        UserSubscription userSubscription = getUserSubscription(currentUser.getId());
        return mapperToSubscriptionResponse.mapToUserSubscriptionResponse(userSubscription);
    }

    public void createUserSubscription(SubscriptionType subscriptionType){
        User currentUser = userService.getCurrentUser();
        Optional<UserSubscription> userSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(currentUser.getId());
        if(userSubscription.isPresent()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Пользователь %s уже имеет подписку", currentUser.getUsername()));
        }
        UserSubscription subscription = UserSubscription.builder()
                .user(currentUser)
                .subscriptionTypeEntity(getSubscriptionEntity(subscriptionType))
                .userSubscriptionStartDate(LocalDateTime.now())
                .userSubscriptionFinishDate(LocalDateTime.now().plusDays(30))
                .build();
        userSubscriptionRepository.save(subscription);
        updateAllDesksUserLimit();
    }

    public void cancelSubscription(){
        User currentUser = userService.getCurrentUser();
        UserSubscription userSubscription = getUserSubscription(currentUser.getId());
        userSubscription.setUserSubscriptionFinishDate(LocalDateTime.now());
        userSubscriptionRepository.save(userSubscription);
        updateAllDesksUserLimit();
    }

    public void updateSubscription(SubscriptionType subscriptionType){
        User currentUser = userService.getCurrentUser();
        UserSubscription userSubscription = getUserSubscription(currentUser.getId());
        if(isUpgrade(userSubscription.getSubscriptionTypeEntity().getSubscriptionType(), subscriptionType)){
            upgradeSubscription(userSubscription, subscriptionType);
        } else {
            downgradeOrKeepSubscription(userSubscription, subscriptionType);
        }
        updateAllDesksUserLimit();
    }

    public List<SubscriptionInfoDto> getAllSubscriptionEntities(){
        List<SubscriptionTypeEntity> subscriptionTypeEntities = subscriptionTypeRepository.findAll();
        return subscriptionTypeEntities.stream()
                .map(mapperToSubscriptionInfoDto::mapToSubscriptionInfoDto)
                .collect(Collectors.toList());
    }

    private SubscriptionTypeEntity getSubscriptionEntity(SubscriptionType subscriptionType){
        return subscriptionTypeRepository.findBySubscriptionType(subscriptionType).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Подписка типа %s не найдена", subscriptionType)));
    }

    private UserSubscription getUserSubscription(Long userId){
        return userSubscriptionRepository.findActiveSubscriptionByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("У пользователя с id: %d нет подписок", userId)));
    }

    private void upgradeSubscription(UserSubscription currentSubscription, SubscriptionType subscriptionType){
        System.out.println(subscriptionType);
        currentSubscription.setUserSubscriptionFinishDate(LocalDateTime.now());
        userSubscriptionRepository.save(currentSubscription);
        UserSubscription newUserSubscription = UserSubscription
                .builder()
                .user(userService.getCurrentUser())
                .subscriptionTypeEntity(getSubscriptionEntity(subscriptionType))
                .userSubscriptionStartDate(LocalDateTime.now())
                .userSubscriptionFinishDate(LocalDateTime.now().plusDays(30))
                .build();
        userSubscriptionRepository.save(newUserSubscription);
    }

    private void downgradeOrKeepSubscription(UserSubscription userSubscription, SubscriptionType subscriptionType){
        UserSubscription newUserSubscription = UserSubscription
                .builder()
                .user(userService.getCurrentUser())
                .subscriptionTypeEntity(getSubscriptionEntity(subscriptionType))
                .userSubscriptionStartDate(userSubscription.getUserSubscriptionFinishDate())
                .userSubscriptionFinishDate(userSubscription.getUserSubscriptionFinishDate().plusDays(30))
                .build();
        userSubscriptionRepository.save(newUserSubscription);
    }

    public boolean isUpgrade(SubscriptionType currentType, SubscriptionType targetType) {
        return getSubscriptionValue(currentType) < getSubscriptionValue(targetType);
    }

    private void updateAllDesksUserLimit(){
        User user = userService.getCurrentUser();
        List<UserRights> userRights = userRightsRepository.findCreatedDesksForUser(user);
        userRights.forEach(ur -> {
            Desks desk = ur.getDesk();
            desk.setUserLimit(subscriptionAccessService.getLimitOfUsersOnDesk(user));
            deskRepository.save(desk);
        });
    }

    private int getSubscriptionValue(SubscriptionType subscriptionType) {
        return switch (subscriptionType) {
            case COMMON -> 1;
            case PRO -> 2;
            case ULTRA -> 3;
        };
    }
}
