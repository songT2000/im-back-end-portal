INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)
VALUES
    (1969, 'RSP_MSG', 'IP_NOT_ALLOWED', 'zh-HK', 'IP[{}]禁止访问', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1970, 'RSP_MSG', 'IP_NOT_ALLOWED', 'zh-CN', 'IP[{}]禁止访问', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1971, 'RSP_MSG', 'IP_NOT_ALLOWED', 'en', 'IP[{}] not allowed', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),
    (1972, 'RSP_MSG', 'IP_NOT_ALLOWED', 'ja-JP', 'IP[{}] not allowed', '2020-05-27 14:54:19', '2020-05-27 14:54:19');

/* 16:46:24 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = '2022-08-15 04:30:00'
                        WHERE `type` = 'I18N_TRANSLATE';