package com.project.taskipro.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_subscription", indexes = {
        @Index(name = "user_subscription_subscription_start_date_hidx", columnList = "subscription_start_date"),
        @Index(name = "user_subscription_user_id_subscription_type_id_hidx", columnList = "user_id, subscription_type_id")
})
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

    //Почему не LocalDate??
    @Column(name = "subscription_start_date", nullable = false)
    @PrePersist
    public void setSubscriptionStartDate(){
        if (userSubscriptionStartDate == null){
            userSubscriptionStartDate = new Date();
            //userSubscriptionStartDate = LocalDate.now();
        }
    }
    private Date userSubscriptionStartDate;
}
