/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : 127.0.0.1:3306
Source Database       : query

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-05-17 00:55:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `account_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_number` varchar(255) DEFAULT NULL,
  `account_name` varchar(255) DEFAULT NULL,
  `account_password` varchar(255) DEFAULT NULL,
  `account_email` varchar(255) DEFAULT NULL,
  `account_repository` varchar(255) DEFAULT NULL,
  `account_date` datetime DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_name_unique` (`account_name`),
  UNIQUE KEY `account_repository_unique` (`account_repository`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account
-- ----------------------------
INSERT INTO `t_account` VALUES ('1', null, 'test', '123456', null, '/2016/4/test', '2016-04-13 11:04:10');
INSERT INTO `t_account` VALUES ('6', '1555241588', 'huangboning', 'e10adc3949ba59abbe56e057f20f883e', 'admin@126.com', '/2016/4/huangboning', '2016-04-14 23:28:03');
INSERT INTO `t_account` VALUES ('7', '1555241588', 'test3', 'e10adc3949ba59abbe56e057f20f883e', 'admin@126.com', '/2016/4/test3', '2016-04-14 23:28:18');
INSERT INTO `t_account` VALUES ('8', '13911710290', 'itrek', '81dc9bdb52d04dc20036dbd8313ed055', 'itrek@163.com', '/2016/4/itrek', '2016-04-18 11:16:47');
INSERT INTO `t_account` VALUES ('9', '1555241588', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin@126.com', '/2016/5/admin', '2016-05-09 22:13:58');

-- ----------------------------
-- Table structure for t_fragment
-- ----------------------------
DROP TABLE IF EXISTS `t_fragment`;
CREATE TABLE `t_fragment` (
  `fragment_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `fragment_uuid` varchar(255) DEFAULT NULL,
  `fragment_name` varchar(255) DEFAULT NULL,
  `fragment_type` varchar(255) DEFAULT NULL,
  `fragment_obj_type` varchar(255) DEFAULT NULL,
  `fragment_desc` varchar(255) DEFAULT NULL,
  `fragment_enable` tinyint(4) DEFAULT NULL,
  `fragment_active` tinyint(4) DEFAULT NULL,
  `fragment_date` datetime DEFAULT NULL,
  PRIMARY KEY (`fragment_id`),
  UNIQUE KEY `fragment_no_unique` (`fragment_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_fragment
-- ----------------------------
INSERT INTO `t_fragment` VALUES ('2', '6', 'FRGM3b617aa7be24475699f73b7ce47b3bec', 'foxFragment', '', null, 'update 1 fragment', '0', null, '2016-04-25 17:43:41');
INSERT INTO `t_fragment` VALUES ('4', '6', 'FRGM8c7a414e6fad450ebdbc0534a9245f64', 'foxFragment2', '', null, '我的第一个fragment', null, null, '2016-04-25 20:25:08');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '系统管理员');
INSERT INTO `t_role` VALUES ('2', '普通用户');

-- ----------------------------
-- Table structure for t_scene
-- ----------------------------
DROP TABLE IF EXISTS `t_scene`;
CREATE TABLE `t_scene` (
  `scene_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `scene_uuid` varchar(255) DEFAULT NULL,
  `scene_name` varchar(255) DEFAULT NULL,
  `scene_desc` varchar(255) DEFAULT NULL,
  `scene_comment` varchar(255) DEFAULT NULL,
  `scene_git` varchar(255) DEFAULT NULL,
  `scene_active` tinyint(11) NOT NULL DEFAULT '0',
  `scene_enable` tinyint(11) NOT NULL DEFAULT '0',
  `scene_date` datetime DEFAULT NULL,
  PRIMARY KEY (`scene_id`),
  UNIQUE KEY `scene_no_unique` (`scene_uuid`) USING BTREE,
  UNIQUE KEY `scene_git_unique` (`scene_git`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_scene
-- ----------------------------
INSERT INTO `t_scene` VALUES ('1', '1', 'ncno1fa', '测试test场景', null, null, null, '0', '0', '2016-04-13 18:08:15');
INSERT INTO `t_scene` VALUES ('2', '1', 'SCNO42fd5681956a48e08533274df5523eba', 'foxquery', '我的第一个场景scene', null, '1460644790947', '0', '0', '2016-04-14 22:39:50');
INSERT INTO `t_scene` VALUES ('3', '1', 'SCNO54619c77a6f04a7aa8af45b036643136', 'foxquery', '我的第一个场景scene', null, '1460647933119', '0', '0', '2016-04-14 23:32:13');
INSERT INTO `t_scene` VALUES ('4', '1', 'SCNO90c7f72a25124038a8293bd82eb4bc62', 'foxquery', '我的第一个场景scene', null, '1460648312028', '0', '0', '2016-04-14 23:38:32');
INSERT INTO `t_scene` VALUES ('5', '6', 'SCNO6d527981f75343a8b52cd54e7e4b3b9d', 'foxquery1', '我的第一个场景scene', null, '1460648396892', '0', '0', '2016-04-14 23:39:56');
INSERT INTO `t_scene` VALUES ('6', '6', 'SCNO023eb5202d84462aa7d41884835016f4', 'foxquery2', '我的第二个场景scene', null, '1460686073149', '0', '0', '2016-04-15 10:07:53');

-- ----------------------------
-- Table structure for t_share_fragment
-- ----------------------------
DROP TABLE IF EXISTS `t_share_fragment`;
CREATE TABLE `t_share_fragment` (
  `share_fragment_id` int(11) DEFAULT NULL,
  `account_id` int(11) DEFAULT NULL,
  `share_fragment_uuid` varchar(255) DEFAULT NULL,
  `share_fragment_name` varchar(255) DEFAULT NULL,
  `share_fragment_type` varchar(255) DEFAULT NULL,
  `share_fragment_obj_type` varchar(255) DEFAULT NULL,
  `share_fragment_desc` varchar(255) DEFAULT NULL,
  `share_fragment_enable` tinyint(4) DEFAULT NULL,
  `share_fragment_active` tinyint(4) DEFAULT NULL,
  `share_fragment_date` datetime DEFAULT NULL,
  `share_fragment_version` varchar(255) DEFAULT NULL,
  UNIQUE KEY `share_fragment_no_unique` (`share_fragment_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_share_fragment
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_account` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_role_id` int(11) DEFAULT NULL,
  `user_date` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name_unique` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '智擎', '1', '2016-04-06 10:53:21');
INSERT INTO `t_user` VALUES ('2', 'hbn', 'e10adc3949ba59abbe56e057f20f883e', 'hbn', '2', '2016-04-13 10:53:37');

-- ----------------------------
-- Table structure for t_variable
-- ----------------------------
DROP TABLE IF EXISTS `t_variable`;
CREATE TABLE `t_variable` (
  `variable_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `variable_name` varchar(255) DEFAULT NULL,
  `variable_uuid` varchar(255) DEFAULT NULL,
  `variable_type` varchar(255) DEFAULT NULL,
  `variable_scope` tinyint(4) DEFAULT '0' COMMENT '0是scenario表示全局变量，1fragment表示fragment内的局部变量',
  `fragment_id` int(11) DEFAULT '0' COMMENT '（当scope为全局时这里fragment为0）0表示是全局变量，当scope为fragment内部变量时这里fragment为所属的fragmentid）',
  `variable_obj_type` varchar(255) DEFAULT NULL,
  `variable_share` tinyint(4) DEFAULT '0' COMMENT '是否共享（0不共享，1共享）',
  `variable_desc` varchar(255) DEFAULT NULL,
  `variable_date` datetime DEFAULT NULL,
  PRIMARY KEY (`variable_id`),
  UNIQUE KEY `variable_no_unique` (`variable_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_variable
-- ----------------------------
INSERT INTO `t_variable` VALUES ('2', '6', 'first Variable', 'VAR4b237122c1b241e69176329d698e1eaa', null, '0', '0', null, '1', '我的第一个变量', '2016-04-26 19:18:32');
INSERT INTO `t_variable` VALUES ('3', '6', 'sencond Variable', 'VARfe22dd661bb14dbea3721ec2d5681ab0', null, '0', '0', null, '0', '我的第二个变量', '2016-04-26 20:14:30');
