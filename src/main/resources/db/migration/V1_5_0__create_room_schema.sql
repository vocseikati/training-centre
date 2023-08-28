CREATE TABLE IF NOT EXISTS rooms
(
    `id`              BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`            VARCHAR(255),
    `address`         VARCHAR(255),
    `capacity`        INTEGER,
    `created_at`      DATETIME,
    `last_updated_at` DATETIME
);
INSERT INTO rooms (id, name, address, capacity, created_at, last_updated_at)
VALUES (1, 'theoretical room', 'Lajos utca, Budapest 1033', 10, '2021-07-24', '2021-07-24'),
       (2, 'practical room', 'Bécsi út 96, Budapest 1035', 5, '2021-07-20', '2021-07-24');