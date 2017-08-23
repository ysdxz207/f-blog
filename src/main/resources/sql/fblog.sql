DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`       VARCHAR(20)  NOT NULL,
  `username` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `nickname` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id`             INTEGER(20)  NOT NULL,
  `creator`        VARCHAR(128) NOT NULL,
  `title`          VARCHAR(256) NOT NULL,
  `context`        TEXT         NULL,
  `category`       VARCHAR(128) NULL,
  `tagIds`         VARCHAR(256) NULL,
  `createDate`     INTEGER(13)  NOT NULL,
  `lastUpdateDate` INTEGER(13)           DEFAULT 0,
  `status`         INTEGER(4)   NULL,
  `isDel`          BOOLEAN      NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

