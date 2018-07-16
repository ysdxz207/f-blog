-- ----------------------------
-- Table structure for afu
-- ----------------------------
DROP TABLE IF EXISTS `afu`;
CREATE TABLE `afu` (
  `id`        VARCHAR(32)  NOT NULL UNIQUE,
  `name`      VARCHAR(128) NOT NULL UNIQUE,
  `type`      VARCHAR(64)           DEFAULT NULL,
  `create_time`     INTEGER(13) DEFAULT '0',
  `content`    VARCHAR(2000)          DEFAULT NULL,
  PRIMARY KEY (`id`)
);
-- ----------------------------
-- Table structure for afu_type
-- ----------------------------
DROP TABLE IF EXISTS `afu_type`;
CREATE TABLE `afu_type` (
  `id`   VARCHAR(32)  NOT NULL UNIQUE,
  `name` VARCHAR(128) NOT NULL UNIQUE,
  `tag` VARCHAR(128) DEFAULT NULL,
  `private_key` VARCHAR(1024) NOT NULL,
  `public_key` VARCHAR(1024) NOT NULL,
  `status` INTEGER(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
);