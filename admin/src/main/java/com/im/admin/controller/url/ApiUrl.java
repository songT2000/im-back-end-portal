package com.im.admin.controller.url;

/**
 * 定义API地址
 *
 * @author Barry
 * @date 2019-11-07
 */
public final class ApiUrl {
    public static final String BASE_NO_AUTH_URL = "/api/admin";
    public static final String BASE_AUTH_URL = "/api/admin/auth";
    /**
     * 系统测速地址 分页
     */
    public static final String SYS_CIRCUIT_PAGE = BASE_AUTH_URL + "/sys-circuit/page";
    /**
     * 系统测速地址 新增
     */
    public static final String SYS_CIRCUIT_ADD = BASE_AUTH_URL + "/sys-circuit/add";
    /**
     * 系统测速地址 编辑
     */
    public static final String SYS_CIRCUIT_EDIT = BASE_AUTH_URL + "/sys-circuit/edit";
    /**
     * 系统测速地址启/禁
     */
    public static final String SYS_CIRCUIT_ENABLE_DISABLE = BASE_AUTH_URL + "/sys-circuit/enable-disable";
    /**
     * 系统测速地址 删除
     */
    public static final String SYS_CIRCUIT_DELETE = BASE_AUTH_URL + "/sys-circuit/delete";
    /**
     * 用户账变导出
     */
    public static final String SYS_CIRCUIT_EXPORT = BASE_AUTH_URL + "/sys-circuit/export";
    /**
     * 待办事项
     */
    public static final String TODO = BASE_AUTH_URL + "/todo";
    /**
     * 管理用户登录
     **/
    public static final String LOGIN = BASE_NO_AUTH_URL + "/user/login";
    /**
     * 管理用户登出
     **/
    public static final String LOGOUT = BASE_NO_AUTH_URL + "/user/logout";
    /**
     * 获取当前登录用户信息，与登录请求返回的数据一致
     **/
    public static final String GET_LOGIN_INFO = BASE_NO_AUTH_URL + "/user/get-login-info";
    /**
     * 获取用户是否已经绑定谷歌
     **/
    public static final String HAS_BOUND_GOOGLE = BASE_NO_AUTH_URL + "/user/has-bound-google";
    /**
     * 绑定谷歌
     **/
    public static final String BIND_GOOGLE = BASE_NO_AUTH_URL + "/user/bind-google";
    /**
     * 修改当前登录用户登录密码
     **/
    public static final String EDIT_LOGIN_PWD = BASE_AUTH_URL + "/user/edit-login-pwd";
    /**
     * 后台用户列表，仅能列出下级用户
     **/
    public static final String ADMIN_USER_PAGE = BASE_AUTH_URL + "/admin-user/page";
    /**
     * 检查用户名是否存在
     **/
    public static final String ADMIN_USER_USERNAME_EXIST = BASE_AUTH_URL + "/admin-user/username-exist";
    /**
     * 添加下级管理员
     **/
    public static final String ADMIN_USER_ADD = BASE_AUTH_URL + "/admin-user/add";
    /**
     * 编辑下级管理员
     **/
    public static final String ADMIN_USER_EDIT = BASE_AUTH_URL + "/admin-user/edit";
    /**
     * 删除下级管理员
     **/
    public static final String ADMIN_USER_DELETE = BASE_AUTH_URL + "/admin-user/delete";
    /**
     * 启用/禁用下级管理员
     **/
    public static final String ADMIN_USER_ENABLE_DISABLE = BASE_AUTH_URL + "/admin-user/enable-disable";
    /**
     * 解绑下级管理员谷歌
     **/
    public static final String ADMIN_USER_UNBIND_GOOGLE = BASE_AUTH_URL + "/admin-user/unbind-google";
    /**
     * 重置密码错误次数
     **/
    public static final String ADMIN_USER_RESET_LOGIN_PWD_ERROR_NUM = BASE_AUTH_URL + "/admin-user/resetLoginPwdNum";
    /**
     * 当前登录用户的下级角色列表
     **/
    public static final String ADMIN_ROLE_CURRENT_LOWER_LIST = BASE_AUTH_URL + "/admin-role/current-lower-list";
    /**
     * 当前登录用户的角色列表和下级角色列表
     **/
    public static final String ADMIN_ROLE_CURRENT_SELF_LOWER_LIST = BASE_AUTH_URL + "/admin-role/current-self-lower-list";
    /**
     * 检查角色名是否存在
     **/
    public static final String ADMIN_ROLE_USERNAME_EXIST = BASE_AUTH_URL + "/admin-role/name-exist";
    /**
     * 添加下级角色
     **/
    public static final String ADMIN_ROLE_ADD = BASE_AUTH_URL + "/admin-role/add";
    /**
     * 编辑下级角色
     **/
    public static final String ADMIN_ROLE_EDIT = BASE_AUTH_URL + "/admin-role/edit";
    /**
     * 启用/禁用下级角色
     **/
    public static final String ADMIN_ROLE_ENABLE_DISABLE = BASE_AUTH_URL + "/admin-role/enable-disable";
    /**
     * 角色菜单权限列表，必须是自己的角色或者下级角色
     **/
    public static final String ADMIN_ROLE_MENU_LIST = BASE_AUTH_URL + "/admin-role/menu-list";
    /**
     * 编辑下级角色权限
     **/
    public static final String ADMIN_ROLE_EDIT_MENU = BASE_AUTH_URL + "/admin-role/edit-menu";
    /**
     * 管理员登录日志
     **/
    public static final String ADMIN_LOGIN_LOG_PAGE = BASE_AUTH_URL + "/admin-login-log/page";
    /**
     * 菜单列表
     **/
    public static final String ADMIN_MENU_LIST = BASE_AUTH_URL + "/admin-menu/list";
    /**
     * 编辑菜单
     **/
    public static final String ADMIN_MENU_EDIT = BASE_AUTH_URL + "/admin-menu/edit";
    /**
     * 启用/禁用菜单
     **/
    public static final String ADMIN_MENU_ENABLE_DISABLE = BASE_AUTH_URL + "/admin-menu/enable-disable";
    /**
     * 修改排序
     **/
    public static final String ADMIN_MENU_EDIT_SORT = BASE_AUTH_URL + "/admin-menu/edit-sort";
    /**
     * 管理员操作日志
     **/
    public static final String ADMIN_OPERATION_LOG_PAGE = BASE_AUTH_URL + "/admin-operation-log/page";
    /**
     * 后台IP黑白名单分页
     */
    public static final String ADMIN_IP_BLACK_WHITE_PAGE = BASE_AUTH_URL + "/admin-ip-black-white/page";
    /**
     * 后台IP黑白名单新增
     */
    public static final String ADMIN_IP_BLACK_WHITE_ADD = BASE_AUTH_URL + "/admin-ip-black-white/add";
    /**
     * 后台IP黑白名单编辑
     */
    public static final String ADMIN_IP_BLACK_WHITE_EDIT = BASE_AUTH_URL + "/admin-ip-black-white/edit";
    /**
     * 后台IP黑白名单删除
     */
    public static final String ADMIN_IP_BLACK_WHITE_DELETE = BASE_AUTH_URL + "/admin-ip-black-white/delete";
    /**
     * 启用的语言列表，无需登录
     **/
    public static final String I18N_LANGUAGE_SIMPLE_LIST = BASE_NO_AUTH_URL + "/i18n-language/simple-list";
    /**
     * 系统语言列表
     **/
    public static final String I18N_LANGUAGE_LIST = BASE_AUTH_URL + "/i18n-language/list";
    /**
     * 启用/禁用语言
     **/
    public static final String I18N_LANGUAGE_ENABLE_DISABLE = BASE_AUTH_URL + "/i18n-language/enable-disable";
    /**
     * 翻译列表
     **/
    public static final String I18N_TRANSLATE_LIST = BASE_AUTH_URL + "/i18n-translate/list";
    /**
     * 编辑翻译
     **/
    public static final String I18N_TRANSLATE_EDIT = BASE_AUTH_URL + "/i18n-translate/edit";
    /**
     * 新增翻译
     **/
    public static final String I18N_TRANSLATE_ADD = BASE_AUTH_URL + "/i18n-translate/add";
    /**
     * 删除翻译
     **/
    public static final String I18N_TRANSLATE_DELETE = BASE_AUTH_URL + "/i18n-translate/delete";
    /**
     * 获取系统配置列表-初始配置
     **/
    public static final String SYS_CONFIG_SIMPLE_LIST = BASE_NO_AUTH_URL + "/sys-config/simple-list";
    /**
     * 获取数据字段配置
     **/
    public static final String SYS_DICT_LIST = BASE_AUTH_URL + "/sys-dict/list";
    /**
     * 系统配置列表
     **/
    public static final String SYS_CONFIG_LIST = BASE_AUTH_URL + "/sys-config/list";
    /**
     * 编辑系统配置
     **/
    public static final String SYS_CONFIG_EDIT = BASE_AUTH_URL + "/sys-config/edit";
    /**
     * 高级配置列表
     **/
    public static final String SYS_CONFIG_ADVANCE_LIST = BASE_AUTH_URL + "/sys-config/advance-list";
    /**
     * 编辑高级配置
     **/
    public static final String SYS_CONFIG_ADVANCE_EDIT = BASE_AUTH_URL + "/sys-config/advance-edit";
    /**
     * 数据备份配置分页
     **/
    public static final String SYS_BACKUP_CONFIG_PAGE = BASE_AUTH_URL + "/sys-backup-config/page";
    /**
     * 编辑数据备份配置
     **/
    public static final String SYS_BACKUP_CONFIG_EDIT = BASE_AUTH_URL + "/sys-backup-config/edit";
    /**
     * 启用/禁用数据备份配置
     **/
    public static final String SYS_BACKUP_CONFIG_ENABLE_DISABLE = BASE_AUTH_URL + "/sys-backup-config/enable-disable";
    /**
     * 删除数据备份配置
     **/
    public static final String SYS_BACKUP_CONFIG_DELETE = BASE_AUTH_URL + "/sys-backup-config/delete";
    /**
     * 系统银行列表
     **/
    public static final String BANK_SIMPLE_LIST = BASE_AUTH_URL + "/bank/simple-list";
    /**
     * 系统银行分页
     **/
    public static final String BANK_PAGE = BASE_AUTH_URL + "/bank/page";
    /**
     * 新增银行
     **/
    public static final String BANK_ADD = BASE_AUTH_URL + "/bank/add";
    /**
     * 编辑银行
     **/
    public static final String BANK_EDIT = BASE_AUTH_URL + "/bank/edit";
    /**
     * 检测用户名是否存在
     **/
    public static final String USER_USERNAME_EXIST = BASE_AUTH_URL + "/user/username-exist";
    /**
     * 用户简单列表
     */
    public static final String PORTAL_USER_SIMPLE_LIST = BASE_AUTH_URL + "/portal-user/simple-list";
    /**
     * 用户分页
     */
    public static final String PORTAL_USER_PAGE = BASE_AUTH_URL + "/portal-user/page";
    /**
     * 用户详情
     */
    public static final String PORTAL_USER_DETAIL = BASE_AUTH_URL + "/portal-user/detail";
    /**
     * 新增用户
     */
    public static final String PORTAL_USER_ADD = BASE_AUTH_URL + "/portal-user/add";
    // /**
    //  * 管理员踢出用户
    //  */
    // public static final String PORTAL_USER_KICK_OUT = BASE_AUTH_URL + "/portal-user/kick-out";
    /**
     * 编辑用户
     */
    public static final String PORTAL_USER_EDIT = BASE_AUTH_URL + "/portal-user/edit";
    /**
     * 启/禁状态
     */
    public static final String PORTAL_USER_ENABLE_DISABLE = BASE_AUTH_URL + "/portal-user/enable-disable";
    /**
     * 启/禁登录
     */
    public static final String PORTAL_USER_ENABLE_DISABLE_LOGIN = BASE_AUTH_URL + "/portal-user/enable-disable-login";
    /**
     * 启/禁加好友
     */
    public static final String PORTAL_USER_ENABLE_DISABLE_ADD_FRIEND = BASE_AUTH_URL + "/portal-user/enable-disable-add-friend";
    /**
     * 启/禁充值
     */
    public static final String PORTAL_USER_ENABLE_DISABLE_RECHARGE = BASE_AUTH_URL + "/portal-user/enable-disable-recharge";
    /**
     * 启/禁提现
     */
    public static final String PORTAL_USER_ENABLE_DISABLE_WITHDRAW = BASE_AUTH_URL + "/portal-user/enable-disable-withdraw";
    /**
     * 管理员增减用户余额
     */
    public static final String PORTAL_USER_ADD_BALANCE = BASE_AUTH_URL + "/portal-user/add-balance";
    /**
     * 用户名是否存在
     */
    public static final String PORTAL_USER_USERNAME_EXIST = BASE_AUTH_URL + "/portal-user/username-exist";
    /**
     * 重置资金密码
     */
    public static final String PORTAL_USER_RESET_FUND_PWD = BASE_AUTH_URL + "/portal-user/reset-fund-pwd";
    /**
     * 编辑提现姓名
     */
    public static final String PORTAL_USER_EDIT_WITHDRAW_NAME = BASE_AUTH_URL + "/portal-user/edit-withdraw-name";
    /**
     * 编辑我的邀请码
     */
    public static final String PORTAL_USER_EDIT_MY_INVITE_CODE = BASE_AUTH_URL + "/portal-user/edit-my-invite-code";

