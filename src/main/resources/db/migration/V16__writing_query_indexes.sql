CREATE INDEX IF NOT EXISTS idx_writing_weak_point_submission ON writing_weak_point(submission_id);
CREATE INDEX IF NOT EXISTS idx_writing_suggestion_submission ON writing_suggestion(submission_id);
CREATE INDEX IF NOT EXISTS idx_writing_submission_user_created ON writing_submission(user_id, created_at DESC);

