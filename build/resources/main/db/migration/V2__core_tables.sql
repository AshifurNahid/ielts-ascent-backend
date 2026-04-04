CREATE TABLE IF NOT EXISTS reading_passage (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    passage_text VARCHAR(8000),
    difficulty VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS listening_audio (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    audio_url VARCHAR(2048),
    duration_seconds INTEGER
);

CREATE TABLE IF NOT EXISTS writing_prompt (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    task_type VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS speaking_prompt (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    part VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS vocabulary_item (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    definition VARCHAR(1000),
    example VARCHAR(2000),
    category VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS mini_lesson (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    lesson_body VARCHAR(4000)
);

CREATE TABLE IF NOT EXISTS question (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    prompt VARCHAR(2000) NOT NULL,
    type VARCHAR(255),
    reading_passage_id UUID,
    listening_audio_id UUID,
    CONSTRAINT fk_question_reading FOREIGN KEY (reading_passage_id) REFERENCES reading_passage(id),
    CONSTRAINT fk_question_listening FOREIGN KEY (listening_audio_id) REFERENCES listening_audio(id)
);

CREATE TABLE IF NOT EXISTS question_option (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    label VARCHAR(1000) NOT NULL,
    question_id UUID,
    CONSTRAINT fk_option_question FOREIGN KEY (question_id) REFERENCES question(id)
);

CREATE TABLE IF NOT EXISTS answer_key (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    question_id UUID,
    correct_answer VARCHAR(1000),
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES question(id)
);

CREATE TABLE IF NOT EXISTS diagnostic_session (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT fk_diag_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS diagnostic_result (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    session_id UUID,
    overall_band DOUBLE PRECISION,
    summary_id UUID,
    CONSTRAINT fk_result_session FOREIGN KEY (session_id) REFERENCES diagnostic_session(id)
);

CREATE TABLE IF NOT EXISTS micro_skill_score (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    diagnostic_result_id UUID,
    skill_name VARCHAR(255),
    score INTEGER,
    confidence INTEGER,
    CONSTRAINT fk_micro_result FOREIGN KEY (diagnostic_result_id) REFERENCES diagnostic_result(id)
);

CREATE TABLE IF NOT EXISTS study_plan (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    status VARCHAR(255),
    CONSTRAINT fk_plan_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS study_plan_week (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    study_plan_id UUID,
    week_start DATE,
    week_number INTEGER,
    CONSTRAINT fk_week_plan FOREIGN KEY (study_plan_id) REFERENCES study_plan(id)
);

CREATE TABLE IF NOT EXISTS study_task (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    study_plan_week_id UUID,
    task_type VARCHAR(255),
    title VARCHAR(255),
    estimated_minutes INTEGER,
    CONSTRAINT fk_task_week FOREIGN KEY (study_plan_week_id) REFERENCES study_plan_week(id)
);

CREATE TABLE IF NOT EXISTS task_completion (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    study_task_id UUID,
    completed_at TIMESTAMP,
    CONSTRAINT fk_completion_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_completion_task FOREIGN KEY (study_task_id) REFERENCES study_task(id)
);

CREATE TABLE IF NOT EXISTS practice_session (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    skill_type VARCHAR(255),
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT fk_practice_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS practice_answer (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    session_id UUID,
    question_id UUID,
    answer VARCHAR(1000),
    correct BOOLEAN,
    CONSTRAINT fk_answer_session FOREIGN KEY (session_id) REFERENCES practice_session(id),
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES question(id)
);

CREATE TABLE IF NOT EXISTS writing_evaluation_result (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    task_response DOUBLE PRECISION,
    coherence DOUBLE PRECISION,
    lexical_resource DOUBLE PRECISION,
    grammar DOUBLE PRECISION,
    overall_band DOUBLE PRECISION,
    feedback_summary VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS writing_submission (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    prompt_id UUID,
    essay_text VARCHAR(10000),
    evaluation_result_id UUID,
    CONSTRAINT fk_submission_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_submission_prompt FOREIGN KEY (prompt_id) REFERENCES writing_prompt(id),
    CONSTRAINT fk_submission_eval FOREIGN KEY (evaluation_result_id) REFERENCES writing_evaluation_result(id)
);

CREATE TABLE IF NOT EXISTS speaking_evaluation_result (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    fluency DOUBLE PRECISION,
    pronunciation DOUBLE PRECISION,
    lexical_resource DOUBLE PRECISION,
    grammar DOUBLE PRECISION,
    overall_band DOUBLE PRECISION,
    feedback_summary VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS speaking_recording_metadata (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    prompt_id UUID,
    audio_url VARCHAR(2048),
    duration_seconds INTEGER,
    evaluation_result_id UUID,
    CONSTRAINT fk_recording_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_recording_prompt FOREIGN KEY (prompt_id) REFERENCES speaking_prompt(id),
    CONSTRAINT fk_recording_eval FOREIGN KEY (evaluation_result_id) REFERENCES speaking_evaluation_result(id)
);

CREATE TABLE IF NOT EXISTS mock_test_session (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    mode VARCHAR(255),
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT fk_mock_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS mock_test_section_result (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    session_id UUID,
    section VARCHAR(255),
    band_score DOUBLE PRECISION,
    feedback VARCHAR(2000),
    CONSTRAINT fk_section_session FOREIGN KEY (session_id) REFERENCES mock_test_session(id)
);

CREATE TABLE IF NOT EXISTS mock_test_overall_result (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    session_id UUID,
    overall_band DOUBLE PRECISION,
    summary VARCHAR(2000),
    CONSTRAINT fk_overall_session FOREIGN KEY (session_id) REFERENCES mock_test_session(id)
);

CREATE TABLE IF NOT EXISTS mistake_history (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    skill_area VARCHAR(255),
    description VARCHAR(2000),
    occurred_at TIMESTAMP,
    CONSTRAINT fk_mistake_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS progress_snapshot (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id UUID,
    overall_band DOUBLE PRECISION,
    snapshot_date DATE,
    CONSTRAINT fk_snapshot_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);
