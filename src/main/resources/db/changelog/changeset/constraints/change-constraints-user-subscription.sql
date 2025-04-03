-- Add indexes on UserSubscription
CREATE INDEX user_subscription_subscription_start_date_hidx
    ON user_subscription (subscription_start_date);

CREATE INDEX user_subscription_user_id_subscription_type_id_hidx
    ON user_subscription (user_id, subscription_type_id);