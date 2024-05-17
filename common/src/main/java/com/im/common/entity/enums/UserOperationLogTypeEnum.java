package com.im.common.entity.enums;

/**
 * 操作日志类型
 *
 * @author Barry
 * @date 2020-05-23
 */
public enum UserOperationLogTypeEnum implements BaseEnum {
    /**
     * 绑定谷歌
     */
    GOOGLE_BIND("GOOGLE_BIND", "绑定谷歌"),
    UNBIND_GOOGLE("UNBIND_GOOGLE", "解绑谷歌"),

    LOGIN_PWD_EDIT("LOGIN_PWD_EDIT", "修改登录密码"),
    FUND_PWD_BIND("FUND_PWD_BIND", "绑定资金密码"),
    FUND_PWD_EDIT("FUND_PWD_EDIT", "修改资金密码"),
    BIND_WITHDRAW_NAME("BIND_WITHDRAW_NAME", "绑定提现姓名"),

    SYS_CONFIG_EDIT("SYS_CONFIG_EDIT", "编辑系统配置"),
    SYS_CONFIG_ADVANCE_EDIT("SYS_CONFIG_ADVANCE_EDIT", "编辑高级配置"),

    ADMIN_USER_ADD("ADMIN_USER_ADD", "新增管理员"),
    ADMIN_USER_EDIT("ADMIN_USER_EDIT", "编辑管理员"),
    ADMIN_USER_DELETE("ADMIN_USER_DELETE", "删除管理员"),
    ADMIN_USER_ENABLE_DISABLE("ADMIN_USER_ENABLE_DISABLE", "启/禁管理员"),
    ADMIN_USER_RESET_LOGIN_PWD_ERROR_NUM("ADMIN_USER_RESET_LOGIN_PWD_ERROR_NUM", "重置管理员密错"),

    I18N_LANGUAGE_ENABLE_DISABLE("I18N_LANGUAGE_ENABLE_DISABLE", "启禁系统语言"),
    I18N_TRANSLATE_EDIT("I18N_TRANSLATE_EDIT", "编辑系统翻译"),
    I18N_TRANSLATE_ADD("I18N_TRANSLATE_ADD", "新增系统翻译"),
    I18N_TRANSLATE_DELETE("I18N_TRANSLATE_DELETE", "删除系统翻译"),

    SYS_BACKUP_CONFIG_EDIT("SYS_BACKUP_CONFIG_EDIT", "编辑数据备份配置"),
    SYS_BACKUP_CONFIG_ENABLE_DISABLE("SYS_BACKUP_CONFIG_ENABLE_DISABLE", "启/禁数据备份配置"),
    SYS_BACKUP_CONFIG_DELETE("SYS_BACKUP_CONFIG_DELETE", "删除数据备份配置"),

    BANK_ADD("BANK_ADD", "新增银行"),
    BANK_EDIT("BANK_EDIT", "编辑银行"),

    USER_BANK_CARD_BLACK_ADD("USER_BANK_CARD_BLACK_ADD", "新增用户卡黑名单"),
    USER_BANK_CARD_BLACK_EDIT("USER_BANK_CARD_BLACK_EDIT", "编辑用户卡黑名单"),
    USER_BANK_CARD_BLACK_DELETE("USER_BANK_CARD_BLACK_DELETE", "删除用户卡黑名单"),

