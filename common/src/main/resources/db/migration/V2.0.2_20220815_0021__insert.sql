/* 02:31:00 local im */ ALTER TABLE `portal_ip_black_white` ADD `usernames` VARCHAR(3096)  NULL  DEFAULT NULL  COMMENT '用户名列表，多个用英文逗号分割，没有就是全局'  AFTER `id`;
/* 03:04:46 local im */ ALTER TABLE `portal_ip_black_white` CHANGE `ip` `ip` VARCHAR(256)  CHARACTER SET utf8  COLLATE utf8_general_ci  NOT NULL  DEFAULT ''  COMMENT 'IP，全文匹配，支持IPV4/IPV4掩码/IPV4段/IPV6格式，多个用英文逗号分割';
