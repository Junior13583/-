/*
Navicat MySQL Data Transfer

Source Server         : 10.175.94.80_7000
Source Server Version : 80028
Source Host           : 10.175.94.80:7000
Source Database       : junior

Target Server Type    : MYSQL
Target Server Version : 80028
File Encoding         : 65001

Date: 2023-07-31 14:06:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for chat_msg
-- ----------------------------
DROP TABLE IF EXISTS `chat_msg`;
CREATE TABLE `chat_msg` (
                            `msgId` int NOT NULL AUTO_INCREMENT,
                            `roomId` int NOT NULL,
                            `sender` varchar(255) NOT NULL,
                            `msgType` varchar(255) NOT NULL,
                            `content` longblob,
                            `filename` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `filesize` int DEFAULT NULL,
                            `sendTime` datetime NOT NULL,
                            PRIMARY KEY (`msgId`)
) ENGINE=InnoDB AUTO_INCREMENT=238 DEFAULT CHARSET=utf8mb3;


-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
                        `roomId` int NOT NULL AUTO_INCREMENT,
                        `roomName` varchar(255) NOT NULL,
                        `creator` varchar(255) NOT NULL,
                        PRIMARY KEY (`roomId`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES ('1', 'Junior 的聊天室', 'admin');

-- ----------------------------
-- Table structure for user_room
-- ----------------------------
DROP TABLE IF EXISTS `user_room`;
CREATE TABLE `user_room` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `userIp` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `roomId` int NOT NULL,
                             `createTime` datetime NOT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;