    // PORTAL_USER_KICK_OUT("PORTAL_USER_KICK_OUT", "踢出用户"),
    PORTAL_USER_ADD("PORTAL_USER_ADD", "新增用户"),
    PORTAL_USER_EDIT("PORTAL_USER_EDIT", "编辑用户"),
    PORTAL_USER_ENABLE_DISABLE_LOGIN("PORTAL_USER_ENABLE_DISABLE_LOGIN", "启/禁登录"),
    PORTAL_USER_ENABLE_DISABLE_ADD_FRIEND("PORTAL_USER_ENABLE_DISABLE_ADD_FRIEND", "启/禁加好友"),
    PORTAL_USER_ENABLE_DISABLE_RECHARGE("PORTAL_USER_ENABLE_DISABLE_RECHARGE", "启/禁充值"),
    PORTAL_USER_ENABLE_DISABLE_WITHDRAW("PORTAL_USER_ENABLE_DISABLE_WITHDRAW", "启/禁提现"),
    PORTAL_USER_ENABLE_DISABLE("PORTAL_USER_ENABLE_DISABLE", "启/禁状态"),
    PORTAL_USER_ADD_BALANCE("PORTAL_USER_ADD_BALANCE", "管理员增减余额"),
    PORTAL_USER_RESET_FUND_PWD("PORTAL_USER_RESET_FUND_PWD", "重置资金密码"),
    PORTAL_USER_EDIT_WITHDRAW_NAME("PORTAL_USER_EDIT_WITHDRAW_NAME", "编辑提现姓名"),
    PORTAL_USER_EDIT_MY_INVITE_CODE("PORTAL_USER_EDIT_MY_INVITE_CODE", "编辑我的邀请码"),
    PORTAL_USER_EDIT_INTERNAL_USER("PORTAL_USER_EDIT_INTERNAL_USER", "编辑内部用户"),

    ADMIN_IP_BLACK_WHITE_ADD("ADMIN_IP_BLACK_WHITE_ADD", "新增后台IP黑白名单"),
    ADMIN_IP_BLACK_WHITE_EDIT("ADMIN_IP_BLACK_WHITE_EDIT", "编辑后台IP黑白名单"),
    ADMIN_IP_BLACK_WHITE_DELETE("ADMIN_IP_BLACK_WHITE_DELETE", "删除后台IP黑白名单"),

    PORTAL_IP_BLACK_WHITE_ADD("PORTAL_IP_BLACK_WHITE_ADD", "新增用户IP黑白名单"),
    PORTAL_IP_BLACK_WHITE_EDIT("PORTAL_IP_BLACK_WHITE_EDIT", "编辑用户IP黑白名单"),
    PORTAL_IP_BLACK_WHITE_DELETE("PORTAL_IP_BLACK_WHITE_DELETE", "删除用户IP黑白名单"),

    PORTAL_AREA_BLACK_WHITE_ADD("PORTAL_AREA_BLACK_WHITE_ADD", "新增用户区域黑白名单"),
    PORTAL_AREA_BLACK_WHITE_EDIT("PORTAL_AREA_BLACK_WHITE_EDIT", "编辑用户区域黑白名单"),
    PORTAL_AREA_BLACK_WHITE_DELETE("PORTAL_AREA_BLACK_WHITE_DELETE", "删除用户区域黑白名单"),

    USER_BILL_EXPORT("USER_BILL_EXPORT", "用户账变导出"),

    USER_BANK_CARD_ADD("USER_BANK_CARD_ADD", "新增用户银行卡"),
    USER_BANK_CARD_ENABLE_DISABLE("USER_BANK_CARD_ENABLE_DISABLE", "启用/禁用用户银行卡"),
    USER_BANK_CARD_DELETE("USER_BANK_CARD_DELETE", "删除用户银行卡"),

    RECHARGE_ORDER_PATCH("RECHARGE_ORDER_PATCH", "充值订单补单"),
    RECHARGE_ORDER_ADMIN_ADD("RECHARGE_ORDER_ADMIN_ADD", "人工充值"),

    WITHDRAW_ORDER_APPROVE_LOCK_UNLOCK("WITHDRAW_ORDER_APPROVE_LOCK_UNLOCK", "提现订单审核锁定/解锁"),
    WITHDRAW_ORDER_APPROVE("WITHDRAW_ORDER_APPROVE", "提现订单审核"),
    WITHDRAW_ORDER_PAY_LOCK_UNLOCK("WITHDRAW_ORDER_PAY_LOCK_UNLOCK", "提现订单打款锁定/解锁"),
    WITHDRAW_ORDER_PAY("WITHDRAW_ORDER_PAY", "提现订单打款"),
    WITHDRAW_ORDER_PAY_FINISH("WITHDRAW_ORDER_PAY_FINISH", "提现订单到账"),
    WITHDRAW_ORDER_ADMIN_ADD("WITHDRAW_ORDER_ADMIN_ADD", "人工提现"),

