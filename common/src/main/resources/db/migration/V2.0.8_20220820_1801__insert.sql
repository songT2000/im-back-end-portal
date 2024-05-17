CREATE TABLE `portal_user_device_info`
(
    `id`             bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`        bigint(19)          NOT NULL DEFAULT 0 COMMENT '用户ID',
    `device_type`    varchar(200)        NULL     DEFAULT '' COMMENT '设备类型，Android或iOS',
    `device_brand`   varchar(200)        NULL     DEFAULT '' COMMENT '手机厂商',
    `system_model`   varchar(200)        NULL     DEFAULT '' COMMENT '手机型号',
    `system_version` varchar(200)        NULL     DEFAULT '' COMMENT '系统版本',
    `app_version`    varchar(200)        NULL     DEFAULT '' COMMENT '软件版本',
    `create_time`    datetime            NOT NULL COMMENT '创建时间',
    `update_time`    datetime            NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户设备信息';