-- Lightweight seed data for Writing and Reading modules (AI testing)
-- Idempotent inserts based on business keys (titles/prompts).

-- =========================
-- Writing prompts/templates
-- =========================
INSERT INTO writing_prompt (
    created_at,
    updated_at,
    title,
    task_type,
    prompt_text,
    instructions,
    tags,
    difficulty,
    ielts_band_min,
    ielts_band_max,
    premium,
    status,
    template_enabled
)
SELECT
    NOW(), NOW(),
    'Task 2: Urban Green Spaces',
    'TASK_2',
    'Some people think cities should invest more in public parks and green spaces than in new roads. Discuss both views and give your opinion.',
    'Write at least 250 words. State a clear opinion and support it with examples.',
    '["task2","discussion","cities","environment"]',
    'MEDIUM',
    6.0,
    7.5,
    FALSE,
    'PUBLISHED',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM writing_prompt WHERE title = 'Task 2: Urban Green Spaces'
);

INSERT INTO writing_prompt (
    created_at,
    updated_at,
    title,
    task_type,
    prompt_text,
    instructions,
    tags,
    difficulty,
    ielts_band_min,
    ielts_band_max,
    premium,
    status,
    template_enabled
)
SELECT
    NOW(), NOW(),
    'Task 1: Internet Usage by Age Group',
    'TASK_1',
    'The chart compares internet usage rates among four age groups in 2005, 2015, and 2025. Summarize the main features and make comparisons where relevant.',
    'Write at least 150 words. Include an overview and key comparisons.',
    '["task1","chart","technology","comparison"]',
    'EASY',
    5.0,
    6.5,
    FALSE,
    'PUBLISHED',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM writing_prompt WHERE title = 'Task 1: Internet Usage by Age Group'
);

INSERT INTO writing_template (
    created_at,
    updated_at,
    prompt_id,
    task_type,
    title,
    description,
    template_content,
    target_band,
    active
)
SELECT
    NOW(), NOW(),
    p.id,
    'TASK_2',
    'Task 2 Balanced Discussion Structure',
    'Simple 4-paragraph structure for discussion essays.',
    'P1: Paraphrase + thesis\nP2: View A + support\nP3: View B + support\nP4: Opinion + conclusion',
    6.5,
    TRUE
FROM writing_prompt p
WHERE p.title = 'Task 2: Urban Green Spaces'
  AND NOT EXISTS (
    SELECT 1 FROM writing_template t WHERE t.title = 'Task 2 Balanced Discussion Structure'
);

-- =====================
-- Reading passages/tests
-- =====================
INSERT INTO reading_passage_v2 (
    created_at,
    updated_at,
    title,
    content,
    short_description,
    topic_tag,
    difficulty,
    ielts_band_min,
    ielts_band_max,
    estimated_reading_minutes,
    premium,
    status
)
SELECT
    NOW(), NOW(),
    'The Growth of Urban Cycling',
    'Many cities have introduced bike lanes over the last decade. Researchers observed that safe cycling networks increased commuter confidence and reduced short car trips in city centers.',
    'A short passage about transport behavior change.',
    'TRANSPORT',
    'MEDIUM',
    5.5,
    7.0,
    6,
    FALSE,
    'PUBLISHED'
WHERE NOT EXISTS (
    SELECT 1 FROM reading_passage_v2 WHERE title = 'The Growth of Urban Cycling'
);

INSERT INTO reading_question_v2 (
    created_at,
    updated_at,
    passage_id,
    group_number,
    type,
    prompt,
    options,
    correct_answer,
    explanation,
    answer_source_hint,
    difficulty,
    order_index,
    premium,
    status,
    tags
)
SELECT
    NOW(), NOW(),
    p.id,
    1,
    'MULTIPLE_CHOICE_SINGLE',
    'What was one reported result of safer cycling networks?',
    '["Increased commuter confidence","Higher fuel prices","Lower internet usage","Longer train delays"]',
    'Increased commuter confidence',
    'The passage explicitly states that safe cycling networks increased commuter confidence.',
    'Look at the second sentence.',
    'MEDIUM',
    1,
    FALSE,
    'PUBLISHED',
    '["transport","cycling"]'
FROM reading_passage_v2 p
WHERE p.title = 'The Growth of Urban Cycling'
  AND NOT EXISTS (
    SELECT 1 FROM reading_question_v2 q
    WHERE q.prompt = 'What was one reported result of safer cycling networks?'
  );

INSERT INTO reading_test (
    created_at,
    updated_at,
    title,
    description,
    total_time_minutes,
    difficulty,
    premium,
    status
)
SELECT
    NOW(), NOW(),
    'Reading Mini Test: Urban Mobility',
    'One-passage mini test for reading AI explanations and recommendation flows.',
    20,
    'MEDIUM',
    FALSE,
    'PUBLISHED'
WHERE NOT EXISTS (
    SELECT 1 FROM reading_test WHERE title = 'Reading Mini Test: Urban Mobility'
);

INSERT INTO reading_test_passage (
    created_at,
    updated_at,
    reading_test_id,
    passage_id,
    order_index
)
SELECT
    NOW(), NOW(),
    t.id,
    p.id,
    1
FROM reading_test t
JOIN reading_passage_v2 p ON p.title = 'The Growth of Urban Cycling'
WHERE t.title = 'Reading Mini Test: Urban Mobility'
  AND NOT EXISTS (
    SELECT 1 FROM reading_test_passage rtp
    WHERE rtp.reading_test_id = t.id AND rtp.passage_id = p.id
  );

