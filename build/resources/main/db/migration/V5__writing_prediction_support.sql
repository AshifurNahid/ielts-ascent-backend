ALTER TABLE writing_evaluation_result
    ADD COLUMN IF NOT EXISTS word_count_too_low BOOLEAN,
    ADD COLUMN IF NOT EXISTS off_topic BOOLEAN,
    ADD COLUMN IF NOT EXISTS mistakes TEXT,
    ADD COLUMN IF NOT EXISTS rewrite_exercises TEXT,
    ADD COLUMN IF NOT EXISTS improvement_tips TEXT;

CREATE TABLE IF NOT EXISTS writing_user_stats (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    task_type VARCHAR(255),
    submission_count INTEGER,
    average_overall_band DOUBLE PRECISION,
    last_overall_band DOUBLE PRECISION,
    last_evaluated_at TIMESTAMP,
    CONSTRAINT fk_writing_stats_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS writing_mistake_history (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    evaluation_result_id UUID,
    category VARCHAR(255),
    description VARCHAR(2000),
    occurred_at TIMESTAMP,
    CONSTRAINT fk_writing_mistake_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_writing_mistake_eval FOREIGN KEY (evaluation_result_id) REFERENCES writing_evaluation_result(id)
);
