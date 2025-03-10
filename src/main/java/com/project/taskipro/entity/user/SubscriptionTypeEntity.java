package com.project.taskipro.entity.user;

import com.project.taskipro.entity.user.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
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

    @Column(name = "desk_limit")
    private int deskLimit;

    @Column(name = "days_limit")
    private int daysLimit;

    @Column(name = "price")
    private double price;

    @OneToMany(mappedBy = "subscriptionType")
    private List<UserSubscription> userSubscription;
}
