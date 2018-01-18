DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id`               INTEGER(20)  NOT NULL UNIQUE,
  `creator`          VARCHAR(128) NOT NULL,
  `title`            VARCHAR(256) NOT NULL,
  `context`          TEXT         NULL,
  `category_id`      INTEGER(20)  NULL,
  `create_date`      INTEGER(13)  NOT NULL,
  `last_update_date` INTEGER(13)  NULL,
  `type`             VARCHAR(10)   NOT NULL DEFAULT 'yiyi',
  `status`           INTEGER(4)   NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id`   INTEGER(20)  NOT NULL UNIQUE,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id`   INTEGER(20)  NOT NULL UNIQUE,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag` (
  `id`         INTEGER(20) NOT NULL UNIQUE,
  `article_id` INTEGER(20) NOT NULL,
  `tag_id`     INTEGER(20) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `access_record`;
CREATE TABLE `access_record` (
  `id`         INTEGER(20) NOT NULL UNIQUE,
  `article_id` INTEGER(20),
  `link`     TEXT NOT NULL,
  `user_agent`     TEXT NOT NULL,
  `os`     VARCHAR(128) NOT NULL,
  `browser`     VARCHAR(128) NOT NULL,
  `access_date` INTEGER(13)  NOT NULL,
  `create_date` INTEGER(13)  NOT NULL,
  `ip` VARCHAR(32) NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE (article_id, access_date, ip),
  UNIQUE (link, access_date, ip)
);
