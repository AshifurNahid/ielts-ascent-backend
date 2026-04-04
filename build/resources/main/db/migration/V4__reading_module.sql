CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS reading_skill_trainer (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    icon VARCHAR(255),
    total_sessions INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS reading_question_type (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS reading_practice_session (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    trainer_id UUID,
    question_type_id UUID,
    total_questions INTEGER NOT NULL,
    correct_answers INTEGER NOT NULL,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT fk_reading_session_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_reading_session_trainer FOREIGN KEY (trainer_id) REFERENCES reading_skill_trainer(id),
    CONSTRAINT fk_reading_session_question_type FOREIGN KEY (question_type_id) REFERENCES reading_question_type(id)
);

CREATE TABLE IF NOT EXISTS reading_user_stats (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    trainer_id UUID,
    question_type_id UUID,
    practiced_count INTEGER NOT NULL,
    accuracy_percentage INTEGER NOT NULL,
    CONSTRAINT fk_reading_stats_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_reading_stats_trainer FOREIGN KEY (trainer_id) REFERENCES reading_skill_trainer(id),
    CONSTRAINT fk_reading_stats_question_type FOREIGN KEY (question_type_id) REFERENCES reading_question_type(id)
);

CREATE TABLE IF NOT EXISTS reading_improvement_history (
    id UUID PRIMARY KEY,
    recorded_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    trainer_id UUID NOT NULL,
    accuracy_before INTEGER NOT NULL,
    accuracy_after INTEGER NOT NULL,
    CONSTRAINT fk_reading_improvement_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_reading_improvement_trainer FOREIGN KEY (trainer_id) REFERENCES reading_skill_trainer(id)
);

CREATE INDEX IF NOT EXISTS idx_reading_practice_session_user_trainer
    ON reading_practice_session (user_id, trainer_id);

CREATE INDEX IF NOT EXISTS idx_reading_practice_session_user_question_type
    ON reading_practice_session (user_id, question_type_id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_reading_skill_trainer_name
    ON reading_skill_trainer (name);

CREATE UNIQUE INDEX IF NOT EXISTS uq_reading_question_type_name
    ON reading_question_type (name);

CREATE UNIQUE INDEX IF NOT EXISTS uq_reading_user_stats_trainer
    ON reading_user_stats (user_id, trainer_id)
    WHERE trainer_id IS NOT NULL AND question_type_id IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_reading_user_stats_question_type
    ON reading_user_stats (user_id, question_type_id)
    WHERE question_type_id IS NOT NULL AND trainer_id IS NULL;

INSERT INTO reading_skill_trainer (id, created_at, updated_at, name, description, icon, total_sessions)
VALUES
    (gen_random_uuid(), NOW(), NOW(), 'TFNG Logic Trainer', 'Master True / False / Not Given', '🎯', 15),
    (gen_random_uuid(), NOW(), NOW(), 'Matching Headings', 'Practice paragraph matching', '🔗', 12),
    (gen_random_uuid(), NOW(), NOW(), 'Skimming Speed', 'Improve reading speed', '⚡', 8),
    (gen_random_uuid(), NOW(), NOW(), 'Scanning Drill', 'Find information quickly', '🔍', 10)
ON CONFLICT DO NOTHING;

INSERT INTO reading_question_type (id, created_at, updated_at, name)
VALUES
    (gen_random_uuid(), NOW(), NOW(), 'Multiple Choice'),
    (gen_random_uuid(), NOW(), NOW(), 'True / False / Not Given'),
    (gen_random_uuid(), NOW(), NOW(), 'Yes / No / Not Given'),
    (gen_random_uuid(), NOW(), NOW(), 'Matching Headings'),
    (gen_random_uuid(), NOW(), NOW(), 'Matching Features'),
    (gen_random_uuid(), NOW(), NOW(), 'Sentence Completion'),
    (gen_random_uuid(), NOW(), NOW(), 'Summary Completion'),
    (gen_random_uuid(), NOW(), NOW(), 'Diagram Label')
ON CONFLICT DO NOTHING;
