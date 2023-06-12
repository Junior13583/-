/*
Navicat MySQL Data Transfer

Source Server         : 10.175.94.80_7000
Source Server Version : 80028
Source Host           : 10.175.94.80:7000
Source Database       : junior

Target Server Type    : MYSQL
Target Server Version : 80028
File Encoding         : 65001

Date: 2023-03-13 17:21:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                          `password` varchar(255) DEFAULT NULL,
                          `age` int DEFAULT NULL,
                          `sex` varchar(255) DEFAULT NULL,
                          `phoneNumber` bigint DEFAULT NULL,
                          `address` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES ('1', '小明', '123456', '12', '男', '12345679890', '上海');
INSERT INTO `person` VALUES ('2', '小明', '123456', '12', '男', '12345679890', '深圳');
