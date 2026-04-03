CREATE TABLE IF NOT EXISTS reading_passage_v2 (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    short_description VARCHAR(1000),
    topic_tag VARCHAR(255) NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    ielts_band_min DOUBLE PRECISION NOT NULL,
    ielts_band_max DOUBLE PRECISION NOT NULL,
    estimated_reading_minutes INTEGER NOT NULL,
    premium BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS reading_question_v2 (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    passage_id UUID NOT NULL REFERENCES reading_passage_v2(id),
    group_number INTEGER,
    type VARCHAR(50) NOT NULL,
    prompt TEXT NOT NULL,
    options TEXT,
    correct_answer TEXT NOT NULL,
    explanation TEXT,
    answer_source_hint VARCHAR(1000),
    difficulty VARCHAR(50) NOT NULL,
    order_index INTEGER NOT NULL,
    premium BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(50) NOT NULL,
    tags TEXT
);

CREATE TABLE IF NOT EXISTS reading_test (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    total_time_minutes INTEGER NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    premium BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS reading_test_passage (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    reading_test_id UUID NOT NULL REFERENCES reading_test(id),
    passage_id UUID NOT NULL REFERENCES reading_passage_v2(id),
    order_index INTEGER NOT NULL,
    UNIQUE(reading_test_id, passage_id)
);

CREATE TABLE IF NOT EXISTS user_reading_profile (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL UNIQUE REFERENCES app_user(id),
    english_level VARCHAR(50) NOT NULL,
    current_ielts_band DOUBLE PRECISION NOT NULL,
    target_ielts_band DOUBLE PRECISION NOT NULL,
    premium_user BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS user_reading_skill_progress (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES app_user(id),
    skill_type VARCHAR(50) NOT NULL,
    skill_key VARCHAR(255) NOT NULL,
    total_attempts INTEGER NOT NULL,
    total_correct INTEGER NOT NULL,
    recent_attempts INTEGER NOT NULL,
    recent_correct INTEGER NOT NULL,
    average_time_seconds DOUBLE PRECISION NOT NULL,
    mastery_score DOUBLE PRECISION NOT NULL,
    weak_score DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    last_practiced_at TIMESTAMP,
    last_improved_at TIMESTAMP,
    UNIQUE(user_id, skill_type, skill_key)
);

CREATE TABLE IF NOT EXISTS reading_attempt (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES app_user(id),
    reading_test_id UUID,
    passage_id UUID,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    total_questions INTEGER NOT NULL,
    correct_answers INTEGER NOT NULL,
    score_percent DOUBLE PRECISION NOT NULL,
    total_time_seconds INTEGER NOT NULL,
    mode VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS reading_question_attempt (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    reading_attempt_id UUID NOT NULL REFERENCES reading_attempt(id),
    reading_question_id UUID NOT NULL REFERENCES reading_question_v2(id),
    user_id UUID NOT NULL REFERENCES app_user(id),
    user_answer TEXT,
    correct BOOLEAN NOT NULL,
    time_spent_seconds INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS reading_recommendation_log (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES app_user(id),
    recommendation_type VARCHAR(100) NOT NULL,
    reason TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_reading_question_v2_passage ON reading_question_v2(passage_id, status, order_index);
CREATE INDEX IF NOT EXISTS idx_reading_test_status ON reading_test(status, premium);
CREATE INDEX IF NOT EXISTS idx_user_reading_skill_progress_user ON user_reading_skill_progress(user_id, status, weak_score DESC);
CREATE INDEX IF NOT EXISTS idx_reading_attempt_user_created ON reading_attempt(user_id, created_at DESC);
