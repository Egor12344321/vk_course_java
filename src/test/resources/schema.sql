DROP TABLE IF EXISTS user_skills;
DROP TABLE IF EXISTS job_tags;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS jobs;

CREATE TABLE users (
    name VARCHAR(255) PRIMARY KEY,
    experience INTEGER NOT NULL
);

CREATE TABLE user_skills (
    user_name VARCHAR(255) NOT NULL,
    skill VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_name, skill)
);

CREATE TABLE jobs (
    title VARCHAR(255) PRIMARY KEY,
    company VARCHAR(255) NOT NULL,
    required_experience INTEGER NOT NULL
);

CREATE TABLE job_tags (
    job_title VARCHAR(255) NOT NULL,
    tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (job_title, tag)
);