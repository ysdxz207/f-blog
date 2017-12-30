-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id`        INTEGER(20)  NOT NULL UNIQUE,
  `author`      VARCHAR(64) NOT NULL,
  `face_url`    TEXT DEFAULT 'http://puyixiaowo.win/images/tools/book/face_pic.png',
  `a_id`      VARCHAR(128) NOT NULL,
  `name`      VARCHAR(128) NOT NULL,
  `url`      VARCHAR(512)           DEFAULT NULL,
  `last_update_chapter` VARCHAR(64)           DEFAULT NULL,
  `create_time`     INTEGER(13) DEFAULT '0',
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
DROP TABLE IF EXISTS `book_read`;
CREATE TABLE `book_read` (
  `id`        INTEGER(20)  NOT NULL UNIQUE,
  `user_id`      INTEGER(20) NOT NULL,
  `book_id`      INTEGER(20) NOT NULL UNIQUE,
  `source`    VARCHAR(128) NOT NULL,
  `last_reading_chapter`          VARCHAR(128) DEFAULT NULL,
  `last_reading_chapter_link`     TEXT DEFAULT NULL,
  `bg_color`      VARCHAR(64) DEFAULT '#817f79',
  `font_size`        INTEGER(6) DEFAULT '24',
  PRIMARY KEY (`id`)
);
