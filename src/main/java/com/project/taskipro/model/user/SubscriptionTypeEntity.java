package com.project.taskipro.model.user;

import com.project.taskipro.model.user.enums.SubscriptionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription_type")
public class SubscriptionTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type")
    private SubscriptionType subscriptionType;

    @Column(name = "desk_limit", nullable = false)
    private int deskLimit;

    @Column(name = "days_limit", nullable = false)
    private int daysLimit;

    @Column(name = "price", nullable = false)
    private double price;

    @OneToMany(mappedBy = "subscriptionTypeEntity")
    private List<UserSubscription> userSubscription;
}
