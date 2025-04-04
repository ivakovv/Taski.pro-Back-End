-- Add indexes and constraints on SubscriptionType
ALTER TABLE subscription_type
    ADD CONSTRAINT ck_subscription_type_subscription_type
        CHECK (subscription_type IN ('COMMON', 'PRO', 'ULTRA')),

    ADD CONSTRAINT ck_subscription_type_desk_limit
        CHECK (desk_limit BETWEEN 1 AND 50),

    ADD CONSTRAINT ck_subscription_type_days_limit
        CHECK (days_limit BETWEEN 1 AND 180),

    ADD CONSTRAINT ck_subscription_type_price
        CHECK (price >= 0 AND price <= 50000);

CREATE INDEX subscription_type_name_hidx ON subscription_type (subscription_type);