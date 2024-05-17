# 修改错误的设备类型
update portal_user_device_info
set device_type = '4'
where device_brand = 'Apple';
update portal_user_device_info
set device_type = '3'
where device_brand != 'Apple';