ALTER TABLE portal_user_profile
    ADD device_unique varchar(100) NULL DEFAULT NULL COMMENT '设备唯一标识符' AFTER admin_remark;