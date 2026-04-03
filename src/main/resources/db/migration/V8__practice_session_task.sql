ALTER TABLE practice_session
    ADD COLUMN IF NOT EXISTS task_id UUID;

CREATE INDEX IF NOT EXISTS idx_practice_session_user_task
    ON practice_session (user_id, task_id);
