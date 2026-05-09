CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    experience INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS user_skills (
    user_name VARCHAR(255) REFERENCES users(name),
    skill VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_name, skill)
);

CREATE TABLE IF NOT EXISTS jobs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    company VARCHAR(255) NOT NULL,
    required_experience INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS job_tags (
    job_title VARCHAR(255) REFERENCES jobs(title),
    tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (job_title, tag)
);