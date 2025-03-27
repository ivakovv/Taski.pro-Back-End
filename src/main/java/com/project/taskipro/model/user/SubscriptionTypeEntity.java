package com.project.taskipro.model.user;

import com.project.taskipro.model.user.enums.SubscriptionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription_type", indexes = {
        @Index(name = "subscription_type_name_hidx", columnList = "subscription_type")
})
public class SubscriptionTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false)
    private SubscriptionType subscriptionType;

    @Column(name = "desk_limit", nullable = false)
    @Min(1)
    @Max(50)
    private int deskLimit;

    @Column(name = "days_limit", nullable = false)
    @Min(1)
    @Max(180)
    private int daysLimit;

    @Column(name = "price", nullable = false)
    @Min(0)
    @Max(5000)
    private double price;

    @OneToMany(mappedBy = "subscriptionType")
    private List<UserSubscription> userSubscription;
}
