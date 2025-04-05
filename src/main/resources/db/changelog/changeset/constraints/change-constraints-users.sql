-- Add indexes and constraints on Users
ALTER TABLE users ALTER COLUMN firstname SET NOT NULL;
ALTER TABLE users ALTER COLUMN lastname SET NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT ck_users_username
        CHECK (LENGTH(username) BETWEEN 4 AND 25),

    ADD CONSTRAINT ck_users_firstname
        CHECK (firstname ~ '^[A-ZА-ЯЁ][a-zа-яё]+$' AND (LENGTH(firstname) BETWEEN 1 AND 25)),

    ADD CONSTRAINT ck_users_lastname
        CHECK (lastname ~ '^[A-ZА-ЯЁ][a-zа-яё]+$' AND (LENGTH(lastname) BETWEEN 1 AND 25)),

    ADD CONSTRAINT uk_users_email UNIQUE (email),

    ADD CONSTRAINT ck_users_email
        CHECK (LENGTH(email) BETWEEN 9 AND 50),

    ADD CONSTRAINT ck_users_password
        CHECK (LENGTH(password) BETWEEN 15 AND 20),

    ADD CONSTRAINT ck_users_role_type
        CHECK (role_type IN ('FRONTEND', 'BACKEND', 'FULLSTACK', 'MANAGER', 'DESIGNER'));

CREATE INDEX users_email_hidx ON users (email);
CREATE INDEX users_username_hidx ON users (username);