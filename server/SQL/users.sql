-- Active: 1703159712281@@127.0.0.1@3306@views

DROP TABLE IF EXISTS users;

-- user : 회원 테이블
CREATE TABLE
    `users` (
        `NO` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `USER_ID` VARCHAR(100) NOT NULL,
        `USER_PW` VARCHAR(200) NOT NULL,
        `NAME` VARCHAR(100) NOT NULL,
        `EMAIL` VARCHAR(200) DEFAULT NULL,
        `REG_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `UPD_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `ENABLED` int DEFAULT 1
    ) COMMENT = '회원';

--BCryptPasswordEncoder - 암호화 시
-- 사용자
INSERT INTO
    `users` (user_id, user_pw, name, email)
VALUES (
        'user',
        '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92',
        '사용자',
        'user@email.com'
    );

--관리자
INSERT INTO
    `users` (user_id, user_pw, name, email)
VALUES (
        'admin',
        '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92',
        '관리자',
        'admin@email.com'
    );