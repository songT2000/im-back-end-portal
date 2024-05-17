package com.im.portal.controller.url;

/**
 * 定义API地址
 *
 * @author Barry
 * @date 2020-06-01
 */
public final class ApiUrl {
    public static final String BASE_NO_AUTH_URL = "/api/portal";
    public static final String BASE_AUTH_URL = "/api/portal/auth";
    /**
     * 测速
     */
    public static final String SPEED_TEST = BASE_NO_AUTH_URL + "/speed-test";
    /**
     * 用户登录
     **/
    public static final String LOGIN = BASE_NO_AUTH_URL + "/user/login";
    /**
     * 注册
     **/
    public static final String REGISTER = BASE_NO_AUTH_URL + "/user/register";
    /**
     * 登出
     **/
    public static final String LOGOUT = BASE_NO_AUTH_URL + "/user/logout";
    /**
     * 检查用户名是否存在
     **/
    public static final String USER_USERNAME_EXIST = BASE_NO_AUTH_URL + "/user/username-exist";
    /**
     * 当前用户信息，与登录请求返回的数据一致
     **/
    public static final String GET_LOGIN_INFO = BASE_NO_AUTH_URL + "/user/get-login-info";
    /**
     * 修改当前登录用户登录密码
     **/
    public static final String EDIT_LOGIN_PWD = BASE_AUTH_URL + "/user/edit-login-pwd";
    /**
     * 绑定当前登录用户资金密码
     **/
    public static final String BIND_FUND_PWD = BASE_AUTH_URL + "/user/bind-fund-pwd";
    /**
     * 修改当前登录用户资金密码
     **/
    public static final String EDIT_FUND_PWD = BASE_AUTH_URL + "/user/edit-fund-pwd";
    /**
     * 绑定提现姓名
     **/
    public static final String BIND_WITHDRAW_NAME = BASE_AUTH_URL + "/user/bind-withdraw-name";
    /**
     * 获取任意上次登录IP
     **/
    public static final String USER_GET_ANYONE_LAST_LOGIN_IP = BASE_AUTH_URL + "/user/get-anyone-last-login-ip";
    /**
     * 我的账变列表
     */
    public static final String USER_BILL_PAGE = BASE_AUTH_URL + "/user-bill/page";
    /**
     * 获取系统配置列表-初始配置
     **/
    public static final String SYS_CONFIG_SIMPLE_LIST = BASE_NO_AUTH_URL + "/sys-config/simple-list";
    /**
     * 缓存时间戳
     **/
    public static final String SYS_CACHE_REFRESH_LIST = BASE_NO_AUTH_URL + "/sys-cache-refresh/list";
    /**
     * 启用的语言列表，无需登录
     **/
    public static final String I18N_LANGUAGE_SIMPLE_LIST = BASE_NO_AUTH_URL + "/i18n-language/simple-list";
    /**
     * 心跳服务，无需登录
     **/
    public static final String PING = BASE_NO_AUTH_URL + "/ping";
    /**
     * Android设备信息服务接口
     **/
    public static final String DEVICE_ANDROID = BASE_AUTH_URL + "/device/android";
    /**
     * iOS设备信息服务接口
     **/
    public static final String DEVICE_IOS = BASE_AUTH_URL + "/device/ios";
    /**
     * 公告分页列表
     */
    public static final String SYS_NOTICE_PAGE = BASE_AUTH_URL + "/sys-notice/page";
    /**
     * 公告列表
     */
    public static final String SYS_NOTICE_LIST = BASE_AUTH_URL + "/sys-notice/list";
    /**
     * 获取短信支持国家列表
     **/
    public static final String SMS_COUNTRY_LIST = BASE_NO_AUTH_URL + "/sms/country-list";
    /**
     * 发送短信验证码
     **/
    public static final String SMS_SEND = BASE_NO_AUTH_URL + "/sms/send";
    /**
     * 个人银行卡列表
     **/
    public static final String USER_BANK_CARD_LIST = BASE_AUTH_URL + "/user-bank-card/list";
    /**
     * 个人银行卡新增
     **/
    public static final String USER_BANK_CARD_ADD = BASE_AUTH_URL + "/user-bank-card/add";
    /**
     * 个人银行卡删除
     **/
    public static final String USER_BANK_CARD_DELETE = BASE_AUTH_URL + "/user-bank-card/delete";
    /**
     * banner列表
     */
    public static final String SYS_BANNER_LIST = BASE_AUTH_URL + "/sys-banner/list";
    /**
     * 上传ID
     **/
    public static final String FILE_UPLOAD_ID = BASE_AUTH_URL + "/file/upload-id";
    /**
     * 上传凭证
     **/
    public static final String FILE_UPLOAD_VOUCHER = BASE_AUTH_URL + "/file/upload-voucher";
    /**
     * 上传图片
     **/
    public static final String FILE_UPLOAD_IMAGE = BASE_AUTH_URL + "/file/upload/image";
    /**
     * 上传头像
     */
    public static final String FILE_UPLOAD_AVATAR = BASE_AUTH_URL + "/file/upload/avatar";
    /**
     * 注册上传头像
     */
    public static final String FILE_UPLOAD_AVATAR_NO_AUTH = BASE_NO_AUTH_URL + "/file/upload/avatar";
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
     * 充值配置
     */
    public static final String RECHARGE_CONFIG_LIST = BASE_AUTH_URL + "/recharge/config-list";
    /**
     * 充值请求
     */
    public static final String RECHARGE_REQUEST = BASE_AUTH_URL + "/recharge/request";
    /**
     * 我的充值订单
     */
    public static final String RECHARGE_ORDER_PAGE = BASE_AUTH_URL + "/recharge-order/page";
    /**
     * 所有提现银行列表
     **/
    public static final String BANK_SIMPLE_WITHDRAW_LIST = BASE_AUTH_URL + "/bank/simple-withdraw-list";
    /**
     * 提现配置
     */
    public static final String WITHDRAW_CONFIG_LIST = BASE_AUTH_URL + "/withdraw/config-list";
    /**
     * 提现请求
     */
    public static final String WITHDRAW_REQUEST = BASE_AUTH_URL + "/withdraw/request";
    /**
     * 我的提现订单
     */
    public static final String WITHDRAW_ORDER_PAGE = BASE_AUTH_URL + "/withdraw-order/page";
    /**
     * 发个人红包
     */
    public static final String PERSONAL_RED_ENVELOPE_SEND = BASE_AUTH_URL + "/personal-red-envelope/send";
    /**
     * 领个人红包
     */
    public static final String PERSONAL_RED_ENVELOPE_RECEIVE = BASE_AUTH_URL + "/personal-red-envelope/receive";
    /**
     * 退个人红包
     */
    public static final String PERSONAL_RED_ENVELOPE_REFUND = BASE_AUTH_URL + "/personal-red-envelope/refund";
    /**
     * 获取个人红包
     */
    public static final String PERSONAL_RED_ENVELOPE_GET = BASE_AUTH_URL + "/personal-red-envelope/get";
    /**
     * 发群红包
     */
    public static final String GROUP_RED_ENVELOPE_SEND = BASE_AUTH_URL + "/group-red-envelope/send";
    /**
     * 领群红包
     */
    public static final String GROUP_RED_ENVELOPE_RECEIVE = BASE_AUTH_URL + "/group-red-envelope/receive";
    /**
     * 获取群红包记录
     */
    public static final String GROUP_RED_ENVELOPE_GET = BASE_AUTH_URL + "/group-red-envelope/get";
    /**
     * 领群红包记录
     */
    public static final String GROUP_RED_ENVELOPE_RECEIVE_LIST = BASE_AUTH_URL + "/group-red-envelope/receive-list";
    /**
     * 删除群组成员
     */
    public static final String TIM_GROUP_MEMBER_DELETE = BASE_AUTH_URL + "/tim/group/member/delete";
    /**
     * 添加群组成员
     */
    public static final String TIM_GROUP_MEMBER_ADD = BASE_AUTH_URL + "/tim/group/member/add";
    /**
     * 通过二维码加群
     */
    public static final String TIM_GROUP_QRCODE_MEMBER_ADD = BASE_AUTH_URL + "/tim/group/member/qrcode-add";
    /**
     * 获取群组人数
     */
    public static final String TIM_GROUP_MEMBER_COUNT = BASE_AUTH_URL + "/tim/group/member/count";
    /**
     * 搜索群组成员
     */
    public static final String TIM_GROUP_MEMBER_PAGE = BASE_AUTH_URL + "/tim/group/member/page";

