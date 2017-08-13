DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`id`  varchar(128) NOT NULL ,
`username`  varchar(128) NOT NULL ,
`password`  varchar(128) NOT NULL ,
`nickname`  varchar(128) NOT NULL ,
PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
`id`  varchar(128) NOT NULL ,
`creator`  varchar(128) NOT NULL ,
`title`  varchar(256) NOT NULL ,
`context`  text NULL ,
`category`  varchar(128) NULL ,
`tagIds`  varchar(256) NULL ,
`createDate`  datetime NOT NULL ,
`lastUpdateDate`  datetime NULL ,
`status`  int(4) NULL ,
`isDel`  bit(1) NOT NULL DEFAULT 0 ,
PRIMARY KEY (`id`)
);