    BANK_CARD_RECHARGE_CONFIG_ADD("BANK_CARD_RECHARGE_CONFIG_ADD", "新增银行卡充值配置"),
    BANK_CARD_RECHARGE_CONFIG_EDIT("BANK_CARD_RECHARGE_CONFIG_EDIT", "编辑银行卡充值配置"),
    BANK_CARD_RECHARGE_CONFIG_DELETE("BANK_CARD_RECHARGE_CONFIG_DELETE", "删除银行卡充值配置"),
    BANK_CARD_RECHARGE_CONFIG_ENABLE_DISABLE("BANK_CARD_RECHARGE_CONFIG_ENABLE_DISABLE", "启/禁合银行卡充值充值"),

    API_RECHARGE_CONFIG_ADD("API_RECHARGE_CONFIG_ADD", "新增三方充值配置"),
    API_RECHARGE_CONFIG_EDIT("API_RECHARGE_CONFIG_EDIT", "编辑三方充值配置"),
    API_RECHARGE_CONFIG_DELETE("API_RECHARGE_CONFIG_DELETE", "删除三方充值配置"),
    API_RECHARGE_CONFIG_ENABLE_DISABLE("API_RECHARGE_CONFIG_ENABLE_DISABLE", "启/禁三方充值"),

    API_WITHDRAW_CONFIG_ADD("API_WITHDRAW_CONFIG_ADD", "新增API代付配置"),
    API_WITHDRAW_CONFIG_EDIT("API_WITHDRAW_CONFIG_EDIT", "编辑API代付配置"),
    API_WITHDRAW_CONFIG_DELETE("API_WITHDRAW_CONFIG_DELETE", "删除API代付配置"),
    API_WITHDRAW_CONFIG_ENABLE_DISABLE("API_WITHDRAW_CONFIG_ENABLE_DISABLE", "启/禁合API代付配置"),

    SYS_NOTICE_ADD("SYS_NOTICE_ADD", "新增公告"),
    SYS_NOTICE_EDIT("SYS_NOTICE_EDIT", "编辑公告"),
    SYS_NOTICE_DELETE("SYS_NOTICE_DELETE", "删除公告"),

    SYS_CIRCUIT_ADD("SYS_CIRCUIT_ADD", "新增测速线路"),
    SYS_CIRCUIT_EDIT("SYS_CIRCUIT_EDIT", "编辑测速线路"),
    SYS_CIRCUIT_ENABLE_DISABLE("SYS_CIRCUIT_ENABLE_DISABLE", "启/禁测速线路"),
    SYS_CIRCUIT_DELETE("SYS_CIRCUIT_DELETE", "删除测速线路"),
    SYS_CIRCUIT_EXPORT("SYS_CIRCUIT_EXPORT", "线路导出"),

    USER_AUTHENTICATION_EDIT_STATUS("USER_AUTHENTICATION_EDIT_STATUS", "编辑用户实名认证状态"),

    BANK_CARD_WITHDRAW_CONFIG_ADD("BANK_CARD_WITHDRAW_CONFIG_ADD", "新增银行卡提现配置"),
    BANK_CARD_WITHDRAW_CONFIG_EDIT("BANK_CARD_WITHDRAW_CONFIG_EDIT", "编辑银行卡提现配置"),
    BANK_CARD_WITHDRAW_CONFIG_DELETE("BANK_CARD_WITHDRAW_CONFIG_DELETE", "删除银行卡提现配置"),
    BANK_CARD_WITHDRAW_CONFIG_ENABLE_DISABLE("BANK_CARD_WITHDRAW_CONFIG_ENABLE_DISABLE", "启/禁银行卡提现配置"),

