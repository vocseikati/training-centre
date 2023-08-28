CREATE TABLE IF NOT EXISTS courses
(
    `id`                   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title`                VARCHAR(255),
    `start_date`           DATE,
    `end_date`             DATE,
    `duration_in_hours`    INT,
    `individual_education` BOOLEAN,
    `price`                DOUBLE,
    `type`                 ENUM ('THEORETICAL', 'PRACTICAL'),
    `status`               ENUM ('FINISHED', 'IN_PROGRESS', 'PLANNED','INCOMPLETE'),
    `instructor_id`        BIGINT,
    `class_room_id`        BIGINT,
    `created_at`           DATETIME,
    `last_updated_at`      DATETIME
);
INSERT INTO courses (id, title, start_date, end_date, duration_in_hours, individual_education, price,
                     type, status, instructor_id, class_room_id, created_at, last_updated_at)
VALUES (1, 'exam preparation course', '2021-06-01', '2021-06-01', 8, TRUE, 40000, 'THEORETICAL', 'FINISHED', 1, 1,
        '2021-07-01', '2021-07-17'),
       (2, 'women basic and fashion haircuts', '2021-06-10', '2021-06-11', 16, FALSE, 30000, 'PRACTICAL',
        'FINISHED', 2, 2, '2021-07-01', '2021-07-17'),
       (3, 'men fashion and classic haircut technologies', '2021-06-01', '2021-06-09', 40, FALSE, 32000, 'PRACTICAL',
        'FINISHED', 1, 2, '2021-07-01', '2021-07-17'),
       (4, 'haircut techniques', '2021-06-20', '2021-06-20', 8, TRUE, 40000, 'THEORETICAL', 'FINISHED', 1,1,
        '2021-07-01', '2021-07-17'),
       (5, 'hair drying for women', '2021-07-21', '2021-07-22', 15, FALSE, 30000, 'PRACTICAL', 'IN_PROGRESS', 2, 2,
        '2021-07-01', '2021-07-17'),
       (6, 'painting techniques', '2021-08-01', '2021-09-15', 60, FALSE, 35000, 'PRACTICAL', 'IN_PROGRESS', 2, 2,
        '2021-07-01', '2021-07-17'),
       (7, 'haircuts with razors and trimmers', '2021-09-18', '2021-09-18', 8, FALSE, 30000, 'PRACTICAL',
        'PLANNED', 1, 1, '2021-07-01', '2021-07-17');