    /**
     * 查询所有群成员
     */
    public static final String TIM_GROUP_MEMBER_ALL = BASE_AUTH_URL + "/tim/group/member/all";
    /**
     * 群组全员禁言/解除禁言
     */
    public static final String TIM_GROUP_SHUT_UP = BASE_AUTH_URL + "/tim/group/shut-up";
    /**
     * 显示群内成员启/禁
     */
    public static final String TIM_GROUP_ENABLE_DISABLE_SHOW_MEMBER = BASE_AUTH_URL + "/tim/group/enable-disable-show-member";
    /**
     * 允许成员私聊启/禁
     */
    public static final String TIM_GROUP_ENABLE_DISABLE_ANONYMOUS_CHAT = BASE_AUTH_URL + "/tim/group/enable-disable-anonymous-chat";
    /**
     * 普通成员上传文件权限启/禁
     */
    public static final String TIM_GROUP_ENABLE_DISABLE_UPLOAD = BASE_AUTH_URL + "/tim/group/enable-disable-upload";
    /**
     * 允许普通成员拉人进群权限启/禁
     */
    public static final String TIM_GROUP_ENABLE_DISABLE_ADD_MEMBER = BASE_AUTH_URL + "/tim/group/enable-disable-add-member";
    /**
     * 群内私加好友权限启/禁
     */
    public static final String TIM_GROUP_ENABLE_DISABLE_ADD_FRIEND = BASE_AUTH_URL + "/tim/group/enable-disable-add-friend";

