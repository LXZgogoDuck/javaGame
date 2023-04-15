CREATE DATABASE IF NOT EXISTS javaGame;
USE javaGame;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS `commonuser`;
CREATE TABLE `commonuser` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `username` varchar(60) COLLATE utf8_bin NOT NULL,
                              `password` varchar(60) COLLATE utf8_bin NOT NULL,
                              `trueName` varchar(60) COLLATE utf8_bin DEFAULT NULL,
                              `sid` varchar(60) COLLATE utf8_bin DEFAULT NULL,
                              `tel` varchar(60) COLLATE utf8_bin DEFAULT NULL,
                              `address` varchar(60) COLLATE utf8_bin DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4357 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

BEGIN;
INSERT INTO `commonuser` VALUES (1, '刘萱卓', '666', 'Duckduckgo', '410392839293829392', '145393895831', 'mars');
INSERT INTO `commonuser` VALUES (2, '邱小雨', '520', '', '', '', 'earth');
COMMIT;


SET FOREIGN_KEY_CHECKS = 1;
