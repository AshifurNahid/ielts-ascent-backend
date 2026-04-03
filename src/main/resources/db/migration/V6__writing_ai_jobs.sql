ALTER TABLE writing_prompt
    ADD COLUMN IF NOT EXISTS category VARCHAR(255),
    ADD COLUMN IF NOT EXISTS question_text TEXT,
    ADD COLUMN IF NOT EXISTS chart_type VARCHAR(255),
    ADD COLUMN IF NOT EXISTS difficulty VARCHAR(255);

ALTER TABLE writing_submission
    ADD COLUMN IF NOT EXISTS task_type VARCHAR(255),
    ADD COLUMN IF NOT EXISTS word_count INTEGER,
    ADD COLUMN IF NOT EXISTS submitted_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS status VARCHAR(255);

ALTER TABLE writing_evaluation_result
    ADD COLUMN IF NOT EXISTS submission_id UUID UNIQUE,
    ADD COLUMN IF NOT EXISTS json_feedback JSONB,
    ADD COLUMN IF NOT EXISTS provider VARCHAR(255),
    ADD COLUMN IF NOT EXISTS model VARCHAR(255),
    ADD COLUMN IF NOT EXISTS prompt_version VARCHAR(255),
    ADD COLUMN IF NOT EXISTS evaluated_at TIMESTAMP;

ALTER TABLE writing_evaluation_result
    ALTER COLUMN mistakes TYPE JSONB USING CASE WHEN mistakes IS NULL THEN NULL ELSE mistakes::jsonb END,
    ALTER COLUMN rewrite_exercises TYPE JSONB USING CASE WHEN rewrite_exercises IS NULL THEN NULL ELSE rewrite_exercises::jsonb END,
    ALTER COLUMN improvement_tips TYPE JSONB USING CASE WHEN improvement_tips IS NULL THEN NULL ELSE improvement_tips::jsonb END;

CREATE TABLE IF NOT EXISTS ai_job (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    input_ref UUID NOT NULL,
    output_ref UUID,
    attempts INTEGER NOT NULL,
    error_message TEXT
);

CREATE TABLE IF NOT EXISTS ai_interaction_log (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    correlation_id VARCHAR(255),
    provider VARCHAR(255),
    model VARCHAR(255),
    prompt_name VARCHAR(255),
    prompt_version VARCHAR(255),
    latency_ms INTEGER,
    token_usage INTEGER,
    request_redacted TEXT,
    response_redacted TEXT
);

ALTER TABLE writing_evaluation_result
    ADD CONSTRAINT fk_writing_eval_submission
        FOREIGN KEY (submission_id) REFERENCES writing_submission(id);
