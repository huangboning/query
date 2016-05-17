/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : 127.0.0.1:3306
Source Database       : query

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-05-18 00:34:38
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account
-- ----------------------------
INSERT INTO `t_account` VALUES ('1', '15557181347', 'huangboning', 'e10adc3949ba59abbe56e057f20f883e', 'huangboning@test.com', '/2016/5/huangboning', '2016-05-17 09:39:51');

-- ----------------------------
-- Table structure for t_fragment
-- ----------------------------
DROP TABLE IF EXISTS `t_fragment`;
CREATE TABLE `t_fragment` (
  `fragment_id` int(11) NOT NULL AUTO_INCREMENT,
  `scene_id` int(11) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_fragment
-- ----------------------------
INSERT INTO `t_fragment` VALUES ('3', '1', 'FRGMaa3a42fc184f4943b0f6035baedec0fd', 'foxFragment', 'query', null, '我的第一个fragment', '0', '0', '2016-05-17 17:19:18');
INSERT INTO `t_fragment` VALUES ('4', '1', 'FRGMa8fbd000b5454ad3867f91c31a678036', 'cangFragment', 'filter', null, '我的第二个fragment', '0', '0', '2016-05-17 17:19:48');
INSERT INTO `t_fragment` VALUES ('5', '2', 'FRGM7d7a94c4c08c4770951ba64731b86a12', 'thirdFragment', 'filter', null, '我的第三个fragment', '0', '0', '2016-05-17 17:21:06');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_scene
-- ----------------------------
INSERT INTO `t_scene` VALUES ('1', '1', 'SCNObd1d9a66bca44137a70fd766558f56a8', 'foxquery', '我的第一个场景foxquery', null, '1463449947107', '0', '0', '2016-05-17 09:52:27');
INSERT INTO `t_scene` VALUES ('2', '1', 'SCNO30652d124b8e4d63b68207c7cdaee113', 'foxquery2', '我的第二个场景foxquery', null, '1463450088442', '0', '0', '2016-05-17 09:54:48');

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
  `share_fragment_git` varchar(255) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_variable
-- ----------------------------
