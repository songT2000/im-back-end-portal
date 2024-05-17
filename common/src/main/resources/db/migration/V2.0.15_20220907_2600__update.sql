INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`,
                          `update_time`)
VALUES (20115, 'portal-user-device-info', '2', 201, 'ADMIN_MENU.DEVICE_INFO_PAGE#I18N',
        '/api/admin/auth/device-info/page', NULL, 15, 1, '2019-11-18 12:58:17', '2019-11-18 12:58:19');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES (1985, 'ADMIN_MENU', 'DEVICE_INFO_PAGE', 'zh-CN', '设备信息分页', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1986, 'ADMIN_MENU', 'DEVICE_INFO_PAGE', 'en', 'Device info page', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1987, 'ADMIN_MENU', 'DEVICE_INFO_PAGE', 'ja-JP', '設備資訊分頁', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
       (1988, 'ADMIN_MENU', 'DEVICE_INFO_PAGE', 'zh-HK', 'デバイス情報ページング', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

