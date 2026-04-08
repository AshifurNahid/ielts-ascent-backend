-- Seed data for Writing module catalogs used by /api/writing/prompts and /api/writing/templates.
-- Uses fixed UUIDs so environments remain deterministic and idempotent.

INSERT INTO writing_prompt (
    id,
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
VALUES
    (
        '11111111-1111-1111-1111-111111111101',
        NOW(),
        NOW(),
        'Task 1: City Transport Trends',
        'TASK_1',
        'The chart shows the percentage of commuters using four transport types in Metro City from 2000 to 2020. Summarize the key features and make comparisons where relevant.',
        'Write at least 150 words. Highlight overall trends before details.',
        '["task1","line-chart","transport","overview"]',
        'EASY',
        4.5,
        6.0,
        FALSE,
        'PUBLISHED',
        TRUE
    ),
    (
        '11111111-1111-1111-1111-111111111102',
        NOW(),
        NOW(),
        'Task 1: University Budget Allocation',
        'TASK_1',
        'The table compares how a university allocated its annual budget across five categories in 2015, 2020, and 2025. Summarize the information by selecting and reporting the main features.',
        'Write at least 150 words. Use comparisons across years and categories.',
        '["task1","table","finance","comparison"]',
        'MEDIUM',
        5.5,
        7.0,
        FALSE,
        'PUBLISHED',
        TRUE
    ),
    (
        '11111111-1111-1111-1111-111111111201',
        NOW(),
        NOW(),
        'Task 2: Remote Work and Productivity',
        'TASK_2',
        'Some people believe remote work increases productivity, while others think office work is more effective. Discuss both views and give your own opinion.',
        'Write at least 250 words. Include a clear thesis and balanced discussion.',
        '["task2","opinion","workplace","discussion"]',
        'MEDIUM',
        6.0,
        7.5,
        FALSE,
        'PUBLISHED',
        TRUE
    ),
    (
        '11111111-1111-1111-1111-111111111202',
        NOW(),
        NOW(),
        'Task 2: Public Funding for Arts',
        'TASK_2',
        'Governments should spend money on public services rather than on the arts. To what extent do you agree or disagree?',
        'Write at least 250 words. Support your position with relevant examples.',
        '["task2","agree-disagree","society","public-policy"]',
        'HARD',
        6.5,
        8.5,
        TRUE,
        'PUBLISHED',
        TRUE
    ),
    (
        '11111111-1111-1111-1111-111111111299',
        NOW(),
        NOW(),
        'Task 2: AI in Education (Draft)',
        'TASK_2',
        'Artificial intelligence should replace teachers in most classrooms within 20 years. Discuss both views and give your opinion.',
        'Write at least 250 words. This prompt is intentionally left in draft status for admin workflow checks.',
        '["task2","ai","education","draft"]',
        'HARD',
        6.5,
        8.5,
        FALSE,
        'DRAFT',
        FALSE
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO writing_template (
    id,
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
VALUES
    (
        '22222222-2222-2222-2222-222222222101',
        NOW(),
        NOW(),
        '11111111-1111-1111-1111-111111111101',
        'TASK_1',
        'Task 1 Core Structure (Overview First)',
        'A compact structure for band 6+ Task 1 responses.',
        'Introduction: paraphrase the prompt in one sentence.\nOverview: describe 2-3 major trends without numbers.\nBody 1: report the first half of key comparisons with figures.\nBody 2: report remaining comparisons and notable exceptions.',
        6.0,
        TRUE
    ),
    (
        '22222222-2222-2222-2222-222222222102',
        NOW(),
        NOW(),
        '11111111-1111-1111-1111-111111111102',
        'TASK_1',
        'Task 1 Table Comparison Framework',
        'Template focused on year-over-year and category-level contrasts.',
        'Intro: paraphrase what is compared.\nOverview: state biggest rises/falls and highest/lowest categories.\nParagraph A: compare categories in earliest year.\nParagraph B: compare changes into later years.',
        6.5,
        TRUE
    ),
    (
        '22222222-2222-2222-2222-222222222201',
        NOW(),
        NOW(),
        '11111111-1111-1111-1111-111111111201',
        'TASK_2',
        'Discuss Both Views + Opinion',
        'Balanced 4-paragraph structure for discussion essays.',
        'P1 Introduction: paraphrase + thesis.\nP2 View A: explain and support with one example.\nP3 View B: explain and support with one example.\nP4 Opinion/Conclusion: state position and final justification.',
        7.0,
        TRUE
    ),
    (
        '22222222-2222-2222-2222-222222222202',
        NOW(),
        NOW(),
        '11111111-1111-1111-1111-111111111202',
        'TASK_2',
        'Agree/Disagree High-Band Outline',
        'Argument-led outline with concession and rebuttal.',
        'Introduction with clear stance.\nBody 1 main argument + evidence.\nBody 2 second argument + evidence.\nBody 3 concession + rebuttal.\nConclusion restates stance with implications.',
        7.5,
        TRUE
    ),
    (
        '22222222-2222-2222-2222-222222222299',
        NOW(),
        NOW(),
        '11111111-1111-1111-1111-111111111299',
        'TASK_2',
        'Draft Internal Template',
        'Kept inactive so /api/writing/templates naturally filters it out.',
        'Internal draft content for admin testing only.',
        8.0,
        FALSE
    )
ON CONFLICT (id) DO NOTHING;

