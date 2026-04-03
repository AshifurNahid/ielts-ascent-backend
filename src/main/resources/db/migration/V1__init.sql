CREATE TABLE IF NOT EXISTS user_profile (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    target_band DOUBLE PRECISION NOT NULL,
    exam_date DATE,
    timezone VARCHAR(255),
    study_preference VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS app_user (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    profile_id UUID,
    CONSTRAINT fk_user_profile FOREIGN KEY (profile_id) REFERENCES user_profile(id)
);