    TIM_GROUP_SHUT_UP("TIM_GROUP_SHUT_UP", "群组全员禁言/解除禁言"),
    TIM_GROUP_ENABLE_DISABLE_SHOW_MEMBER("TIM_GROUP_ENABLE_DISABLE_SHOW_MEMBER", "显示群内成员启/禁"),
    TIM_GROUP_ENABLE_DISABLE_ANONYMOUS_CHAT("TIM_GROUP_ENABLE_DISABLE_ANONYMOUS_CHAT", "允许成员私聊启/禁"),
    TIM_GROUP_ENABLE_DISABLE_UPLOAD("TIM_GROUP_ENABLE_DISABLE_UPLOAD", "普通成员上传文件权限启/禁"),
    TIM_GROUP_ENABLE_DISABLE_ADD_MEMBER("TIM_GROUP_ENABLE_DISABLE_ADD_MEMBER", "允许普通成员拉人进群权限启/禁"),
    TIM_GROUP_ENABLE_DISABLE_ADD_FRIEND("TIM_GROUP_ENABLE_DISABLE_ADD_FRIEND", "群内私加好友权限启/禁"),
    TIM_GROUP_ENABLE_DISABLE_EXIT("TIM_GROUP_ENABLE_DISABLE_EXIT", "普通成员退出群组权限启/禁"),
    TIM_GROUP_EDIT_NOTIFICATION("TIM_GROUP_EDIT_NOTIFICATION", "修改群组公告"),
    TIM_GROUP_EDIT_INTRODUCTION("TIM_GROUP_EDIT_INTRODUCTION", "修改群组简介"),
    TIM_GROUP_COPY("TIM_GROUP_COPY", "一键复制创建新群"),
    TIM_GROUP_DESTROY("TIM_GROUP_DESTROY", "解散群组"),
    TIM_GROUP_EDIT("TIM_GROUP_EDIT", "修改群组信息"),
    TIM_GROUP_MEMBER_SHUT_UP("TIM_GROUP_MEMBER_SHUT_UP", "群组成员禁言"),
    TIM_GROUP_MEMBER_DELETE("TIM_GROUP_MEMBER_DELETE", "删除群组成员"),
    TIM_GROUP_MEMBER_ADD("TIM_GROUP_MEMBER_ADD", "添加群组成员"),
    TIM_GROUP_QRCODE_MEMBER_ADD("TIM_GROUP_QRCODE_MEMBER_ADD", "通过二维码加群"),
    TIM_GROUP_SET_MANAGER("TIM_GROUP_SET_MANAGER", "设置/取消群组管理员"),
    TIM_GROUP_MESSAGE_WITHDRAW("TIM_GROUP_MESSAGE_WITHDRAW", "撤回群组消息"),
    TIM_GROUP_MESSAGE_SEND("TIM_GROUP_MESSAGE_SEND", "发送群组文本消息"),
    TIM_GROUP_MESSAGE_LIST("TIM_GROUP_MESSAGE_LIST", "前台查询群聊记录"),

    TIM_GROUP_FILE_ADD("TIM_GROUP_FILE_ADD", "保存群文件"),
    TIM_GROUP_FILE_DELETE("TIM_GROUP_FILE_DELETE", "删除群文件"),

    TIM_FRIEND_ADD("TIM_FRIEND_ADD", "添加好友"),
    TIM_FRIEND_DELETE("TIM_FRIEND_DELETE", "删除好友"),
    TIM_FRIEND_DELETE_ALL("TIM_FRIEND_DELETE_ALL", "删除用户所有的好友"),

    TIM_BLACKLIST_ADD("TIM_BLACKLIST_ADD", "添加黑名单"),
    TIM_BLACKLIST_DELETE("TIM_BLACKLIST_DELETE", "删除黑名单"),

    TIM_GLOBAL_SHUT_UP_SET("TIM_GLOBAL_SHUT_UP_SET", "设置用户全局禁言"),

    TIM_SWITCH_IMPORT_ACCOUNT("TIM_SWITCH_IMPORT_ACCOUNT", "导入用户账号数据到腾讯IM"),
    TIM_SWITCH_KICK_ALL("TIM_SWITCH_KICK_ALL", "失效所有用户的登陆状态"),
    TIM_SET_ACCOUNT_PORTRAIT("TIM_SET_ACCOUNT_PORTRAIT", "设置账号信息"),
    TIM_IMPORT_FRIENDSHIP("TIM_IMPORT_FRIENDSHIP", "导入用户好友数据"),
    TIM_IMPORT_C2C_MESSAGE("TIM_IMPORT_C2C_MESSAGE", "导入单聊消息"),
    TIM_IMPORT_GROUP("TIM_IMPORT_GROUP", "导入群组"),
    TIM_IMPORT_GROUP_MEMBER("TIM_IMPORT_GROUP_MEMBER", "导入群组成员"),
    TIM_IMPORT_GROUP_MESSAGE("TIM_IMPORT_GROUP_MESSAGE", "导入群组消息"),

