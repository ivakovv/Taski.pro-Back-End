-- Add indexes and constraints on Tokens
ALTER TABLE tokens ALTER COLUMN access_token SET NOT NULL;
ALTER TABLE tokens ALTER COLUMN refresh_token SET NOT NULL;
ALTER TABLE tokens ALTER COLUMN is_logged_out SET NOT NULL;

CREATE INDEX tokens_access_token_hidx ON tokens (access_token);
CREATE INDEX tokens_refresh_token_hidx ON tokens (refresh_token);
CREATE INDEX tokens_user_id_hidx ON tokens (user_id);