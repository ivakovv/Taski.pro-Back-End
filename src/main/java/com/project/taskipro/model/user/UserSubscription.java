package com.project.taskipro.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_subscription")
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_subscription")
    private long idUserSubscription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_type_id")
    private SubscriptionTypeEntity subscriptionType;

    @Column(name = "subscription_start_date")
    private Date userSubscriptionStartDate;
}
