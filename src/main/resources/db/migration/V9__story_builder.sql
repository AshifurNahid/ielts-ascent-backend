CREATE TABLE IF NOT EXISTS story_submission (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    topic TEXT NOT NULL,
    parts_json TEXT NOT NULL,
    word_count INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS story_evaluation (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    submission_id UUID NOT NULL UNIQUE,
    fluency_score DOUBLE PRECISION NOT NULL,
    coherence_score DOUBLE PRECISION NOT NULL,
    vocabulary_score DOUBLE PRECISION NOT NULL,
    grammar_score DOUBLE PRECISION NOT NULL,
    overall_band DOUBLE PRECISION NOT NULL,
    strengths_json TEXT NOT NULL,
    improvements_json TEXT NOT NULL,
    evaluated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_story_eval_submission FOREIGN KEY (submission_id) REFERENCES story_submission(id)
);

CREATE INDEX IF NOT EXISTS idx_story_submission_user_id ON story_submission(user_id);
