DROP DATABASE IF EXISTS `lttrs`;
CREATE DATABASE `lttrs`;
USE `lttrs`;

-- Accounts table to store user information.
CREATE TABLE IF NOT EXISTS `accounts`
(
    `id`       INT                    NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255)           NOT NULL UNIQUE,
    `password` VARCHAR(255)           NOT NULL,
    `name`     VARCHAR(255)           NOT NULL,
    `role`     ENUM ('user', 'admin') NOT NULL DEFAULT 'user',
    PRIMARY KEY (`id`)
);

-- Contracts table to store information about the user's contract.
CREATE TABLE IF NOT EXISTS `contracts`
(
    `id`         INT  NOT NULL AUTO_INCREMENT,
    `account_id` INT  NOT NULL,
    `hours`      INT  NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date`   DATE NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_contracts_accounts` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE
);

-- Leaves table to store absence information.
CREATE TABLE IF NOT EXISTS `leaves`
(
    `id`         INT  NOT NULL AUTO_INCREMENT,
    `account_id` INT  NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date`   DATE NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_leaves_accounts` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE
);

-- Registrations table to store the amount of hours worked on a specific date.
CREATE TABLE IF NOT EXISTS `registrations`
(
    `id`         INT  NOT NULL AUTO_INCREMENT,
    `account_id` INT  NOT NULL,
    `hours`      INT  NOT NULL,
    `date`       DATE NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_registrations_accounts` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE
);
