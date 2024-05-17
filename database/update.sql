-- 更新到生产环境后，执行完成后，请删除本文件

-- 3.15变更如下
-- 群组表增加5个权限配置类属性 -------------------------------------------------------------------------------------------------


ALTER TABLE tim_group
    ADD show_member_enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '显示群内成员权限，默认显示' AFTER remark;

ALTER TABLE tim_group
    ADD upload_enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '上传权限，默认打开' AFTER remark;

ALTER TABLE tim_group
    ADD add_member_enabled TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否允许普通成员拉人进群，默认关闭' AFTER remark;

ALTER TABLE tim_group
    ADD anonymous_chat_enabled TINYINT(1) NOT NULL DEFAULT 0 COMMENT '群内成员私聊权限，默认关闭' AFTER remark;

ALTER TABLE tim_group
    ADD add_friend_enabled TINYINT(1) NOT NULL DEFAULT 0 COMMENT '群内私加好友权限，默认关闭' AFTER remark;

-- 群组表增加5个权限配置类属性 -------------------------------------------------------------------------------------------------


-- 3.25变更如下
-- 导航管理 -------------------------------------------------------------------------------------------------
CREATE TABLE `portal_navigator`
(
    `id`          bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(256)        NOT NULL DEFAULT '' COMMENT '名称',
    `url`         varchar(512)        NOT NULL DEFAULT '' COMMENT '链接',
    `sort`        int(11)             NOT NULL COMMENT '排序号',
    `is_enabled`  tinyint(1) unsigned NOT NULL COMMENT '是否启用',
    `create_time` datetime            NOT NULL COMMENT '创建时间',
    `update_time` datetime            NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='前台导航';

INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (808, 'portal-navigator', '1', 8, 'ADMIN_MENU.PORTAL_NAVIGATOR#I18N', '/portal-navigator', NULL, 8, 1,
        '2021-09-03 18:40:40', '2021-09-03 18:40:40');


INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (80801, 'portal-navigator-page', '2', 808, 'ADMIN_MENU.LIST#I18N', '/api/admin/auth/portal-navigator/page', NULL,
        1, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (80802, 'portal-navigator-add', '2', 808, 'ADMIN_MENU.ADD#I18N', '/api/admin/auth/portal-navigator/add', NULL, 2,
        1, '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (80803, 'portal-navigator-edit', '2', 808, 'ADMIN_MENU.EDIT#I18N', '/api/admin/auth/portal-navigator/edit', NULL,
        3, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (80804, 'portal-navigator-delete', '2', 808, 'ADMIN_MENU.DELETE#I18N', '/api/admin/auth/portal-navigator/delete',
        NULL, 4, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18');


INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1877, 'ADMIN_MENU', 'PORTAL_NAVIGATOR', 'zh-CN', '前台导航管理', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1878, 'ADMIN_MENU', 'PORTAL_NAVIGATOR', 'en', 'Navigator MGT', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1879, 'ADMIN_MENU', 'PORTAL_NAVIGATOR', 'ja-JP', 'フロントナビゲーション', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1880, 'ADMIN_MENU', 'PORTAL_NAVIGATOR', 'zh-HK', '前台導航管理', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

/* 16:46:24 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'ADMIN_MENU';
/* 16:46:29 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'ADMIN_ROLE_MENU';
/* 16:46:33 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'I18N_TRANSLATE';
-- 导航管理 -------------------------------------------------------------------------------------------------


-- 4.6变更如下
-- 群文件 -------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tim_group_file`;
CREATE TABLE `tim_group_file`
(
    `id`          bigint unsigned                                               NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `group_id`    varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '群ID',
    `file_name`   varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名称',
    `url`         varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件地址',
    `size`        decimal(11, 2)                                                NOT NULL COMMENT '文件大小，单位kb',
    `postfix`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT '文件后缀',
    `create_time` datetime                                                      NOT NULL COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='群文件';


-- 群文件 -------------------------------------------------------------------------------------------------

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1885, 'RSP_MSG', 'ADD_FRIEND_SUCCESS_MESSAGE', 'zh-HK', '我們已成為好友，現在可以開始聊天啦', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1886, 'RSP_MSG', 'ADD_FRIEND_SUCCESS_MESSAGE', 'zh-CN', '我们已成为好友，现在可以开始聊天啦', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1887, 'RSP_MSG', 'ADD_FRIEND_SUCCESS_MESSAGE', 'ja-JP', '私たちはもう友達になりました。今からおしゃべりができます。', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1888, 'RSP_MSG', 'ADD_FRIEND_SUCCESS_MESSAGE', 'en', 'We have become good friends. Now we can start chatting',
        '2020-05-27 14:54:19', '2020-05-27 14:54:19');

INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (60111, 'tim-group-file-page', '2', 601, 'ADMIN_MENU.LIST#I18N', '/api/admin/auth/tim-group-file/list', NULL, 11,
        1, '2019-11-06 23:37:00', '2021-04-05 22:11:18'),
       (60112, 'tim-group-file-add', '2', 601, 'ADMIN_MENU.ADD#I18N', '/api/admin/auth/tim-group-file/add', NULL, 12, 1,
        '2019-11-06 23:37:00', '2021-04-05 22:11:18'),
       (60113, 'tim-group-file-delete', '2', 601, 'ADMIN_MENU.DELETE#I18N', '/api/admin/auth/tim-group-file/delete',
        NULL, 13, 1, '2019-11-06 23:37:00', '2021-04-05 22:11:18');

-- 4.7新增用户，默认头像，默认好友 -------------------------------------------------------------------------------------------------
CREATE TABLE `sys_default_avatar`
(
    `id`          bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `url`         varchar(1024)       NOT NULL DEFAULT '' COMMENT 'URL',
    `create_time` datetime            NOT NULL COMMENT '创建时间',
    `update_time` datetime            NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_url` (`url`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='系统默认头像';

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1889, 'RSP_MSG', 'USER_USERNAME_NOT_EXISTED', 'zh-CN', '用户名[{}]不存在！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1890, 'RSP_MSG', 'USER_USERNAME_NOT_EXISTED', 'en', 'Username [{}] does not exist!', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1891, 'RSP_MSG', 'USER_USERNAME_NOT_EXISTED', 'ja-JP', 'ユーザー名[{}は存在しません！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1892, 'RSP_MSG', 'USER_USERNAME_NOT_EXISTED', 'zh-HK', '用戶名[{}]不存在！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19');
-- 4.7新增用户，默认头像，默认好友 -------------------------------------------------------------------------------------------------

-- 4.8前台导航点击数据收集 -------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `portal_navigator_click`;
CREATE TABLE `portal_navigator_click`
(
    `id`                  bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `portal_navigator_id` bigint unsigned NOT NULL COMMENT '前台链接ID',
    `user_id`             bigint unsigned NOT NULL COMMENT '用户ID',
    `create_time`         datetime        NOT NULL COMMENT '创建时间',
    `update_time`         datetime        NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='前台导航点击数据收集';
-- 注意，此行之前的已经执行 -------------------------------------------------------------------------------------------------


-- 4.16表情包-------------------------------------------------------------------------------------------------------------

-- ----------------------------
-- Table structure for tim_face
-- ----------------------------
DROP TABLE IF EXISTS `tim_face`;
CREATE TABLE `tim_face`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `chat_panel_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '聊天面板图标，50*50',
    `face_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '专辑名称',
    `create_time`     datetime            NOT NULL COMMENT '创建时间',
    `update_time`     datetime            NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 348
  DEFAULT CHARSET = utf8mb4 COMMENT ='表情包专辑';

-- ----------------------------
-- Table structure for tim_face_item
-- ----------------------------
DROP TABLE IF EXISTS `tim_face_item`;
CREATE TABLE `tim_face_item`
(
    `id`            bigint(20) unsigned                                    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tim_face_id`   bigint(20)                                             NOT NULL COMMENT '表情包专辑ID',
    `face_index`    int(11)                                                NOT NULL COMMENT '表情包序号',
    `face_url`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主图地址 240*240',
    `thumbnail_url` varchar(255)                                           NOT NULL COMMENT '缩略图地址 120*120',
    `create_time`   datetime                                               NOT NULL COMMENT '创建时间',
    `update_time`   datetime                                               NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 348
  DEFAULT CHARSET = utf8mb4 COMMENT ='表情包元素';

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1905, 'ADMIN_MENU', 'PORTAL_NAVIGATOR_STATISTIC', 'zh-CN', '前台导航统计', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1906, 'ADMIN_MENU', 'PORTAL_NAVIGATOR_STATISTIC', 'en', 'Portal navigation statistics', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1907, 'ADMIN_MENU', 'PORTAL_NAVIGATOR_STATISTIC', 'zh-HK', '前臺導航統計', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1908, 'ADMIN_MENU', 'PORTAL_NAVIGATOR_STATISTIC', 'ja-JP', 'ナビゲーション統計', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19');

INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (809, 'portal-navigator-statistic', '1', 8, 'ADMIN_MENU.PORTAL_NAVIGATOR_STATISTIC#I18N',
        '/portal-navigator-statistic', NULL, 8, 1,
        '2021-09-03 18:40:40', '2021-09-03 18:40:40');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1909, 'ADMIN_MENU', 'EMOJI_MANAGE', 'zh-CN', '表情管理', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1910, 'ADMIN_MENU', 'EMOJI_MANAGE', 'en', 'Emoji manage', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1911, 'ADMIN_MENU', 'EMOJI_MANAGE', 'zh-HK', '表情管理', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1912, 'ADMIN_MENU', 'EMOJI_MANAGE', 'ja-JP', '表情管理', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (810, 'emoji-manage', '1', 8, 'ADMIN_MENU.EMOJI_MANAGE#I18N', '/emoji-manage', NULL, 8, 1,
        '2021-09-03 18:40:40', '2021-09-03 18:40:40');

INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (80901, 'portal-navigator-statistic-list', '2', 809, 'ADMIN_MENU.STATISTIC#I18N',
        '/api/admin/auth/portal-navigator/statistic/list', NULL,
        1, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (80902, 'portal-navigator-statistic-detail', '2', 809, 'ADMIN_MENU.STATISTIC_DETAIL#I18N',
        '/api/admin/auth/portal-navigator/statistic/detail', NULL, 2,
        1, '2019-11-18 12:58:00', '2021-04-05 22:11:18');


INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (81001, 'tim-face-list', '2', 810, 'ADMIN_MENU.LIST#I18N', '/api/admin/auth/tim-face/list', NULL, 1, 1,
        '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (81002, 'tim-face-add', '2', 810, 'ADMIN_MENU.FACE_ADD#I18N', '/api/admin/auth/tim-face/add', NULL, 2, 1,
        '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (81003, 'tim-face-delete', '2', 810, 'ADMIN_MENU.FACE_DELETE#I18N', '/api/admin/auth/tim-face/delete', NULL, 3,
        1, '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (81004, 'tim-face-item-add', '2', 810, 'ADMIN_MENU.FACE_ITEM_ADD#I18N', '/api/admin/auth/tim-face-item/add',
        NULL, 4, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (81005, 'tim-face-item-delete', '2', 810, 'ADMIN_MENU.FACE_ITEM_DELETE#I18N',
        '/api/admin/auth/tim-face-item/delete', NULL, 5, 1, '2019-11-18 12:58:00', '2021-04-05 22:11:18');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1913, 'ADMIN_MENU', 'STATISTIC', 'zh-CN', '统计', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1914, 'ADMIN_MENU', 'STATISTIC', 'en', 'Statistics', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1915, 'ADMIN_MENU', 'STATISTIC', 'zh-HK', '統計', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1916, 'ADMIN_MENU', 'STATISTIC', 'ja-JP', '統計', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1917, 'ADMIN_MENU', 'STATISTIC_DETAIL', 'zh-CN', '统计明细', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1918, 'ADMIN_MENU', 'STATISTIC_DETAIL', 'en', 'Statistical details', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1919, 'ADMIN_MENU', 'STATISTIC_DETAIL', 'zh-HK', '統計明細', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1920, 'ADMIN_MENU', 'STATISTIC_DETAIL', 'ja-JP', '統計詳細', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1921, 'ADMIN_MENU', 'FACE_ADD', 'zh-CN', '新增专辑', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1922, 'ADMIN_MENU', 'FACE_ADD', 'en', 'New album', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1923, 'ADMIN_MENU', 'FACE_ADD', 'zh-HK', '新增專輯', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1924, 'ADMIN_MENU', 'FACE_ADD', 'ja-JP', 'ニューアルバム', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1925, 'ADMIN_MENU', 'FACE_DELETE', 'zh-CN', '删除专辑', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1926, 'ADMIN_MENU', 'FACE_DELETE', 'en', 'New album', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1927, 'ADMIN_MENU', 'FACE_DELETE', 'zh-HK', '删除專輯', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1928, 'ADMIN_MENU', 'FACE_DELETE', 'ja-JP', 'アルバムを削除', '2020-05-27 14:54:19', '2020-05-27 14:54:19');


INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1929, 'ADMIN_MENU', 'FACE_ITEM_ADD', 'zh-CN', '新增表情', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1930, 'ADMIN_MENU', 'FACE_ITEM_ADD', 'en', 'New emoji', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1931, 'ADMIN_MENU', 'FACE_ITEM_ADD', 'zh-HK', '新增表情', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1932, 'ADMIN_MENU', 'FACE_ITEM_ADD', 'ja-JP', '新しい表情', '2020-05-27 14:54:19', '2020-05-27 14:54:19');


INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1933, 'ADMIN_MENU', 'FACE_ITEM_DELETE', 'zh-CN', '删除表情', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1934, 'ADMIN_MENU', 'FACE_ITEM_DELETE', 'en', 'Delete emoji', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1935, 'ADMIN_MENU', 'FACE_ITEM_DELETE', 'zh-HK', '删除表情', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1936, 'ADMIN_MENU', 'FACE_ITEM_DELETE', 'ja-JP', '表情を削除', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

-- 5.17版本需要更新 ------

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1937, 'RSP_MSG', 'OFFLINE_PUSH_INFO_MESSAGE', 'zh-HK', '您有一條新訊息', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1938, 'RSP_MSG', 'OFFLINE_PUSH_INFO_MESSAGE', 'zh-CN', '您有一条新消息', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1939, 'RSP_MSG', 'OFFLINE_PUSH_INFO_MESSAGE', 'ja-JP', '新しいメッセージがあります', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1940, 'RSP_MSG', 'OFFLINE_PUSH_INFO_MESSAGE', 'en', 'You have a new message', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19');

-- 6.1版本 --

ALTER TABLE `portal_user`
    ADD COLUMN `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '启/禁状态' AFTER `add_friend_enabled`;

-- 6.10版本 --

ALTER TABLE `personal_red_envelope`
    ADD COLUMN `msg_key` varchar(100) NULL COMMENT 'IM的消息ID，用于后续更新发红包消息的状态' AFTER `remark`;


ALTER TABLE `group_red_envelope`
    ADD COLUMN `msg_seq` bigint(20) NULL COMMENT 'IM的群消息ID，用于后续更新发红包消息的状态' AFTER `remark`;

-- 6.16变更如下
-- 敏感词管理 -------------------------------------------------------------------------------------------------
CREATE TABLE `sensitive_word`
(
    `id`          bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `word`        varchar(256)        NOT NULL DEFAULT '' COMMENT '敏感词',
    `create_time` datetime            NOT NULL COMMENT '创建时间',
    `update_time` datetime            NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='敏感词管理';



INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1941, 'ADMIN_MENU', 'SENSITIVE_WORD_MANAGE', 'zh-CN', '敏感词管理', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1942, 'ADMIN_MENU', 'SENSITIVE_WORD_MANAGE', 'en', 'Sensitive word management', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1943, 'ADMIN_MENU', 'SENSITIVE_WORD_MANAGE', 'zh-HK', '敏感詞管理', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1944, 'ADMIN_MENU', 'SENSITIVE_WORD_MANAGE', 'ja-JP', '敏感語管理', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (812, 'sensitive-word-manage', '1', 8, 'ADMIN_MENU.SENSITIVE_WORD_MANAGE#I18N', '/sensitive-word-manage', NULL,
        8, 1,
        '2021-09-03 18:40:40', '2021-09-03 18:40:40');

INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (81201, 'sensitive-word-list', '2', 812, 'ADMIN_MENU.LIST#I18N', '/api/admin/auth/sensitive-word/page', NULL, 1,
        1,
        '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (81202, 'sensitive-word-add', '2', 812, 'ADMIN_MENU.SENSITIVE_WORD_ADD#I18N',
        '/api/admin/auth/sensitive-word/add', NULL, 2, 1,
        '2019-11-18 12:58:00', '2021-04-05 22:11:18'),
       (81203, 'sensitive-word-delete', '2', 812, 'ADMIN_MENU.SENSITIVE_WORD_DELETE#I18N',
        '/api/admin/auth/sensitive-word/delete',
        NULL, 3,
        1, '2019-11-18 12:58:00', '2021-04-05 22:11:18');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1945, 'ADMIN_MENU', 'SENSITIVE_WORD_ADD', 'zh-CN', '新增敏感词', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1946, 'ADMIN_MENU', 'SENSITIVE_WORD_ADD', 'en', 'New sensitive words', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1947, 'ADMIN_MENU', 'SENSITIVE_WORD_ADD', 'zh-HK', '新增敏感詞', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1948, 'ADMIN_MENU', 'SENSITIVE_WORD_ADD', 'ja-JP', '新規敏感語', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1949, 'ADMIN_MENU', 'SENSITIVE_WORD_DELETE', 'zh-CN', '删除敏感词', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1950, 'ADMIN_MENU', 'SENSITIVE_WORD_DELETE', 'en', 'Delete sensitive words', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1951, 'ADMIN_MENU', 'SENSITIVE_WORD_DELETE', 'zh-HK', '删除敏感詞', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1952, 'ADMIN_MENU', 'SENSITIVE_WORD_DELETE', 'ja-JP', '敏感語の削除', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

-- 6.26变更如下

INSERT INTO `sys_config` (`id`, `group`, `item`, `value`, `name`, `remark`,
                          `is_advance`, `is_editable`, `create_time`, `update_time`)
VALUES (108, 'GLOBAL', 'PORTAL_ADMIN_ACCOUNT', '', '前台管理员账号', '配置管理员账号用于接收一些系统消息,多个管理员账号请使用逗号分割', 1, 1,
        '2019-10-25 20:09:15', '2020-04-21 11:41:15');

/* 16:46:24 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'SYS_CONFIG';


INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1953, 'RSP_MSG', 'WITHDRAW_SYSTEM_MESSAGE', 'zh-HK', '[系統消息]平臺有新的提現申請訂單，請登入管理後臺處理！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1954, 'RSP_MSG', 'WITHDRAW_SYSTEM_MESSAGE', 'zh-CN', '[系统消息]平台有新的提现申请订单，请登录管理后台处理！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1955, 'RSP_MSG', 'WITHDRAW_SYSTEM_MESSAGE', 'ja-JP',
        '[システムメッセージ]プラットフォームには新しい現金化申請注文がありますので、管理バックグラウンドにログインして処理してください！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1956, 'RSP_MSG', 'WITHDRAW_SYSTEM_MESSAGE', 'en',
        '[system message] there are new withdrawal application orders on the platform. Please log in to the management background for processing!',
        '2020-05-27 14:54:19',
        '2020-05-27 14:54:19');


INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1957, 'RSP_MSG', 'RECHARGE_SYSTEM_MESSAGE', 'zh-HK', '[系統消息]平臺有新的充值申請訂單，請登入管理後臺處理！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1958, 'RSP_MSG', 'RECHARGE_SYSTEM_MESSAGE', 'zh-CN', '[系统消息]平台有新的充值申请订单，请登录管理后台处理！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1959, 'RSP_MSG', 'RECHARGE_SYSTEM_MESSAGE', 'ja-JP',
        '[システムメッセージ]プラットフォームに新しいチャージ申請注文がありますので、管理バックグラウンドにログインして処理してください！', '2020-05-27 14:54:19',
        '2020-05-27 14:54:19'),
       (1960, 'RSP_MSG', 'RECHARGE_SYSTEM_MESSAGE', 'en',
        '[system message] there is a new recharge application order on the platform. Please log in to the management background for processing!',
        '2020-05-27 14:54:19',
        '2020-05-27 14:54:19');


-- 6.28新增用户app操作日志表
CREATE TABLE `portal_user_app_operation_log`
(
    `id`             bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`        bigint(19) unsigned NOT NULL COMMENT '操作的用户ID',
    `to_user_id`     bigint(19)                   DEFAULT NULL COMMENT '对面的用户ID',
    `operation_type` varchar(100)        NOT NULL DEFAULT '' COMMENT '操作类型',
    `group_id`       varchar(100)                 DEFAULT NULL COMMENT '操作的群组ID',
    `content`        varchar(2000)       NOT NULL DEFAULT '' COMMENT '操作内容',
    `client_ip`      varchar(256)                 DEFAULT '' COMMENT '客户端IP地址',
    `opt_platform`   varchar(512)        NOT NULL DEFAULT '' COMMENT '客户端平台',
    `create_time`    datetime            NOT NULL COMMENT '创建时间',
    `update_time`    datetime            NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`) USING BTREE,
    KEY `idx_operation_type` (`operation_type`) USING BTREE,
    KEY `idx_create_time` (`create_time`) USING BTREE,
    KEY `idx_to_user_id` (`to_user_id`) USING BTREE,
    KEY `idx_group_id` (`group_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1541375176602517507
  DEFAULT CHARSET = utf8 COMMENT ='app用户操作日志（包含加好友、黑名单等等）';

ALTER TABLE `portal_user_profile`
    ADD COLUMN `is_internal_user` tinyint NOT NULL DEFAULT 0 COMMENT '是否内部账号，默认否' AFTER `total_withdraw_amount`,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`id`) USING BTREE;
