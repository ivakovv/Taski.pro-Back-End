-- Add indexes and constraints on UserRights
CREATE INDEX user_rights_desk_id_user_id_hidx ON user_rights (desk_id, user_id);

ALTER TABLE user_rights
    ADD CONSTRAINT ck_user_rights_right_type
        CHECK (right_type IN ('CREATOR', 'CONTRIBUTOR', 'MEMBER'));