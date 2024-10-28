-- Active: 1703159712281@@127.0.0.1@3306@views
DROP TABLE IF EXISTS user_auth;

CREATE TABLE `user_auth` (
    `auth_no` INT NOT NULL AUTO_INCREMENT PRIMARY KEY
    , `user_id` VARCHAR(100) NOT NULL
    , `auth`  VARCHAR(100) NOT NULL
);

INSERT INTO `user_auth` (user_id, auth)
VALUES('user', 'ROLE_USER');

INSERT INTO `user_auth` (user_id, auth)
VALUES('admin', 'ROLE_USER');

INSERT INTO `user_auth` (user_id, auth)
VALUES('admin', 'ROLE_ADMIN');