truncate t_fragment;
truncate t_scene;
truncate t_share_fragment;
truncate t_variable;

/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : 127.0.0.1:3306
Source Database       : query

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-05-18 23:00:13
*/

SET FOREIGN_KEY_CHECKS=0;

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
  `share_fragment_git` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`share_fragment_id`),
  UNIQUE KEY `share_fragment_no_unique` (`share_fragment_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_share_fragment
-- ----------------------------
INSERT INTO `t_share_fragment` VALUES ('3', '1', 'FRGMaa3a42fc184f4943b0f6035baedec0fd', 'foxQuery', 'query', null, '我的第一个分享fragment', '0', '0', '2016-05-18 22:48:41', '0f656605cba2cdd2edf6e7f02935cc6183c81880', '1463582916514');
