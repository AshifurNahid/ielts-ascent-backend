CREATE TABLE IF NOT EXISTS vocabulary_word (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    word VARCHAR(255) NOT NULL UNIQUE,
    simple_meaning VARCHAR(1000) NOT NULL,
    detailed_meaning VARCHAR(4000),
    part_of_speech VARCHAR(50) NOT NULL,
    level VARCHAR(20) NOT NULL,
    ielts_band_min DOUBLE PRECISION NOT NULL,
    ielts_band_max DOUBLE PRECISION NOT NULL,
    common_mistake VARCHAR(2000),
    premium BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT'
);

CREATE TABLE IF NOT EXISTS vocabulary_example (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    vocabulary_word_id UUID NOT NULL,
    type VARCHAR(30) NOT NULL,
    sentence VARCHAR(2000) NOT NULL,
    ai_generated BOOLEAN NOT NULL DEFAULT FALSE,
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_vocabulary_example_word FOREIGN KEY (vocabulary_word_id) REFERENCES vocabulary_word(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vocabulary_word_tag (
    vocabulary_word_id UUID NOT NULL,
    tag VARCHAR(100) NOT NULL,
    CONSTRAINT fk_vocabulary_word_tag_word FOREIGN KEY (vocabulary_word_id) REFERENCES vocabulary_word(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vocabulary_word_synonym (
    vocabulary_word_id UUID NOT NULL,
    value VARCHAR(255) NOT NULL,
    CONSTRAINT fk_vocabulary_word_synonym_word FOREIGN KEY (vocabulary_word_id) REFERENCES vocabulary_word(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vocabulary_word_antonym (
    vocabulary_word_id UUID NOT NULL,
    value VARCHAR(255) NOT NULL,
    CONSTRAINT fk_vocabulary_word_antonym_word FOREIGN KEY (vocabulary_word_id) REFERENCES vocabulary_word(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vocabulary_word_collocation (
    vocabulary_word_id UUID NOT NULL,
    value VARCHAR(255) NOT NULL,
    CONSTRAINT fk_vocabulary_word_collocation_word FOREIGN KEY (vocabulary_word_id) REFERENCES vocabulary_word(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_language_profile (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL UNIQUE,
    english_level VARCHAR(20) NOT NULL,
    current_ielts_band DOUBLE PRECISION,
    target_ielts_band DOUBLE PRECISION,
    premium_user BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_user_language_profile_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_language_profile_weak_tag (
    profile_id UUID NOT NULL,
    tag VARCHAR(100) NOT NULL,
    CONSTRAINT fk_user_language_profile_weak_tag FOREIGN KEY (profile_id) REFERENCES user_language_profile(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_vocabulary_progress (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    vocabulary_word_id UUID NOT NULL,
    exposure_count INTEGER NOT NULL DEFAULT 0,
    correct_count INTEGER NOT NULL DEFAULT 0,
    wrong_count INTEGER NOT NULL DEFAULT 0,
    mastery_level DOUBLE PRECISION NOT NULL DEFAULT 0,
    last_practiced_at TIMESTAMP,
    next_recommended_at TIMESTAMP,
    marked_difficult BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_user_vocabulary_progress_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_vocabulary_progress_word FOREIGN KEY (vocabulary_word_id) REFERENCES vocabulary_word(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_word_progress UNIQUE (user_id, vocabulary_word_id)
);

CREATE TABLE IF NOT EXISTS vocabulary_practice_attempt (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    vocabulary_word_id UUID NOT NULL,
    question_type VARCHAR(30) NOT NULL,
    user_answer VARCHAR(2000),
    correct BOOLEAN NOT NULL,
    CONSTRAINT fk_vocabulary_practice_attempt_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_vocabulary_practice_attempt_word FOREIGN KEY (vocabulary_word_id) REFERENCES vocabulary_word(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_vocabulary_word_status_level ON vocabulary_word(status, level);
CREATE INDEX IF NOT EXISTS idx_vocabulary_word_band ON vocabulary_word(ielts_band_min, ielts_band_max);
CREATE INDEX IF NOT EXISTS idx_user_vocabulary_progress_user ON user_vocabulary_progress(user_id);
