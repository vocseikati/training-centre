CREATE TABLE IF NOT EXISTS certificates
(
    `id`                       BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `student_first_name`       VARCHAR(255),
    `student_last_name`        VARCHAR(255),
    `course_start_date`        DATE,
    `course_end_date`          DATE,
    `course_duration_in_hours` INTEGER,
    `course_title`             VARCHAR(255),
    `done`                     BOOLEAN,
    `created_at`               DATE,
    `student_id`               BIGINT
);