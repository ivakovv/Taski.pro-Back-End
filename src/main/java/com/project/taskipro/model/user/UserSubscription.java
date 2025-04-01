package com.project.taskipro.model.user;

import com.project.taskipro.model.audit.EntityAuditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_subscription")
public class UserSubscription implements EntityAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_subscription")
    private long idUserSubscription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_type_id")
    private SubscriptionTypeEntity subscriptionTypeEntity;

    @Column(name = "subscription_finish_date")
    private LocalDateTime userSubscriptionFinishDate;

    @Column(name = "subscription_start_date", nullable = false)
    private LocalDateTime userSubscriptionStartDate;

    @Override
    public void setCreationDate(LocalDateTime creationDate) {
        this.userSubscriptionStartDate = creationDate;
    }
}
