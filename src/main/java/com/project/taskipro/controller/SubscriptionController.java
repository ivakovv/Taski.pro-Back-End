package com.project.taskipro.controller;

import com.project.taskipro.dto.subscription.SubscriptionResponseDto;
import com.project.taskipro.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    @GetMapping()
    public ResponseEntity<SubscriptionResponseDto> getUserSubscription(){
        return ResponseEntity.ok(subscriptionService.getUserSubscription());
    }
}
