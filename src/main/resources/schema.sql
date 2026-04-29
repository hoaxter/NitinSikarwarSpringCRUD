-- Initialize student table if it does not already exist
CREATE TABLE IF NOT EXISTS students (
    id      SERIAL       PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    email   VARCHAR(100) NOT NULL,
    course  VARCHAR(100) NOT NULL
);
