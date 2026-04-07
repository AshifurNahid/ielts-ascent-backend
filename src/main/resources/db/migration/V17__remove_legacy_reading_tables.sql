-- Remove legacy reading/question schema after moving to reading v2 tables.
DROP TABLE IF EXISTS practice_answer;
DROP TABLE IF EXISTS answer_key;
DROP TABLE IF EXISTS question_option;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS reading_passage;

