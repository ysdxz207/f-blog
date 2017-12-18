-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id`        INTEGER(20)  NOT NULL UNIQUE,
  `a_id`      VARCHAR(128) NOT NULL,
  `name`      VARCHAR(128) NOT NULL,
  `url`      VARCHAR(512)           DEFAULT NULL,
  `last_update_chapter` VARCHAR(64)           DEFAULT NULL,
  `create_time`     INTEGER(13) DEFAULT '0',
  `last_update_time`     INTEGER(13) DEFAULT '0',
  `is_over`     INTEGER(2) DEFAULT '0',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for bookshelf
-- ----------------------------
DROP TABLE IF EXISTS `bookshelf`;
CREATE TABLE `bookshelf` (
  `id`        INTEGER(20)  NOT NULL UNIQUE,
  `user_id`      INTEGER(20) NOT NULL,
  `book_ids`      VARCHAR(128) DEFAULT '',
  `create_time`     INTEGER(13) DEFAULT '0',
  PRIMARY KEY (`id`)
);