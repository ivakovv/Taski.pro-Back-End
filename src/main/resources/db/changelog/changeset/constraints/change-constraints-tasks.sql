-- Add indexes and constraints on Tasks
ALTER TABLE tasks
    ADD CONSTRAINT ck_tasks_task_name
        CHECK (LENGTH(task_name) <= 75),

    ADD CONSTRAINT ck_tasks_task_description
        CHECK (LENGTH(task_description) <= 200),

    ADD CONSTRAINT ck_tasks_task_comment
        CHECK (LENGTH(task_comment) <= 200),

    ADD CONSTRAINT ck_tasks_priority_type
        CHECK (priority_type IN ('COMMON', 'FROZEN', 'LOW', 'MEDIUM', 'HIGH')),

    DROP COLUMN start_date;

CREATE INDEX tasks_desk_id_hidx ON tasks (desk_id);
CREATE INDEX tasks_user_id_hidx ON tasks (user_id);
CREATE INDEX tasks_priority_type_hidx ON tasks (priority_type);