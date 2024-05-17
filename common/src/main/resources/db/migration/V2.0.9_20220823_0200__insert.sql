/* 22:11:47 local im */ UPDATE `sys_config` SET `remark` = '如果允许，则IM修改为双平台登录，如果不允许，则IM修改为单平台登录' WHERE `id` = '302';
INSERT INTO `sys_config` (`id`, `group`, `item`, `value`, `name`, `remark`, `is_advance`, `is_editable`, `create_time`, `update_time`)
VALUES
    (305, 'PORTAL', 'WEB_LOGIN_MAX_CLIENT', '1', 'WEB端最大允许同时在线', '需要在IM设置【Web 端可同时在线个数】一样的数量', 0, 1, '2018-09-14 09:06:25', '2020-05-07 16:44:53'),
    (306, 'PORTAL', 'MOBILE_LOGIN_MAX_CLIENT', '1', '手机端最大允许同时在线', '需要在IM设置【Android、iPhone、iPad、Windows、Mac、Linux 平台，每种平台可同时在线设备个数】一样的数量', 0, 1, '2018-09-14 09:06:25', '2020-05-07 16:44:53');