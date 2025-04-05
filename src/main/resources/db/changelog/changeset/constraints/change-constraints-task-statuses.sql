-- Add indexes and constraints on TaskStatuses
ALTER TABLE task_statuses
    ADD CONSTRAINT ck_task_statuses_status_type
        CHECK (status_type IN ('BACKLOG', 'INWORK', 'REVIEW', 'TESTING', 'COMPLETED'));

CREATE INDEX task_statuses_task_id_hidx ON task_statuses (task_id);
CREATE INDEX task_statuses_status_type_hidx ON task_statuses (status_type);