    /**
     * 编辑内部用户
     */
    public static final String PORTAL_USER_EDIT_INTERNAL_USER = BASE_AUTH_URL + "/portal-user/edit-internal-user";
    /**
     * 用户操作日志
     */
    public static final String PORTAL_OPERATION_LOG_PAGE = BASE_AUTH_URL + "/portal-operation-log/page";
    /**
     * 用户登录日志
     */
    public static final String PORTAL_LOGIN_LOG_PAGE = BASE_AUTH_URL + "/portal-login-log/page";
    /**
     * 用户余额快照
     */
    public static final String USER_BALANCE_SNAPSHOT_PAGE = BASE_AUTH_URL + "/user-balance-snapshot/page";
    /**
     * 用户账变
     */
    public static final String USER_BILL_PAGE = BASE_AUTH_URL + "/user-bill/page";
    /**
     * 用户账变导出
     */
    public static final String USER_BILL_EXPORT = BASE_AUTH_URL + "/user-bill/export";
    /**
     * 用户组简单列表
     */
    public static final String USER_GROUP_SIMPLE_LIST = BASE_AUTH_URL + "/user-group/simple-list";
    /**
     * 用户组分页
     */
    public static final String USER_GROUP_PAGE = BASE_AUTH_URL + "/user-group/page";
    /**
     * 用户组编辑
     */
    public static final String USER_GROUP_EDIT = BASE_AUTH_URL + "/user-group/edit";
    /**
     * 用户组新增
     */
    public static final String USER_GROUP_ADD = BASE_AUTH_URL + "/user-group/add";
    /**
     * 用户组用户分页
     */
    public static final String USER_GROUP_USER_LIST = BASE_AUTH_URL + "/user-group/user-list";
    /**
     * 编辑用户组用户
     */
    public static final String USER_GROUP_EDIT_USER = BASE_AUTH_URL + "/user-group/edit-user";
    /**
     * 用户组银行卡充值配置列表
     */
    public static final String USER_GROUP_BANK_CARD_RECHARGE_CONFIG_LIST = BASE_AUTH_URL + "/user-group/bank-card-recharge-config-list";
    /**
     * 编辑用户组银行卡充值配置
     */
    public static final String USER_GROUP_EDIT_BANK_CARD_RECHARGE_CONFIG = BASE_AUTH_URL + "/user-group/edit-bank-card-recharge-config";
    /**
     * 用户组三方充值配置列表
     */
    public static final String USER_GROUP_API_RECHARGE_CONFIG_LIST = BASE_AUTH_URL + "/user-group/api-recharge-config-list";
    /**
     * 编辑用户组三方充值配置
     */
    public static final String USER_GROUP_EDIT_API_RECHARGE_CONFIG = BASE_AUTH_URL + "/user-group/edit-api-recharge-config";
    /**
     * 用户组删除
     */
    public static final String USER_GROUP_DELETE = BASE_AUTH_URL + "/user-group/delete";
    /**
     * 用户银行卡
     */
    public static final String USER_BANK_CARD_PAGE = BASE_AUTH_URL + "/user-bank-card/page";
    /**
     * 用户银行卡添加
     */
    public static final String USER_BANK_CARD_ADD = BASE_AUTH_URL + "/user-bank-card/add";
    /**
     * 用户银行卡删除
     */
    public static final String USER_BANK_CARD_DELETE = BASE_AUTH_URL + "/user-bank-card/delete";
    /**
     * 启用禁用用户银行卡
     */
    public static final String USER_BANK_CARD_ENABLE_DISABLE = BASE_AUTH_URL + "/user-bank-card/enable-disable";
    /**
     * 用户卡黑名单分页
     */
    public static final String USER_BANK_CARD_BLACK_PAGE = BASE_AUTH_URL + "/user-bank-card-black/page";
    /**
     * 用户卡黑名新增
     */
    public static final String USER_BANK_CARD_BLACK_ADD = BASE_AUTH_URL + "/user-bank-card-black/add";
    /**
     * 用户卡黑名单编辑
     */
    public static final String USER_BANK_CARD_BLACK_EDIT = BASE_AUTH_URL + "/user-bank-card-black/edit";
    /**
     * 用户卡黑名单删除
     */
    public static final String USER_BANK_CARD_BLACK_DELETE = BASE_AUTH_URL + "/user-bank-card-black/delete";
    /**
     * 用户IP黑白名单分页
     */
    public static final String PORTAL_IP_BLACK_WHITE_PAGE = BASE_AUTH_URL + "/portal-ip-black-white/page";
    /**
     * 用户IP黑白名添加
     */
    public static final String PORTAL_IP_BLACK_WHITE_ADD = BASE_AUTH_URL + "/portal-ip-black-white/add";
    /**
     * 用户IP黑白名单编辑
     */
    public static final String PORTAL_IP_BLACK_WHITE_EDIT = BASE_AUTH_URL + "/portal-ip-black-white/edit";
    /**
     * 用户IP黑白名单删除
     */
    public static final String PORTAL_IP_BLACK_WHITE_DELETE = BASE_AUTH_URL + "/portal-ip-black-white/delete";
    /**
     * 用户区域黑白名单分页
     */
    public static final String PORTAL_AREA_BLACK_WHITE_PAGE = BASE_AUTH_URL + "/portal-area-black-white/page";
    /**
     * 用户区域黑白名添加
     */
    public static final String PORTAL_AREA_BLACK_WHITE_ADD = BASE_AUTH_URL + "/portal-area-black-white/add";
    /**
     * 用户区域黑白名单编辑
     */
    public static final String PORTAL_AREA_BLACK_WHITE_EDIT = BASE_AUTH_URL + "/portal-area-black-white/edit";
    /**
     * 用户区域黑白名单删除
     */
    public static final String PORTAL_AREA_BLACK_WHITE_DELETE = BASE_AUTH_URL + "/portal-area-black-white/delete";
    /**
     * 系统公告 添加
     */
    public static final String SYS_NOTICE_ADD = BASE_AUTH_URL + "/sys-notice/add";
    /**
     * 系统公告 修改
     */
    public static final String SYS_NOTICE_EDIT = BASE_AUTH_URL + "/sys-notice/edit";
    /**
     * 系统公告 删除
     */
    public static final String SYS_NOTICE_DELETE = BASE_AUTH_URL + "/sys-notice/delete";
    /**
     * 系统公告 分页查询
     */
    public static final String SYS_NOTICE_PAGE = BASE_AUTH_URL + "/sys-notice/page";
    /**
     * 上传图片
     */
    public static final String FILE_UPLOAD_IMAGE = BASE_AUTH_URL + "/file/upload/image";
    /**
     * 上传头像
     */
    public static final String FILE_UPLOAD_AVATAR = BASE_AUTH_URL + "/file/upload/avatar";
    /**
     * 上传视频
     */
    public static final String FILE_UPLOAD_VIDEO = BASE_AUTH_URL + "/file/upload/video";
    /**
     * 上传语音
     */
    public static final String FILE_UPLOAD_SOUND = BASE_AUTH_URL + "/file/upload/sound";
    /**
     * 上传文件
     */
    public static final String FILE_UPLOAD_FILE = BASE_AUTH_URL + "/file/upload/file";
    /**
     * 银行卡充值配置简单列表
     */
    public static final String BANK_CARD_RECHARGE_CONFIG_SIMPLE_LIST = BASE_AUTH_URL + "/bank-card-recharge-config/simple-list";
    /**
     * 银行卡充值配置 分页
     */
    public static final String BANK_CARD_RECHARGE_CONFIG_PAGE = BASE_AUTH_URL + "/bank-card-recharge-config/page";
    /**
     * 银行卡充值配置 新增
     */
    public static final String BANK_CARD_RECHARGE_CONFIG_ADD = BASE_AUTH_URL + "/bank-card-recharge-config/add";
    /**
     * 银行卡充值配置 编辑
     */
    public static final String BANK_CARD_RECHARGE_CONFIG_EDIT = BASE_AUTH_URL + "/bank-card-recharge-config/edit";
    /**
     * 银行卡充值配置 删除
     */
    public static final String BANK_CARD_RECHARGE_CONFIG_DELETE = BASE_AUTH_URL + "/bank-card-recharge-config/delete";
    /**
     * 银行卡充值配置 启/禁
     */
    public static final String BANK_CARD_RECHARGE_CONFIG_ENABLE_DISABLE = BASE_AUTH_URL + "/bank-card-recharge-config/enable-disable";
    /**
     * 三方充值配置简单列表
     */
    public static final String API_RECHARGE_CONFIG_SIMPLE_LIST = BASE_AUTH_URL + "/api-recharge-config/simple-list";
    /**
     * 三方充值配置 分页
     */
    public static final String API_RECHARGE_CONFIG_PAGE = BASE_AUTH_URL + "/api-recharge-config/page";
    /**
     * 三方充值配置 新增
     */
    public static final String API_RECHARGE_CONFIG_ADD = BASE_AUTH_URL + "/api-recharge-config/add";
    /**
     * 三方充值配置 编辑
     */
    public static final String API_RECHARGE_CONFIG_EDIT = BASE_AUTH_URL + "/api-recharge-config/edit";
    /**
     * 三方充值配置 删除
     */
    public static final String API_RECHARGE_CONFIG_DELETE = BASE_AUTH_URL + "/api-recharge-config/delete";
    /**
     * 三方充值配置 启/禁
     */
    public static final String API_RECHARGE_CONFIG_ENABLE_DISABLE = BASE_AUTH_URL + "/api-recharge-config/enable-disable";
    /**
     * 银行卡提现配置简单列表
     */
    public static final String BANK_CARD_WITHDRAW_CONFIG_SIMPLE_LIST = BASE_AUTH_URL + "/bank-card-withdraw-config/simple-list";
    /**
     * 银行卡提现配置 分页
     */
    public static final String BANK_CARD_WITHDRAW_CONFIG_PAGE = BASE_AUTH_URL + "/bank-card-withdraw-config/page";
    /**
     * 银行卡提现配置 新增
     */
    public static final String BANK_CARD_WITHDRAW_CONFIG_ADD = BASE_AUTH_URL + "/bank-card-withdraw-config/add";
    /**
     * 银行卡提现配置 编辑
     */
    public static final String BANK_CARD_WITHDRAW_CONFIG_EDIT = BASE_AUTH_URL + "/bank-card-withdraw-config/edit";
    /**
     * 银行卡提现配置 删除
     */
    public static final String BANK_CARD_WITHDRAW_CONFIG_DELETE = BASE_AUTH_URL + "/bank-card-withdraw-config/delete";
    /**
     * 银行卡提现配置 启/禁
     */
    public static final String BANK_CARD_WITHDRAW_CONFIG_ENABLE_DISABLE = BASE_AUTH_URL + "/bank-card-withdraw-config/enable-disable";
    /**
     * API代付配置简单列表
     */
    public static final String API_WITHDRAW_CONFIG_SIMPLE_LIST = BASE_AUTH_URL + "/api-withdraw-config/simple-list";
    /**
     * API代付配置 分页
     */
    public static final String API_WITHDRAW_CONFIG_PAGE = BASE_AUTH_URL + "/api-withdraw-config/page";
    /**
     * API代付配置 新增
     */
    public static final String API_WITHDRAW_CONFIG_ADD = BASE_AUTH_URL + "/api-withdraw-config/add";
    /**
     * API代付配置 编辑
     */
    public static final String API_WITHDRAW_CONFIG_EDIT = BASE_AUTH_URL + "/api-withdraw-config/edit";
    /**
     * API代付配置 删除
     */
    public static final String API_WITHDRAW_CONFIG_DELETE = BASE_AUTH_URL + "/api-withdraw-config/delete";
    /**
     * API代付配置 启/禁
     */
    public static final String API_WITHDRAW_CONFIG_ENABLE_DISABLE = BASE_AUTH_URL + "/api-withdraw-config/enable-disable";
    /**
     * 充值订单日志分页
     */
    public static final String RECHARGE_ORDER_LOG_PAGE = BASE_AUTH_URL + "/recharge-order/log-page";
    /**
     * 充值订单分页
     */
    public static final String RECHARGE_ORDER_PAGE = BASE_AUTH_URL + "/recharge-order/page";
    /**
     * 充值订单补单
     */
    public static final String RECHARGE_ORDER_PATCH = BASE_AUTH_URL + "/recharge-order/patch";
    /**
     * 充值订单人工充值
     */
    public static final String RECHARGE_ORDER_ADMIN_ADD = BASE_AUTH_URL + "/recharge-order/admin-add";
    /**
     * 充值订单人工充值获取用户信息
     */
    public static final String RECHARGE_ORDER_ADMIN_ADD_GET_USER_INFO = BASE_AUTH_URL + "/recharge-order/admin-add-get-user-info";
    /**
     * 充值订单导出
     */
    public static final String RECHARGE_ORDER_EXPORT = BASE_AUTH_URL + "/recharge-order/export";
    /**
     * 提现订单日志分页
     */
    public static final String WITHDRAW_ORDER_LOG_PAGE = BASE_AUTH_URL + "/withdraw-order/log-page";
    /**
     * 提现订单分页
     */
    public static final String WITHDRAW_ORDER_PAGE = BASE_AUTH_URL + "/withdraw-order/page";
    /**
     * 提现订单审核锁定/解锁
     */
    public static final String WITHDRAW_ORDER_APPROVE_LOCK_UNLOCK = BASE_AUTH_URL + "/withdraw-order/approve-lock-unlock";
    /**
     * 提现订单审核
     */
    public static final String WITHDRAW_ORDER_APPROVE = BASE_AUTH_URL + "/withdraw-order/approve";
    /**
     * 提现订单打款锁定/解锁
     */
    public static final String WITHDRAW_ORDER_PAY_LOCK_UNLOCK = BASE_AUTH_URL + "/withdraw-order/pay-lock-unlock";
    /**
     * 提现订单打款
     */
    public static final String WITHDRAW_ORDER_PAY = BASE_AUTH_URL + "/withdraw-order/pay";
    /**
     * 提现订单到账
     */
    public static final String WITHDRAW_ORDER_PAY_FINISH = BASE_AUTH_URL + "/withdraw-order/pay-finish";
    /**
     * 人工提现
     */
    public static final String WITHDRAW_ORDER_ADMIN_ADD = BASE_AUTH_URL + "/withdraw-order/admin-add";
    /**
     * 人工提现获取用户信息
     */
    public static final String WITHDRAW_ORDER_ADMIN_ADD_GET_USER_INFO = BASE_AUTH_URL + "/withdraw-order/admin-add-get-user-info";
    /**
     * 提现订单导出
     */
    public static final String WITHDRAW_ORDER_EXPORT = BASE_AUTH_URL + "/withdraw-order/export";
    /**
     * 群组列表
     */
    public static final String TIM_GROUP_SIMPLE_LIST = BASE_AUTH_URL + "/tim/group/simple-list";
    /**
     * 群组信息分页
     */
    public static final String TIM_GROUP_PAGE = BASE_AUTH_URL + "/tim/group/page";
    /**
     * 群组成员信息分页
     */
    public static final String TIM_GROUP_MEMBER_PAGE = BASE_AUTH_URL + "/tim/group/member/page";
    /**
     * 群组全员禁言/解除禁言
     */
    public static final String TIM_GROUP_SHUT_UP = BASE_AUTH_URL + "/tim/group/shut-up";
    /**
     * 解散群组
     */
    public static final String TIM_GROUP_DESTROY = BASE_AUTH_URL + "/tim/group/destroy";
    /**
     * 编辑群组信息
     */
    public static final String TIM_GROUP_EDIT = BASE_AUTH_URL + "/tim/group/edit";
    /**
     * 群组成员禁言
     */
    public static final String TIM_GROUP_MEMBER_SHUT_UP = BASE_AUTH_URL + "/tim/group/member/shut-up";
    /**
     * 删除群组成员
     */
    public static final String TIM_GROUP_MEMBER_DELETE = BASE_AUTH_URL + "/tim/group/member/delete";
    /**
     * 添加群组成员
     */
    public static final String TIM_GROUP_MEMBER_ADD = BASE_AUTH_URL + "/tim/group/member/add";
    /**
     * 设置群组管理员
     */
    public static final String TIM_GROUP_SET_MANAGER = BASE_AUTH_URL + "/tim/group/manager/set";
    /**
     * 群组消息分页
     */
    public static final String TIM_GROUP_MESSAGE_PAGE = BASE_AUTH_URL + "/tim/group/message/page";
    /**
     * 撤回群组消息
     */
    public static final String TIM_GROUP_MESSAGE_WITHDRAW = BASE_AUTH_URL + "/tim/group/message/withdraw";
    /**
     * 发送群组消息
     */
    public static final String TIM_GROUP_MESSAGE_SEND = BASE_AUTH_URL + "/tim/group/message/send";
    /**
     * 好友信息分页
     */
    public static final String TIM_FRIEND_PAGE = BASE_AUTH_URL + "/tim/friend/page";
    /**
     * 添加好友
     */
    public static final String TIM_FRIEND_ADD = BASE_AUTH_URL + "/tim/friend/add";
    /**
     * 删除好友
     */
    public static final String TIM_FRIEND_DELETE = BASE_AUTH_URL + "/tim/friend/delete";
    /**
     * 删除某个用户的所有好友
     */
    public static final String TIM_FRIEND_DELETE_ALL = BASE_AUTH_URL + "/tim/friend/delete/all";
    /**
     * 黑名单信息分页
     */
    public static final String TIM_BLACKLIST_PAGE = BASE_AUTH_URL + "/tim/blacklist/page";
    /**
     * 添加黑名单
     */
    public static final String TIM_BLACKLIST_ADD = BASE_AUTH_URL + "/tim/blacklist/add";
    /**
     * 删除黑名单
     */
    public static final String TIM_BLACKLIST_DELETE = BASE_AUTH_URL + "/tim/blacklist/delete";
    /**
     * 设置用户全局禁言
     */
    public static final String TIM_GLOBAL_SHUT_UP_SET = BASE_AUTH_URL + "/tim/global/shut-up/set";
    /**
     * 获取用户全局禁言数据
     */
    public static final String TIM_GLOBAL_SHUT_UP_GET = BASE_AUTH_URL + "/tim/global/shut-up/get";
    /**
     * 群文件-发布
     */
    public static final String TIM_GROUP_FILE_ADD = BASE_AUTH_URL + "/tim-group-file/add";
    /**
     * 群文件-删除
     */
    public static final String TIM_GROUP_FILE_DELETE = BASE_AUTH_URL + "/tim-group-file/delete";
    /**
     * 群文件-分页
     */
    public static final String TIM_GROUP_FILE_LIST = BASE_AUTH_URL + "/tim-group-file/list";
    /**
     * 单聊消息分页
     */
    public static final String TIM_C2C_MESSAGE_PAGE = BASE_AUTH_URL + "/tim/c2c/message/page";
    /**
     * 撤回单聊消息
     */
    public static final String TIM_C2C_MESSAGE_WITHDRAW = BASE_AUTH_URL + "/tim/c2c/message/withdraw";
    /**
     * 发送单聊消息
     */
    public static final String TIM_C2C_MESSAGE_SEND = BASE_AUTH_URL + "/tim/c2c/message/send";
    /**
     * 在线用户分页
     */
    public static final String TIM_USER_ONLINE_PAGE = BASE_AUTH_URL + "/tim/user/online/page";
    /**
     * 踢下线
     */
    public static final String TIM_USER_KICK_OUT = BASE_AUTH_URL + "/tim/user/kick-out";
    /**
     * im数据统计
     */
    public static final String TIM_STATISTIC = BASE_AUTH_URL + "/tim/statistic";
    /**
     * 获取所有表情包数据
     */
    public static final String TIM_FACE_LIST = BASE_AUTH_URL + "/tim-face/list";
    /**
     * 新增表情包专辑数据
     */
    public static final String TIM_FACE_ADD = BASE_AUTH_URL + "/tim-face/add";
    /**
     * 删除表情包专辑数据
     */
    public static final String TIM_FACE_DELETE = BASE_AUTH_URL + "/tim-face/delete";
    /**
     * 删除表情数据
     */
    public static final String TIM_FACE_ITEM_DELETE = BASE_AUTH_URL + "/tim-face-item/delete";
    /**
     * 新增表情数据
     */
    public static final String TIM_FACE_ITEM_ADD = BASE_AUTH_URL + "/tim-face-item/add";
    /**
     * 个人红包列表
     */
    public static final String PERSONAL_RED_ENVELOPE_PAGE = BASE_AUTH_URL + "/personal-red-envelope/page";
    /**
     * 群红包列表
     */
    public static final String GROUP_RED_ENVELOPE_PAGE = BASE_AUTH_URL + "/group-red-envelope/page";
    /**
     * 群红包领取记录
     */
    public static final String GROUP_RED_ENVELOPE_RECEIVE_PAGE = BASE_AUTH_URL + "/group-red-envelope/receive-page";
    /**
     * 朋友圈分页
     */
    public static final String USER_MOMENTS_PAGE = BASE_AUTH_URL + "/user-moments/page";
    /**
     * 删除朋友圈
     */
    public static final String USER_MOMENTS_DELETE = BASE_AUTH_URL + "/user-moments/delete";
    /**
     * 删除评论
     */
    public static final String USER_MOMENTS_REVIEW_DELETE = BASE_AUTH_URL + "/user-moments-review/delete";
    /**
     * 删除点赞
     */
    public static final String USER_MOMENTS_LIKE_DELETE = BASE_AUTH_URL + "/user-moments-like/delete";
    /**
     * 切换腾讯IM账号——获取APP ID信息
     */
    public static final String TIM_SWITCH_APP_ID_GET = BASE_AUTH_URL + "/tim/switch/app/app-id/get";
    /**
     * 切换腾讯IM账号——失效所有用户的登陆状态
     */
    public static final String TIM_SWITCH_KICK_ALL = BASE_AUTH_URL + "/tim/switch/app/kick-all";
    /**
     * 切换腾讯IM账号——导入用户账号数据
     */
    public static final String TIM_SWITCH_IMPORT_ACCOUNT = BASE_AUTH_URL + "/tim/switch/app/import-account";
    /**
     * 切换腾讯IM账号——设置账号信息
     */
    public static final String TIM_SET_ACCOUNT_PORTRAIT = BASE_AUTH_URL + "/tim/switch/app/set-account-portrait";
    /**
     * 切换腾讯IM账号——导入用户好友数据
     */
    public static final String TIM_IMPORT_FRIENDSHIP = BASE_AUTH_URL + "/tim/switch/app/import-friendship";
    /**
     * 切换腾讯IM账号——导入单聊消息
     */
    public static final String TIM_IMPORT_C2C_MESSAGE = BASE_AUTH_URL + "/tim/switch/app/import-c2c-message";
    /**
     * 切换腾讯IM账号——导入群组
     */
    public static final String TIM_IMPORT_GROUP = BASE_AUTH_URL + "/tim/switch/app/import-group";
    /**
     * 切换腾讯IM账号——导入群组成员
     */
    public static final String TIM_IMPORT_GROUP_MEMBER = BASE_AUTH_URL + "/tim/switch/app/import-group-member";
    /**
     * 切换腾讯IM账号——导入群组消息
     */
    public static final String TIM_IMPORT_GROUP_MESSAGE = BASE_AUTH_URL + "/tim/switch/app/import-group-message";
    /**
     * 切换腾讯IM账号——获取导入进度
     */
    public static final String TIM_SWITCH_PROCESS = BASE_AUTH_URL + "/tim/switch/app/process";
    /**
     * app版本管理列表
     **/
    public static final String APP_VERSION_LIST = BASE_AUTH_URL + "/app-version/list";
    /**
     * 新增app版本管理
     **/
    public static final String APP_VERSION_ADD = BASE_AUTH_URL + "/app-version/add";
    /**
     * 删除app版本管理
     **/
    public static final String APP_VERSION_DELETE = BASE_AUTH_URL + "/app-version/delete";
    /**
     * 前台导航分页
     **/
    public static final String PORTAL_NAVIGATOR_PAGE = BASE_AUTH_URL + "/portal-navigator/page";
    /**
     * 新增前台导航
     **/
    public static final String PORTAL_NAVIGATOR_ADD = BASE_AUTH_URL + "/portal-navigator/add";
    /**
     * 编辑前台导航
     **/
    public static final String PORTAL_NAVIGATOR_EDIT = BASE_AUTH_URL + "/portal-navigator/edit";
    /**
     * 启/禁前台导航
     **/
    public static final String PORTAL_NAVIGATOR_ENABLE_DISABLE = BASE_AUTH_URL + "/portal-navigator/enable-disable";
    /**
     * 删除前台导航
     **/
    public static final String PORTAL_NAVIGATOR_DELETE = BASE_AUTH_URL + "/portal-navigator/delete";
    /**
     * 前台导航统计
     **/
    public static final String PORTAL_NAVIGATOR_STATISTIC_LIST = BASE_AUTH_URL + "/portal-navigator/statistic/list";
    /**
     * 前台导航统计详情
     **/
    public static final String PORTAL_NAVIGATOR_STATISTIC_DETAIL = BASE_AUTH_URL + "/portal-navigator/statistic/detail";
    /**
     * 系统默认头像列表
     **/
    public static final String SYS_DEFAULT_AVATAR_SIMPLE_LIST = BASE_AUTH_URL + "/sys-default-avatar/simple-list";
    /**
     * 新增系统默认头像
     **/
    public static final String SYS_DEFAULT_AVATAR_ADD = BASE_AUTH_URL + "/sys-default-avatar/add";
    /**
     * 敏感词分页
     */
    public static final String SENSITIVE_WORD_PAGE = BASE_AUTH_URL + "/sensitive-word/page";
    /**
     * 新增敏感词
     */
    public static final String SENSITIVE_WORD_ADD = BASE_AUTH_URL + "/sensitive-word/add";
    /**
     * 删除敏感词
     */
    public static final String SENSITIVE_WORD_DELETE = BASE_AUTH_URL + "/sensitive-word/delete";

    /**
     * app用户操作日志
     **/
    public static final String PORTAL_USER_APP_OPERATION_LOG_PAGE = BASE_AUTH_URL + "/portal-user-app-operation-log/page";

    /**
     * 自动回复配置分页
     */
    public static final String APP_AUTO_REPLY_CONFIG_PAGE = BASE_AUTH_URL + "/app-auto-reply-config/page";
    /**
     * 新增自动回复配置
     */
    public static final String APP_AUTO_REPLY_CONFIG_ADD = BASE_AUTH_URL + "/app-auto-reply-config/add";
    /**
     * 删除自动回复配置
     */
    public static final String APP_AUTO_REPLY_CONFIG_DELETE = BASE_AUTH_URL + "/app-auto-reply-config/delete";

    /**
     * 设备信息分页
     */
    public static final String DEVICE_INFO_PAGE = BASE_AUTH_URL + "/device-info/page";

    public ApiUrl() {
    }
}
