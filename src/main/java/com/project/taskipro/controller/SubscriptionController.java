package com.project.taskipro.controller;

import com.project.taskipro.dto.subscription.SubscriptionInfoDto;
import com.project.taskipro.dto.subscription.UserSubscriptionResponseDto;
import com.project.taskipro.model.user.enums.SubscriptionType;
import com.project.taskipro.service.SubscriptionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о подписке получена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не имеет подписок")
    })
    public ResponseEntity<UserSubscriptionResponseDto> getUserSubscription(){
        return ResponseEntity.ok(subscriptionService.getUserSubscription());
    }

    @PostMapping("/{type}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подписка получена"),
            @ApiResponse(responseCode = "403", description = "У данного пользователя уже есть подписка"),
            @ApiResponse(responseCode = "404", description = "Тип подписки не найден")
    })
    public ResponseEntity<Void> createUserSubscription(@PathVariable(name = "type") SubscriptionType subscriptionType){
        subscriptionService.createUserSubscription(subscriptionType);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подписка успешно отменена"),
            @ApiResponse(responseCode = "404", description = "Активная подписка не найдена")
    })
    public ResponseEntity<Void> cancelSubscription() {
        subscriptionService.cancelSubscription();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{type}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подписка успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Активная подписка не найдена")
    })
    public ResponseEntity<Void> updateSubscription(@PathVariable("type") SubscriptionType subscriptionType) {
        subscriptionService.updateSubscription(subscriptionType);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список возможных подписок получен")
    })
    public ResponseEntity<List<SubscriptionInfoDto>> getAllSubscriptionEntities() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptionEntities());
    }
}
