CREATE TABLE IF NOT EXISTS writing_prompt (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    task_type VARCHAR(32) NOT NULL,
    prompt_text TEXT NOT NULL,
    instructions TEXT,
    tags TEXT,
    difficulty VARCHAR(32) NOT NULL,
    ielts_band_min NUMERIC(3,1) NOT NULL,
    ielts_band_max NUMERIC(3,1) NOT NULL,
    premium BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(32) NOT NULL,
    template_enabled BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS writing_template (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    prompt_id UUID NULL REFERENCES writing_prompt(id),
    task_type VARCHAR(32) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    template_content TEXT NOT NULL,
    target_band NUMERIC(3,1),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS writing_submission (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES "user"(id),
    prompt_id UUID NULL REFERENCES writing_prompt(id),
    task_type VARCHAR(32) NOT NULL,
    essay_text TEXT NOT NULL,
    word_count INTEGER NOT NULL DEFAULT 0,
    timed_mode BOOLEAN NOT NULL DEFAULT FALSE,
    started_at TIMESTAMP,
    submitted_at TIMESTAMP,
    duration_seconds BIGINT,
    status VARCHAR(40) NOT NULL,
    overall_band NUMERIC(3,1),
    task_response_band NUMERIC(3,1),
    coherence_band NUMERIC(3,1),
    lexical_band NUMERIC(3,1),
    grammar_band NUMERIC(3,1),
    evaluation_confidence NUMERIC(4,3),
    ai_summary TEXT
);

CREATE TABLE IF NOT EXISTS writing_weak_point (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    submission_id UUID NOT NULL REFERENCES writing_submission(id),
    user_id UUID NOT NULL REFERENCES "user"(id),
    category VARCHAR(64) NOT NULL,
    weak_key VARCHAR(255) NOT NULL,
    severity VARCHAR(32) NOT NULL,
    explanation TEXT,
    suggestion TEXT
);

CREATE TABLE IF NOT EXISTS writing_suggestion (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    submission_id UUID NOT NULL REFERENCES writing_submission(id),
    user_id UUID NOT NULL REFERENCES "user"(id),
    suggestion_type VARCHAR(64) NOT NULL,
    original_text TEXT,
    suggested_text TEXT,
    explanation TEXT
);

CREATE TABLE IF NOT EXISTS user_writing_profile (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL UNIQUE REFERENCES "user"(id),
    current_estimated_writing_band NUMERIC(3,1),
    target_writing_band NUMERIC(3,1),
    weak_categories TEXT,
    last_submission_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_writing_progress (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL UNIQUE REFERENCES "user"(id),
    total_submissions BIGINT NOT NULL DEFAULT 0,
    average_band NUMERIC(3,1),
    latest_band NUMERIC(3,1),
    best_band NUMERIC(3,1),
    trend_value NUMERIC(4,2),
    last30_day_improvement NUMERIC(4,2)
);

CREATE INDEX IF NOT EXISTS idx_writing_prompt_status_task ON writing_prompt(status, task_type);
CREATE INDEX IF NOT EXISTS idx_writing_submission_user_status ON writing_submission(user_id, status, submitted_at DESC);
CREATE INDEX IF NOT EXISTS idx_writing_weak_point_user ON writing_weak_point(user_id, created_at DESC);
