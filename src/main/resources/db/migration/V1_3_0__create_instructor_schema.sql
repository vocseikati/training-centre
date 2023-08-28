CREATE TABLE IF NOT EXISTS instructors
(
    `id`              BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `first_name`      VARCHAR(255),
    `last_name`       VARCHAR(255),
    `gender`          ENUM ('MALE', 'FEMALE'),
    `date_of_birth`   DATE,
    `mailing_address` VARCHAR(255),
    `billing_address` VARCHAR(255),
    `email`           VARCHAR(255),
    `phone_number`    VARCHAR(255),
    `created_at`      DATETIME,
    `last_updated_at` DATETIME
);
INSERT INTO instructors (id, first_name, last_name, gender, date_of_birth, mailing_address, billing_address, email,
                         phone_number,created_at,last_updated_at)
VALUES (1, 'Varga' , 'Csilla', 'FEMALE', '1973-05-21', 'Fogócska utca 1., 1113 Budapest Hungary',
        'Fogócska utca 1., 1113 Budapest Hungary', 'vcsilla@gmail.com', '0036302532801',
        '2021-07-01', '2021-07-17'),
       (2, 'Alma' , 'Virág', 'FEMALE', '1984-03-25', 'Etele út 54/a., 1115 Budapest Hungary',
        'Etele út 54/a., 1115 Budapest Hungary', 'almavirag@gmail.com', '0036306984210',
        '2021-07-01', '2021-07-17');