    /**
     * 普通成员退出群组权限启/禁
     */
    public static final String TIM_GROUP_ENABLE_DISABLE_EXIT = BASE_AUTH_URL + "/tim/group/enable-disable-exit";
    /**
     * 修改群组公告
     */
    public static final String TIM_GROUP_EDIT_NOTIFICATION = BASE_AUTH_URL + "/tim/group/edit-notification";
    /**
     * 修改群组简介
     */
    public static final String TIM_GROUP_EDIT_INTRODUCTION = BASE_AUTH_URL + "/tim/group/edit-introduction";
    /**
     * 撤回群组消息
     */
    public static final String TIM_GROUP_MESSAGE_WITHDRAW = BASE_AUTH_URL + "/tim/group/message/withdraw";
    /**
     * 一键复制创建新群
     */
    public static final String TIM_GROUP_COPY = BASE_AUTH_URL + "/tim/group/copy";
    /**
     * 获取群组信息
     */
    public static final String TIM_GROUP_INFO = BASE_AUTH_URL + "/tim/group/info";
    /**
     * 群文件-分页
     */
    public static final String TIM_GROUP_FILE_LIST = BASE_AUTH_URL + "/tim-group-file/list";
    /**
     * 获取表情包信息
     */
    public static final String TIM_FACE_LIST = BASE_NO_AUTH_URL + "/tim-face/list";

    /**
     * 获取用户自定义表情包信息
     */
    public static final String PORTAL_USER_EMOJI_LIST = BASE_AUTH_URL + "/portal-user-emoji/list";

