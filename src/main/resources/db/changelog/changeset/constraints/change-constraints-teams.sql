-- Add index on Teams
CREATE INDEX teams_desk_id_user_id_hidx ON teams (desk_id, user_id);