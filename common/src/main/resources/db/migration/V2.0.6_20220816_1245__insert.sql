INSERT INTO `admin_menu` (`id`, `code`, `type`, `parent_id`, `name`, `url`, `icon`, `sort`, `is_enabled`, `create_time`, `update_time`)
VALUES
    (20112, 'portal-user-edit-internal-user', '2', 201, 'ADMIN_MENU.EDIT_INTERNAL_USER#I18N', '/api/admin/auth/portal-user/edit-internal-user', NULL, 12, 1, '2019-11-18 12:58:17', '2019-11-18 12:58:19');

INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES
    (1973, 'ADMIN_MENU', 'EDIT_INTERNAL_USER', 'zh-CN', '编辑内部用户', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1974, 'ADMIN_MENU', 'EDIT_INTERNAL_USER', 'en', 'Edit Internal user', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1975, 'ADMIN_MENU', 'EDIT_INTERNAL_USER', 'ja-JP', 'Edit Internal user', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1976, 'ADMIN_MENU', 'EDIT_INTERNAL_USER', 'zh-HK', '编辑内部用户', '2020-05-27 14:54:19', '2020-05-27 14:54:19');
