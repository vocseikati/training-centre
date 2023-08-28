CREATE TABLE IF NOT EXISTS student_courses
(
    `student_id` BIGINT,
    `course_id`  BIGINT,
    FOREIGN KEY (`student_id`) REFERENCES students (id),
    FOREIGN KEY (`course_id`) REFERENCES courses (id)
);