truncate t_fragment;
truncate t_scene;
truncate t_share_fragment;
truncate t_variable;

/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : query

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-05-21 19:00:23
*/

SET FOREIGN_KEY_CHECKS=0;

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
-- Records of t_share_variable
-- ----------------------------


/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : query

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-05-21 19:00:31
*/

SET FOREIGN_KEY_CHECKS=0;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_variable
-- ----------------------------
INSERT INTO `t_variable` VALUES ('6', '1', 'third Variable', 'VAR4fbdb8ec8ac04fdd94f059571f6e4e4a', 'vType', '1', 'FRGMaa3a42fc184f4943b0f6035baedec0fd', 'voType', '我的第二个变量', '2016-05-21 16:47:58');
