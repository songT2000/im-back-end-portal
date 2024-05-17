/* 16:46:24 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'ADMIN_MENU';
/* 16:46:29 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'ADMIN_ROLE_MENU';
/* 16:46:33 local im */ UPDATE `sys_cache_refresh`
                        SET `update_time` = now()
                        WHERE `type` = 'I18N_TRANSLATE';