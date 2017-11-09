-- ----------------------------
-- Table structure for afu
-- ----------------------------
DROP TABLE IF EXISTS `afu`;
CREATE TABLE `afu` (
  `id`        INTEGER(20)  NOT NULL,
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
  `id`   INTEGER(20)  NOT NULL,
  `name` VARCHAR(128) NOT NULL UNIQUE,
  `private_key` VARCHAR(1024) NOT NULL,
  `public_key` VARCHAR(1024) NOT NULL,
  PRIMARY KEY (`id`)
);