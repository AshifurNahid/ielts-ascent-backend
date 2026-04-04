CREATE TABLE IF NOT EXISTS grammar_topic (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    icon VARCHAR(50),
    lessons INTEGER NOT NULL,
    completed INTEGER NOT NULL,
    order_index INTEGER NOT NULL,
    unlocked BOOLEAN NOT NULL DEFAULT TRUE
);
