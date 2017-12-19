-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id`        INTEGER(20)  NOT NULL UNIQUE,
  `author`      VARCHAR(64) NOT NULL,
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
  `user_id`      INTEGER(20) NOT NULL UNIQUE,
  `book_ids`      VARCHAR(128) DEFAULT '',
  `create_time`     INTEGER(13) DEFAULT '0',
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for book_chapter
-- ----------------------------
DROP TABLE IF EXISTS `book_chapter`;
CREATE TABLE `book_chapter` (
  `id`        INTEGER(20)  NOT NULL UNIQUE,
  `book_id`      INTEGER(20) NOT NULL,
  `name`      VARCHAR(128) DEFAULT NULL,
  `content`      VARCHAR(5000) DEFAULT NULL ,
  `link`     VARCHAR (500) DEFAULT NULL,
  `status`     INTEGER(2) DEFAULT '1',
  PRIMARY KEY (`id`)
);