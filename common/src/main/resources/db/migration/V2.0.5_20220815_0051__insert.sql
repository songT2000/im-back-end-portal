ALTER TABLE `portal_user_profile`
    ADD COLUMN `is_internal_user` tinyint NOT NULL DEFAULT 0 COMMENT '是否内部账号，默认否' AFTER `total_withdraw_amount`,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`id`) USING BTREE;