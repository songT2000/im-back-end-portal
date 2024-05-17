DROP TABLE IF EXISTS `app_auto_reply_config`;
CREATE TABLE `app_auto_reply_config`
(
    `id`          bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `usernames`   varchar(500)        NOT NULL DEFAULT '' COMMENT '管理员用户账号集合，多个使用逗号分割',
    `msg_type`    varchar(50)         NOT NULL DEFAULT '' COMMENT '消息类型，TIMTextElem图片消息，TIMImageElem文本消息',
    `content`     varchar(2000)       NOT NULL DEFAULT '' COMMENT '自动回复内容',
    `start_time`  varchar(50)         NOT NULL DEFAULT '' COMMENT '自动回复的开始时间,格式：HH:mm:ss',
    `end_time`    varchar(50)         NOT NULL DEFAULT '' COMMENT '自动回复的结束时间,格式：HH:mm:ss',
    `note`        varchar(2000)       NULL     DEFAULT '' COMMENT '存储图片消息元素',
    `create_time` datetime            NOT NULL COMMENT '创建时间',
    `update_time` datetime            NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='自动回复配置';


INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (607, 'app-auto-reply-config', '1', 6, 'ADMIN_MENU.APP_AUTO_REPLY_CONFIG#I18N', '/app-auto-reply-config', NULL,
        7, 1,
        '2021-09-03 18:40:40', '2021-09-03 18:40:40');


INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (60701, 'app-auto-reply-config-page', '2', 607, 'ADMIN_MENU.LIST#I18N',
        '/api/admin/auth/app-auto-reply-config/page',
        NULL, 1, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (60702, 'app-auto-reply-config-add', '2', 607, 'ADMIN_MENU.ADD#I18N',
        '/api/admin/auth/app-auto-reply-config/add',
        NULL, 2, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (60703, 'app-auto-reply-config-delete', '2', 607, 'ADMIN_MENU.DELETE#I18N',
        '/api/admin/auth/app-auto-reply-config/delete',
        NULL, 4, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18');


INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1965, 'ADMIN_MENU', 'APP_AUTO_REPLY_CONFIG', 'zh-CN', '自动回复配置', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1966, 'ADMIN_MENU', 'APP_AUTO_REPLY_CONFIG', 'en', 'Auto reply config', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1967, 'ADMIN_MENU', 'APP_AUTO_REPLY_CONFIG', 'ja-JP', '自動返信構成', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1968, 'ADMIN_MENU', 'APP_AUTO_REPLY_CONFIG', 'zh-HK', '自動回復配寘', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

/* 16:46:24 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'ADMIN_MENU';
/* 16:46:29 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'ADMIN_ROLE_MENU';
/* 16:46:33 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'I18N_TRANSLATE';


ALTER TABLE `tim_group`
    ADD COLUMN `exit_enabled` tinyint(1) NULL DEFAULT 1 COMMENT '退出群组权限，默认打开' AFTER `show_member_enabled`;
ALTER TABLE `tim_group`
    ADD COLUMN `msg_interval_time` int NULL DEFAULT 0 COMMENT '普通成员发消息间隔时间，单位秒' AFTER `show_member_enabled`;