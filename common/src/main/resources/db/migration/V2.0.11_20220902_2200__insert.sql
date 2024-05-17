ALTER TABLE portal_user_device_info
    ADD device_unique varchar(100) NULL DEFAULT NULL COMMENT '设备唯一标识符' AFTER app_version;