DROP TABLE IF EXISTS `t_note`;
CREATE TABLE `t_note` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `note_title` varchar(255) DEFAULT NULL,
  `note_content` varchar(255) DEFAULT NULL,
  `note_date` datetime DEFAULT NULL,
  PRIMARY KEY (`note_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_note
-- ----------------------------
INSERT INTO `t_note` VALUES ('1', '10', '测试标题', '阿斯大法阿斯大法', '2016-07-14 21:17:21');
