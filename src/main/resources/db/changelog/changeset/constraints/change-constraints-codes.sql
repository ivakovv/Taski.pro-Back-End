-- Add indexes on Codes
CREATE UNIQUE INDEX codes_code_hidx ON codes (code);

CREATE INDEX codes_code_type_expire_time_hidx ON codes (code_type, code_expire_time);

CREATE INDEX codes_user_id_hidx ON codes (user_id);