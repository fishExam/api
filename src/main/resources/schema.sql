CREATE TABLE IF NOT EXISTS user_refresh_tokens (
    refresh_token VARCHAR(1024) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    surname VARCHAR(30) NOT NULL UNIQUE,
    first_name VARCHAR(30) NOT NULL,
    patronymic VARCHAR(30),
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    birth DATE NOT NULL,
    telegram_id VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(1024) NOT NULL
);

CREATE TABLE IF NOT EXISTS parents (
    user_id SERIAL PRIMARY KEY,
    surname VARCHAR(30) NOT NULL UNIQUE,
    first_name VARCHAR(30) NOT NULL,
    patronymic VARCHAR(30),
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    birth DATE NOT NULL,
    telegram_id VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS students (
    user_id SERIAL PRIMARY KEY,
    surname VARCHAR(30) NOT NULL UNIQUE,
    first_name VARCHAR(30) NOT NULL,
    patronymic VARCHAR(30),
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    birth DATE NOT NULL,
    telegram_id VARCHAR(255) NOT NULL UNIQUE,
    parent_id BIGINT,
    tasks_count INTEGER NOT NULL DEFAULT 0 CHECK (tasks_count >= 0),
    FOREIGN KEY (parent_id) REFERENCES parents(user_id)
);

CREATE TABLE IF NOT EXISTS teachers (
    user_id BIGSERIAL PRIMARY KEY,
    surname VARCHAR(30) NOT NULL UNIQUE,
    first_name VARCHAR(30) NOT NULL,
    patronymic VARCHAR(30),
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    birth DATE NOT NULL,
    telegram_id VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS teacher_students (
    teacher_students_id BIGSERIAL PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teachers(user_id),
    FOREIGN KEY (student_id) REFERENCES students(user_id)
);

CREATE TABLE IF NOT EXISTS homework (
    homework_id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    description TEXT,
    FOREIGN KEY (author_id) REFERENCES teachers(user_id)
);

CREATE TABLE IF NOT EXISTS homework_user (
    homework_user_id BIGSERIAL PRIMARY KEY,
    homework_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    FOREIGN KEY (homework_id) REFERENCES homework(homework_id),
    FOREIGN KEY (student_id) REFERENCES students(user_id)
);

CREATE TABLE IF NOT EXISTS task (
    task_id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    answer VARCHAR(255) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES teachers(user_id)
);

CREATE TABLE IF NOT EXISTS homework_task (
    homework_task_id BIGSERIAL PRIMARY KEY,
    homework_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    FOREIGN KEY (homework_id) REFERENCES homework(homework_id),
    FOREIGN KEY (task_id) REFERENCES task(task_id)
);

CREATE TABLE IF NOT EXISTS student_answers (
    student_answers_id BIGSERIAL PRIMARY KEY,
    homework_user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    student_answer VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    feedback TEXT,
    update_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (homework_user_id) REFERENCES homework_user(homework_user_id),
    FOREIGN KEY (task_id) REFERENCES task(task_id)
);

CREATE TABLE IF NOT EXISTS outline (
    outline_id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES teachers(user_id)
);

CREATE TABLE IF NOT EXISTS outline_student (
    outline_student_id BIGSERIAL PRIMARY KEY,
    outline_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    UNIQUE (outline_id, student_id),
    FOREIGN KEY (outline_id) REFERENCES outline(outline_id),
    FOREIGN KEY (student_id) REFERENCES students(user_id)
);

CREATE TABLE IF NOT EXISTS hobby (
    hobby_id BIGSERIAL PRIMARY KEY,
    topic VARCHAR(255) NOT NULL unique
);

CREATE TABLE IF NOT EXISTS hobby_student (
    hobby_student_id BIGSERIAL PRIMARY KEY,
    hobby_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    FOREIGN KEY (hobby_id) REFERENCES hobby(hobby_id),
    FOREIGN KEY (student_id) REFERENCES students(user_id)
);
