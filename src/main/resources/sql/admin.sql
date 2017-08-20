
-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` integer(20) NOT NULL,
  `menu_id` integer(20) NOT NULL,
  `permission_name` varchar(128) NOT NULL,
  `permission` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` integer(20) NOT NULL,
  `menu_name` varchar(128) NOT NULL,
  `icon` varchar(64) DEFAULT NULL,
  `sort` varchar(64) DEFAULT NULL,
  `href` varchar(2000) DEFAULT NULL,
  `status` boolean(1) DEFAULT '1',
  `remark` varchar(256) DEFAULT NULL,
  `type` integer(4) NOT NULL DEFAULT '2',
  `code` varchar(128) DEFAULT NULL,
  `pid` integer(20) NOT NULL DEFAULT '0',
  `expand` boolean(1) DEFAULT '0',
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` integer(20) NOT NULL,
  `role_name` varchar(20) NOT NULL,
  `code` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` integer(20) NOT NULL,
  `role_id` integer(20) NOT NULL,
  `permission_id` integer(20) NOT NULL,
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` integer(20) NOT NULL,
  `loginname` varchar(64) NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `password` varchar(64) NOT NULL,
  `create_time` integer(13) DEFAULT '0',
  `last_login_time` integer(13) DEFAULT '0',
  `status` boolean(1) DEFAULT '1',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` integer(20) NOT NULL,
  `user_id` integer(20) NOT NULL,
  `role_id` integer(20) NOT NULL,
  PRIMARY KEY (`id`)
);