    TIM_C2C_MESSAGE_WITHDRAW("TIM_C2C_MESSAGE_WITHDRAW", "单聊消息撤回"),
    TIM_C2C_MESSAGE_SEND("TIM_C2C_MESSAGE_SEND", "发送单聊消息"),
    TIM_C2C_MESSAGE_EXPORT("TIM_C2C_MESSAGE_EXPORT", "导出单聊消息"),
    TIM_C2C_MESSAGE_LIST("TIM_C2C_MESSAGE_LIST", "查询单聊消息"),

    TIM_USER_KICK_OUT("TIM_USER_KICK_OUT", "踢下线"),
    TIM_FACE_ADD("TIM_FACE_ADD", "新增表情包专辑"),
    TIM_FACE_DELETE("TIM_FACE_DELETE", "删除表情包专辑"),
    TIM_FACE_ITEM_ADD("TIM_FACE_ITEM_ADD", "新增表情包元素"),
    TIM_FACE_ITEM_DELETE("TIM_FACE_ITEM_DELETE", "删除表情包元素"),

    USER_MOMENTS_ADD("USER_MOMENTS_ADD", "发布朋友圈"),
    USER_MOMENTS_DELETE("USER_MOMENTS_DELETE", "删除朋友圈"),
    USER_MOMENTS_REVIEW_ADD("USER_MOMENTS_REVIEW_ADD", "新增朋友圈评论"),
    USER_MOMENTS_REVIEW_DELETE("USER_MOMENTS_REVIEW_DELETE", "删除朋友圈评论"),
    USER_MOMENTS_LIKE_ADD("USER_MOMENTS_LIKE_ADD", "新增朋友圈点赞"),
    USER_MOMENTS_LIKE_DELETE("USER_MOMENTS_LIKE_DELETE", "删除朋友圈点赞"),

    APP_VERSION_DELETE("APP_VERSION_DELETE", "删除应用版本"),
    APP_VERSION_ADD("APP_VERSION_ADD", "新增应用版本"),

    USER_GROUP_ADD("USER_GROUP_ADD", "新增用户组"),
    USER_GROUP_EDIT("USER_GROUP_EDIT", "编辑用户组"),
    USER_GROUP_EDIT_USER("USER_GROUP_EDIT_USER", "编辑用户组用户"),
    USER_GROUP_EDIT_BANK_CARD_RECHARGE_CONFIG("USER_GROUP_EDIT_BANK_CARD_RECHARGE_CONFIG", "编辑用户组银行卡充值配置"),
    USER_GROUP_EDIT_API_RECHARGE_CONFIG("USER_GROUP_EDIT_API_RECHARGE_CONFIG", "编辑用户组三方充值配置"),
    USER_GROUP_DELETE("USER_GROUP_DELETE", "删除用户组"),

    PORTAL_NAVIGATOR_ADD("PORTAL_NAVIGATOR_ADD", "新增前台导航"),
    PORTAL_NAVIGATOR_EDIT("PORTAL_NAVIGATOR_EDIT", "编辑前台导航"),
    PORTAL_NAVIGATOR_ENABLE_DISABLE("PORTAL_NAVIGATOR_ENABLE_DISABLE", "启/禁前台导航"),
    PORTAL_NAVIGATOR_DELETE("PORTAL_NAVIGATOR_DELETE", "删除前台导航"),

    SENSITIVE_WORD_ADD("SENSITIVE_WORD_ADD", "新增敏感词"),
    SENSITIVE_WORD_DELETE("SENSITIVE_WORD_DELETE", "删除敏感词"),

    APP_AUTO_REPLY_CONFIG_DELETE("APP_AUTO_REPLY_CONFIG_DELETE", "删除自动回复"),
    APP_AUTO_REPLY_CONFIG_ADD("APP_AUTO_REPLY_CONFIG_ADD", "新增自动回复"),
    ;

    private String val;
    private String str;

    UserOperationLogTypeEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    @Override
    public String getVal() {
        return val;
    }

    @Override
    public String getStr() {
        return str;
    }

    @Override
    public String toString() {
        return this.val;
    }

}
