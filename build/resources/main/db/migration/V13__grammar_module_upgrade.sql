ALTER TABLE grammar_topic
    DROP COLUMN IF EXISTS lessons,
    DROP COLUMN IF EXISTS completed,
    DROP COLUMN IF EXISTS unlocked,
    ADD COLUMN IF NOT EXISTS level VARCHAR(20) NOT NULL DEFAULT 'BEGINNER',
    ADD COLUMN IF NOT EXISTS ielts_band_min DOUBLE PRECISION NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS ielts_band_max DOUBLE PRECISION NOT NULL DEFAULT 9,
    ADD COLUMN IF NOT EXISTS premium BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS unlocked_by_default BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    ADD COLUMN IF NOT EXISTS prerequisite_topic_id UUID;

CREATE TABLE IF NOT EXISTS grammar_lesson (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    topic_id UUID NOT NULL REFERENCES grammar_topic(id),
    title VARCHAR(255) NOT NULL,
    content VARCHAR(12000) NOT NULL,
    order_index INTEGER NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    premium BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    completed_by_default BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS grammar_lesson_example (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    lesson_id UUID NOT NULL REFERENCES grammar_lesson(id) ON DELETE CASCADE,
    correct_sentence VARCHAR(1500) NOT NULL,
    incorrect_sentence VARCHAR(1500),
    explanation VARCHAR(4000) NOT NULL,
    ai_generated BOOLEAN NOT NULL DEFAULT FALSE,
    approved BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS grammar_question (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    topic_id UUID NOT NULL REFERENCES grammar_topic(id),
    lesson_id UUID REFERENCES grammar_lesson(id) ON DELETE SET NULL,
    type VARCHAR(30) NOT NULL,
    question VARCHAR(4000) NOT NULL,
    correct_answer VARCHAR(1000) NOT NULL,
    explanation VARCHAR(4000),
    difficulty VARCHAR(20) NOT NULL,
    premium BOOLEAN NOT NULL DEFAULT FALSE,
    ielts_band_min DOUBLE PRECISION NOT NULL,
    ielts_band_max DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    ai_generated BOOLEAN NOT NULL DEFAULT FALSE,
    reviewed_by_admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS grammar_question_option (
    grammar_question_id UUID NOT NULL REFERENCES grammar_question(id) ON DELETE CASCADE,
    option_text VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS grammar_question_tag (
    grammar_question_id UUID NOT NULL REFERENCES grammar_question(id) ON DELETE CASCADE,
    tag VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS grammar_drill_template (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL,
    icon VARCHAR(255),
    estimated_time_minutes INTEGER NOT NULL,
    question_count INTEGER NOT NULL,
    type VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS user_grammar_profile (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    english_level VARCHAR(20) NOT NULL,
    current_ielts_band DOUBLE PRECISION,
    target_ielts_band DOUBLE PRECISION NOT NULL,
    premium_user BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS user_grammar_weak_topic (
    profile_id UUID NOT NULL REFERENCES user_grammar_profile(id) ON DELETE CASCADE,
    topic_title VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_grammar_weak_drill_type (
    profile_id UUID NOT NULL REFERENCES user_grammar_profile(id) ON DELETE CASCADE,
    drill_type VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_grammar_progress (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    topic_id UUID NOT NULL REFERENCES grammar_topic(id) ON DELETE CASCADE,
    completed_lessons INTEGER NOT NULL DEFAULT 0,
    total_lessons INTEGER NOT NULL DEFAULT 0,
    mastery_score DOUBLE PRECISION NOT NULL DEFAULT 0,
    last_practiced_at TIMESTAMP,
    unlocked BOOLEAN NOT NULL DEFAULT TRUE,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (user_id, topic_id)
);

CREATE TABLE IF NOT EXISTS grammar_practice_attempt (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    grammar_question_id UUID NOT NULL REFERENCES grammar_question(id) ON DELETE CASCADE,
    topic_id UUID NOT NULL REFERENCES grammar_topic(id),
    lesson_id UUID REFERENCES grammar_lesson(id) ON DELETE SET NULL,
    question_type VARCHAR(30) NOT NULL,
    user_answer VARCHAR(2000),
    correct BOOLEAN NOT NULL
);

INSERT INTO grammar_drill_template (id, created_at, updated_at, name, icon, estimated_time_minutes, question_count, type, active)
SELECT '11111111-1111-1111-1111-111111111111', now(), now(), 'Error Correction Sprint', 'alert-circle', 10, 8, 'ERROR_CORRECTION', true
WHERE NOT EXISTS (SELECT 1 FROM grammar_drill_template WHERE type = 'ERROR_CORRECTION');

INSERT INTO grammar_drill_template (id, created_at, updated_at, name, icon, estimated_time_minutes, question_count, type, active)
SELECT '22222222-2222-2222-2222-222222222222', now(), now(), 'Fill Blank Builder', 'pen-line', 8, 10, 'FILL_BLANK', true
WHERE NOT EXISTS (SELECT 1 FROM grammar_drill_template WHERE type = 'FILL_BLANK');

INSERT INTO grammar_drill_template (id, created_at, updated_at, name, icon, estimated_time_minutes, question_count, type, active)
SELECT '33333333-3333-3333-3333-333333333333', now(), now(), 'Multiple Choice Mix', 'check-square', 7, 10, 'MULTIPLE_CHOICE', true
WHERE NOT EXISTS (SELECT 1 FROM grammar_drill_template WHERE type = 'MULTIPLE_CHOICE');

INSERT INTO grammar_drill_template (id, created_at, updated_at, name, icon, estimated_time_minutes, question_count, type, active)
SELECT '44444444-4444-4444-4444-444444444444', now(), now(), 'Transformation Intensive', 'refresh-cw', 12, 6, 'TRANSFORMATION', true
WHERE NOT EXISTS (SELECT 1 FROM grammar_drill_template WHERE type = 'TRANSFORMATION');
