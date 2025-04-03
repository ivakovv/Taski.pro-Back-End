-- Add indexes and constraints on Desks
CREATE INDEX desks_desk_name_hidx ON desks (desk_name);

CREATE INDEX desks_create_date_hidx ON desks (create_date);

ALTER TABLE desks
    ADD CONSTRAINT ck_desks_desk_name CHECK (LENGTH(desk_name) <= 50),

    ALTER COLUMN desk_description DROP NOT NULL;

    ALTER TABLE desks ADD CONSTRAINT ck_desks_deks_description CHECK (LENGTH(desk_description) <= 200);

    ALTER TABLE desks ALTER COLUMN finish_date DROP NOT NULL;

    ALTER TABLE desks ADD CONSTRAINT ck_desks_user_limit CHECK (user_limit BETWEEN 1 AND 20);