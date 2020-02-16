/*
Navicat MySQL Data Transfer

Source Server         : 连接1
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : demo1_login

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2020-02-16 15:01:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `account` int NOT NULL,
  `password` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'name',
  `age` int DEFAULT '0',
  `sex` int DEFAULT '1' COMMENT '1 = man,0 = woman',
  `avatar` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'tou Xiang',
  `signature` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'no words' COMMENT 'word',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '1141517977', 'jiweihao', '季炜浩', '19', '1', 'C:\\Users\\Administrator\\Desktop\\20200216140415', '无');
INSERT INTO `user` VALUES ('11', '123', 'as', 'ex', '12', '1', null, 'asdf');
