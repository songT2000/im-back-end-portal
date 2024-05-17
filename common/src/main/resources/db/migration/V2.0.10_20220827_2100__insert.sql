INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`, `update_time`)
VALUES
    (20113, 'portal-user-enable-disable', '2', 201, 'ADMIN_MENU.ENABLE_DISABLE#I18N', '/api/admin/auth/portal-user/enable-disable', NULL, 13, 1, '2019-11-18 12:58:17', '2019-11-18 12:58:19'),
    (20114, 'portal-user-add-balance', '2', 201, 'ADMIN_MENU.ADD_BALANCE#I18N', '/api/admin/auth/portal-user/add-balance', NULL, 14, 1, '2019-11-18 12:58:17', '2019-11-18 12:58:19');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES
    (1981, 'ADMIN_MENU', 'ADD_BALANCE', 'zh-CN', '增减余额', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1982, 'ADMIN_MENU', 'ADD_BALANCE', 'en', 'Add balance', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1983, 'ADMIN_MENU', 'ADD_BALANCE', 'ja-JP', 'Add balance', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1984, 'ADMIN_MENU', 'ADD_BALANCE', 'zh-HK', '增减余额', '2020-05-27 14:54:19', '2020-05-27 14:54:19');
