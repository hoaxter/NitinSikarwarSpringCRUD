-- Enrollee table — created on first launch if absent
CREATE TABLE IF NOT EXISTS enrollees (
    enrollee_id   SERIAL        PRIMARY KEY,
    full_name     VARCHAR(120)  NOT NULL,
    email_address VARCHAR(120)  NOT NULL,
    program       VARCHAR(120)  NOT NULL
);
