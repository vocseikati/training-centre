CREATE TABLE IF NOT EXISTS students
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
    `level`           VARCHAR(255),
    `created_at`      DATETIME,
    `last_updated_at` DATETIME
);
INSERT INTO students (id, first_name, last_name, gender, date_of_birth, mailing_address, billing_address, email,
                      phone_number, level, created_at, last_updated_at)
VALUES (1, 'Első', 'Némó', 'MALE', '1988-05-21', 'Szabadság út 93., 2040 Budaörs Hungary',
        'Petőfi Sándor utca 23., 2120 Dunakeszi Hungary', 'harsanyiherka@freemail.hu', '0036-70-525-5200', 'beginner',
        '2021-07-01', '2021-07-17'),
       (2, 'Második', 'Miklós', 'MALE', '1993-02-08', 'Petőfi S. út 19., 2015 Szigetmonostor Hungary',
        'Petőfi S. út 19., 2015 Szigetmonostor Hungary', 'vocseikati@gmail.com', '0036-20-215-8200', 'beginner',
        '2021-07-01', '2021-07-17'),
       (3, 'Harmadik', 'Mariann', 'FEMALE', '1984-03-25', 'Damijanich út 21., 9400 Sopron Hungary',
        'Etele út 54., 1115 Budapest Hungary', 'mariharom@freemail.hu', '0036-30-115-3206', 'restarter', '2021-07-01',
        '2021-07-17'),
       (4, 'Negyedik', 'Júlia', 'FEMALE', '1996-09-01', 'Alberti Béla utca 36., 2151 Fót Hungary',
        'Alberti Béla utca 36., 2151 Fót Hungary', 'julinegy@citromail.hu', '0036-20-899-9900', 'career modifier',
        '2021-07-01', '2021-07-17'),
       (5, 'Ötödik', 'Liza', 'FEMALE', '1995-10-11', 'Kiss János altábornagy utca 10., 1126 Budapest Hungary',
        'Kiss János altábornagy utca 10., 1126 Budapest Hungary', 'lizaot@gmail.com', '0036-30-625-3206', 'master',
        '2021-07-01', '2021-07-17');