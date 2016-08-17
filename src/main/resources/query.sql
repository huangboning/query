/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : query

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-08-17 11:23:09
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
  `account_real_name` varchar(255) DEFAULT NULL,
  `account_email` varchar(255) DEFAULT NULL,
  `account_repository` varchar(255) DEFAULT NULL,
  `account_status` int(11) DEFAULT '0' COMMENT '0正常-1禁用',
  `account_pwd_status` int(11) DEFAULT '0',
  `account_template_status` int(11) DEFAULT '0',
  `account_date` datetime DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_name_unique` (`account_name`),
  UNIQUE KEY `account_repository_unique` (`account_repository`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_branch
-- ----------------------------
DROP TABLE IF EXISTS `t_branch`;
CREATE TABLE `t_branch` (
  `branch_id` int(11) NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(255) DEFAULT NULL,
  `branch_name_cn` varchar(255) DEFAULT NULL,
  `scene_id` int(11) DEFAULT NULL,
  `branch_date` datetime DEFAULT NULL,
  PRIMARY KEY (`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_note
-- ----------------------------
DROP TABLE IF EXISTS `t_note`;
CREATE TABLE `t_note` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `note_title` varchar(255) DEFAULT NULL,
  `note_content` varchar(255) DEFAULT NULL,
  `note_date` datetime DEFAULT NULL,
  PRIMARY KEY (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `scene_scope` varchar(255) DEFAULT NULL,
  `scene_date` datetime DEFAULT NULL,
  PRIMARY KEY (`scene_id`),
  UNIQUE KEY `scene_no_unique` (`scene_uuid`) USING BTREE,
  UNIQUE KEY `scene_git_unique` (`scene_git`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_scene_desc
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_desc`;
CREATE TABLE `t_scene_desc` (
  `scene_desc_id` int(11) NOT NULL AUTO_INCREMENT,
  `scene_uuid` varchar(255) DEFAULT NULL,
  `scene_desc` varchar(255) DEFAULT NULL,
  `scene_version` varchar(255) DEFAULT NULL,
  `scene_id` int(11) DEFAULT NULL,
  `scene_desc_date` datetime DEFAULT NULL,
  PRIMARY KEY (`scene_desc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_share_fragment
-- ----------------------------
DROP TABLE IF EXISTS `t_share_fragment`;
CREATE TABLE `t_share_fragment` (
  `share_fragment_id` int(11) NOT NULL AUTO_INCREMENT,
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
  `share_fragment_scope` varchar(255) DEFAULT NULL,
  `share_fragment_git` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`share_fragment_id`),
  UNIQUE KEY `share_fragment_no_unique` (`share_fragment_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_share_variable
-- ----------------------------
DROP TABLE IF EXISTS `t_share_variable`;
CREATE TABLE `t_share_variable` (
  `share_variable_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `share_variable_name` varchar(255) DEFAULT NULL,
  `share_variable_uuid` int(11) DEFAULT NULL,
  `share_variable_type` varchar(255) DEFAULT NULL,
  `share_variable_scope` varchar(255) DEFAULT NULL,
  `fragment_uuid` varchar(255) DEFAULT NULL,
  `share_variable_obj_type` varchar(255) DEFAULT NULL,
  `share_variable_desc` varchar(255) DEFAULT NULL,
  `share_variable_date` datetime DEFAULT NULL,
  `share_variable_version` varchar(255) DEFAULT NULL,
  `share_variable_git` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`share_variable_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_variable
-- ----------------------------
DROP TABLE IF EXISTS `t_variable`;
CREATE TABLE `t_variable` (
  `variable_id` int(11) NOT NULL AUTO_INCREMENT,
  `scene_id` int(11) DEFAULT NULL,
  `variable_name` varchar(255) DEFAULT NULL,
  `variable_uuid` varchar(255) DEFAULT NULL,
  `variable_type` varchar(255) DEFAULT NULL,
  `variable_scope` tinyint(4) DEFAULT '0' COMMENT '0是scenario表示全局变量，1fragment表示fragment内的局部变量',
  `fragment_uuid` varchar(255) DEFAULT '0' COMMENT '（当scope为全局时这里fragment为0）0表示是全局变量，当scope为fragment内部变量时这里fragment为所属的fragmentid）',
  `variable_obj_type` varchar(255) DEFAULT NULL,
  `variable_desc` varchar(255) DEFAULT NULL,
  `variable_date` datetime DEFAULT NULL,
  PRIMARY KEY (`variable_id`),
  UNIQUE KEY `variable_no_unique` (`variable_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
