CREATE TABLE user_refresh_tokens (
    refresh_token VARCHAR(1024) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(1024) NOT NULL
);

CREATE TABLE students (
    user_id INT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    name VARCHAR(255)
);

CREATE TABLE teachers (
    user_id INT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    name VARCHAR(255)
);