    /**
     * 获取用户自定义表情包信息
     */
    public static final String PORTAL_USER_EMOJI_ADD = BASE_AUTH_URL + "/portal-user-emoji/add";

    /**
     * 获取用户自定义表情包信息
     */
    public static final String PORTAL_USER_EMOJI_DEL = BASE_AUTH_URL + "/portal-user-emoji/del";

    /**
     * 获取敏感词信息
     */
    public static final String SENSITIVE_WORD_LIST = BASE_NO_AUTH_URL + "/sensitive-word/list";
    /**
     * 朋友圈分页
     */
    public static final String USER_MOMENTS_PAGE = BASE_AUTH_URL + "/user-moments/page";
    /**
     * 朋友圈-发布
     */
    public static final String USER_MOMENTS_ADD = BASE_AUTH_URL + "/user-moments/add";
    /**
     * 朋友圈-删除
     */
    public static final String USER_MOMENTS_DELETE = BASE_AUTH_URL + "/user-moments/delete";
    /**
     * 朋友圈详情
     */
    public static final String USER_MOMENTS_DETAIL = BASE_AUTH_URL + "/user-moments/detail";
    /**
     * 朋友圈动态提醒
     */
    public static final String USER_MOMENTS_TRENDS = BASE_AUTH_URL + "/user-moments/trends";
    /**
     * 朋友圈提醒我看，动态提醒已读
     */
    public static final String USER_MOMENTS_TRENDS_CALL_READ = BASE_AUTH_URL + "/user-moments/trends-call-read";
    /**
     * 朋友圈评论，动态提醒已读
     */
    public static final String USER_MOMENTS_TRENDS_REVIEW_READ = BASE_AUTH_URL + "/user-moments/trends-review-read";
    /**
     * 某个人的朋友圈
     */
    public static final String USER_MOMENTS_BY_USER = BASE_AUTH_URL + "/user-moments/by-user";
    /**
     * 新增朋友圈评论
     */
    public static final String USER_MOMENTS_REVIEW_ADD = BASE_AUTH_URL + "/user-moments-review/add-review";
    /**
     * 删除朋友圈评论
     */
    public static final String USER_MOMENTS_REVIEW_DELETE = BASE_AUTH_URL + "/user-moments-review/delete-review";
    /**
     * 新增朋友圈点赞
     */
    public static final String USER_MOMENTS_LIKE_ADD = BASE_AUTH_URL + "/user-moments-review/add-like";
    /**
     * 删除朋友圈点赞
     */
    public static final String USER_MOMENTS_LIKE_DELETE = BASE_AUTH_URL + "/user-moments-review/delete-like";
    /**
     * 获取应用最新版本
     */
    public static final String APP_VERSION_LATEST = BASE_AUTH_URL + "/app-version/latest";
    /**
     * 获取应用最新版本(URL无需授权)
     */
    public static final String APP_VERSION_LATEST2 = BASE_NO_AUTH_URL + "/app-version/latest";
    /**
     * 前台导航列表
     */
    public static final String PORTAL_NAVIGATOR_LIST = BASE_AUTH_URL + "/portal-navigator/list";

    /**
     * 前台导航单条
     */
    public static final String PORTAL_NAVIGATOR_CLUB = BASE_AUTH_URL + "/portal-navigator/club";
    /**
     * 前台导航点击
     */
    public static final String PORTAL_NAVIGATOR_CLICK = BASE_AUTH_URL + "/portal-navigator/click";
    /**
     * 导出单聊记录
     */
    public static final String TIM_C2C_MESSAGE_EXPORT = BASE_AUTH_URL + "/tim-c2c-message/export";
    /**
     * 查询单聊记录
     */
    public static final String TIM_C2C_MESSAGE_LIST = BASE_AUTH_URL + "/tim-c2c-message/list";
    /**
     * 查询群聊记录
     */
    public static final String TIM_GROUP_MESSAGE_LIST = BASE_AUTH_URL + "/tim-group-message/list";

    public ApiUrl() {
    }
}
