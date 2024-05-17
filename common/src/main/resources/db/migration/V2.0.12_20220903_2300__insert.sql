CREATE TABLE `portal_user_emoji`
(
    `id`          bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     bigint(19)       NOT NULL COMMENT '链接',
    `url`         varchar(512)        NOT NULL DEFAULT '' COMMENT '链接',
    `create_time` datetime            NOT NULL COMMENT '创建时间',
    `update_time` datetime            NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户自定义表情';