DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`id`  int(20) NOT NULL ,
`username`  varchar(128) NOT NULL ,
`password`  varchar(128) NOT NULL ,
`nickname`  varchar(128) NOT NULL ,
PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
`id`  int(20) NOT NULL ,
`creator`  varchar(128) NOT NULL ,
`title`  varchar(256) NOT NULL ,
`context`  text NULL ,
`category`  varchar(128) NULL ,
`tagIds`  varchar(256) NULL ,
`createDate`  int(13) NOT NULL ,
`lastUpdateDate`  int(13) DEFAULT 0,
`status`  int(4) NULL ,
`isDel`  boolean NOT NULL DEFAULT 0 ,
PRIMARY KEY (`id`)
);

