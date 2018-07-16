-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id`              VARCHAR(32)  NOT NULL UNIQUE,
  `menu_id`         VARCHAR(32)  NOT NULL,
  `permission_name` VARCHAR(128) NOT NULL,
  `permission`      VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id`        VARCHAR(32)  NOT NULL UNIQUE,
  `menu_name` VARCHAR(128) NOT NULL,
  `icon`      VARCHAR(64)           DEFAULT NULL,
  `sort`      VARCHAR(64)           DEFAULT NULL,
  `href`      VARCHAR(2000)         DEFAULT NULL,
  `status`    INTEGER(1)            DEFAULT '1',
  `remark`    VARCHAR(256)          DEFAULT NULL,
  `type`      VARCHAR(32)   NOT NULL DEFAULT 'blog',
  `code`      VARCHAR(128)          DEFAULT NULL,
  `pid`       VARCHAR(32)  NOT NULL DEFAULT '0',
  `expand`    INTEGER(1)            DEFAULT '0',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id`        VARCHAR(32) NOT NULL UNIQUE,
  `role_name` VARCHAR(32) NOT NULL,
  `code`      VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id`            VARCHAR(32) NOT NULL UNIQUE,
  `role_id`       VARCHAR(32) NOT NULL,
  `permission_id` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`              VARCHAR(32) NOT NULL UNIQUE,
  `loginname`       VARCHAR(64) NOT NULL,
  `nickname`        VARCHAR(64) NOT NULL,
  `password`        VARCHAR(64) NOT NULL,
  `create_time`     INTEGER(13) DEFAULT '0',
  `last_login_time` INTEGER(13) DEFAULT '0',
  `status`          INTEGER(1)  DEFAULT '1',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id`      VARCHAR(32) NOT NULL UNIQUE,
  `user_id` VARCHAR(32) NOT NULL,
  `role_id` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`)
);

