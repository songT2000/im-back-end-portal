package com.im.common.response;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.constant.CommonConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有的状态码在这里定义
 * message接收如下几种格式
 * <p>
 * CommonConstant.NEED_I18N_SUFFIX：RestResponse会把message自动变成RSP_MSG.code()#I18N
 * XXX.XXX#I18N：不会变更字符，会前往i18n_translate表找相应翻译
 * 其它任意字符不以#I18N结尾：不会变更字符，直接输出
 * <p>
 *
 * @author Barry
 * @date 2018/5/12
 */
public enum ResponseCode {
    /**
     * 请求成功
     **/
    OK("OK"),

    /***************************系统类***************************/
    SYS_SERVER_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    SYS_NO_SUCH_METHOD(CommonConstant.NEED_I18N_SUFFIX),
    SYS_CONTENT_TYPE_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    SYS_NO_PERMISSION(CommonConstant.NEED_I18N_SUFFIX),
    SYS_METHOD_UNDER_CONSTRUCTION(CommonConstant.NEED_I18N_SUFFIX),
    SYS_CUSTOM_MSG("{}"),
    SYS_REQUEST_REPEAT(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REQUEST_REPEAT_BY_IP(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REQUEST_ACQUIRE_LOCK_FAILED(CommonConstant.NEED_I18N_SUFFIX),

    SYS_REQUEST_PARAM_MISSING(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REQUEST_PARAM_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE("{}"),
    SYS_REQUEST_ILLEGAL(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REQUEST_PARAM_ILLEGAL(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REQUEST_PROCESSING(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REQUEST_TIMEOUT(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REQUEST_LIMITED(CommonConstant.NEED_I18N_SUFFIX),

    SYS_DATA_STATUS_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    SYS_DATA_NOT_EDITABLE(CommonConstant.NEED_I18N_SUFFIX),
    SYS_DATA_DELETED(CommonConstant.NEED_I18N_SUFFIX),
    SYS_DATA_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    SYS_DATA_NOT_EXIST(CommonConstant.NEED_I18N_SUFFIX),
    SYS_DATA_NOT_EXIST_OR_EXPIRED(CommonConstant.NEED_I18N_SUFFIX),
    SYS_DATA_SAME_DATABASE(CommonConstant.NEED_I18N_SUFFIX),
    IP_NOT_ALLOWED(CommonConstant.NEED_I18N_SUFFIX),
    IP_NOT_ILLEGAL_IPV4_IPV4MASK_IPV6(CommonConstant.NEED_I18N_SUFFIX),
    IP_FORMATTING_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    AREA_NOT_ALLOWED(CommonConstant.NEED_I18N_SUFFIX),
    SYS_ONLY_ONE_REQUEST_AT_SAME_TIME(CommonConstant.NEED_I18N_SUFFIX),

    SYS_DATA_ALREADY_FIRST(CommonConstant.NEED_I18N_SUFFIX),
    SYS_DATA_ALREADY_LAST(CommonConstant.NEED_I18N_SUFFIX),

    SYS_AMOUNT_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    SYS_AMOUNT_NO_PRECISION(CommonConstant.NEED_I18N_SUFFIX),
    SYS_AMOUNT_MAX_PRECISION_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    SYS_AMOUNT_NOT_IN_RANGE(CommonConstant.NEED_I18N_SUFFIX),
    SYS_AMOUNT_RANGE_FORMATTING_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    SYS_TIME_RANGE_FORMATTING_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),

    OSS_CONNECT_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    SYS_UPLOAD_FILE_NOT_EMPTY(CommonConstant.NEED_I18N_SUFFIX),
    SYS_UPLOAD_FILE_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    SYS_UPLOAD_FILE_ONLY_ACCEPT_JPG_OR_PNG(CommonConstant.NEED_I18N_SUFFIX),
    SYS_UPLOAD_FILE_SIZE_EXCEEDED(CommonConstant.NEED_I18N_SUFFIX),
    SYS_MAX_QUERY_PAST_DAY(CommonConstant.NEED_I18N_SUFFIX),
    SYS_EXPORT_DATETIME_RANGE_EXCEEDED(CommonConstant.NEED_I18N_SUFFIX),
    SYS_REMARK_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),

    IM_CONNECT_EXCEPTION(CommonConstant.NEED_I18N_SUFFIX),
    IM_EXECUTE_EXCEPTION(CommonConstant.NEED_I18N_SUFFIX),
    /***************************系统类***************************/

    /***************************角色类***************************/
    ADMIN_ROLE_NAME_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    ADMIN_ROLE_NOT_SELF_OR_LOWER_ROLE(CommonConstant.NEED_I18N_SUFFIX),
    ADMIN_ROLE_NOT_LOWER(CommonConstant.NEED_I18N_SUFFIX),
    /***************************角色类***************************/

    /***************************用户类***************************/
    YOUR_ACCOUNT_HAS_BEEN_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    YOUR_ACCOUNT_LOGIN_HAS_BEEN_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    YOUR_ACCOUNT_RECHARGE_HAS_BEEN_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    YOUR_ACCOUNT_WITHDRAW_HAS_BEEN_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    USER_ALREADY_LOGIN(CommonConstant.NEED_I18N_SUFFIX),
    USER_LOGIN_USERNAME_OR_PASSWORD_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    USER_LOGIN_USERNAME_OR_PASSWORD_ERROR_LEFT_TIME(CommonConstant.NEED_I18N_SUFFIX),
    USER_LOGIN_TOO_MANY_PWD_ERROR_TIMES(CommonConstant.NEED_I18N_SUFFIX),
    USER_ROLE_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    USER_ROLE_ILLEGAL(CommonConstant.NEED_I18N_SUFFIX),
    USER_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),

    USER_INSUFFICIENT_BALANCE(CommonConstant.NEED_I18N_SUFFIX),
    GOOGLE_CODE_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),
    GOOGLE_CODE_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    GOOGLE_CODE_DUPLICATED(CommonConstant.NEED_I18N_SUFFIX),
    GOOGLE_ALREADY_BIND(CommonConstant.NEED_I18N_SUFFIX),
    GOOGLE_NOT_YET_BIND(CommonConstant.NEED_I18N_SUFFIX),
    USER_USERNAME_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    USER_USERNAME_NOT_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    USER_NOT_ALLOW_OPERATE_SELF(CommonConstant.NEED_I18N_SUFFIX),
    USER_NOT_ALLOW_ADMIN(CommonConstant.NEED_I18N_SUFFIX),
    USER_SESSION_FORCE_LOGOUT(CommonConstant.NEED_I18N_SUFFIX),
    USER_SESSION_OTHER_LOGIN(CommonConstant.NEED_I18N_SUFFIX),
    USER_SESSION_INFO_CHANGED(CommonConstant.NEED_I18N_SUFFIX),
    USER_SESSION_INVALIDATE(CommonConstant.NEED_I18N_SUFFIX),
    USER_SESSION_EXPIRED(CommonConstant.NEED_I18N_SUFFIX),
    USER_SESSION_PLEASE_LOGIN_FIRST(CommonConstant.NEED_I18N_SUFFIX),
    USER_SESSION_TOKEN_MISSING(CommonConstant.NEED_I18N_SUFFIX),
    USER_SESSION_TOKEN_INVALID(CommonConstant.NEED_I18N_SUFFIX),
    USER_OLD_PASSWORD_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    USER_PASSWORD_ALREADY_BOUND(CommonConstant.NEED_I18N_SUFFIX),
    USER_NEW_PWD_MUST_DIFFERENT_OLD(CommonConstant.NEED_I18N_SUFFIX),
    USER_FUND_PASSWORD_NOT_YET_BIND(CommonConstant.NEED_I18N_SUFFIX),
    USER_FUND_PASSWORD_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    USER_FUND_PWD_MUST_DIFFERENT_LOGIN_PWD(CommonConstant.NEED_I18N_SUFFIX),
    USER_RECHARGE_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    USER_WITHDRAW_DISABLED(CommonConstant.NEED_I18N_SUFFIX),

    USER_REGISTER_INVITE_CODE_EXCEEDED(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_LIMIT_COUNT_BY_IP_EXCEEDED(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_INVITE_CODE_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_INVITE_CODE_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_INVITE_CODE_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_MOBILE_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_MOBILE_REPEAT(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_MOBILE_VERIFICATION_CODE_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_FUND_PWD_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),
    USER_REGISTER_AVATAR_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),

    INVITE_CODE_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),

    USER_WITHDRAW_NAME_MUST_BE_CHINESE(CommonConstant.NEED_I18N_SUFFIX),
    USER_WITHDRAW_NAME_ALREADY_BOUND(CommonConstant.NEED_I18N_SUFFIX),
    USER_WITHDRAW_NAME_NOT_YET_BIND(CommonConstant.NEED_I18N_SUFFIX),
    /***************************用户类***************************/

    /***************************用户关系类***************************/
    NOT_YOUR_FRIEND(CommonConstant.NEED_I18N_SUFFIX),
    /***************************用户关系类***************************/

    /***************************用户组类***************************/
    USER_GROUP_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    USER_GROUP_NAME_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    USER_GROUP_USER_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    USER_GROUP_BANK_CARD_RECHARGE_CONFIG_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    USER_GROUP_API_RECHARGE_CONFIG_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    /***************************用户组类***************************/

    /***************************充提类***************************/
    BANK_CARD_RECHARGE_CONFIG_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    RECHARGE_NO_CARD_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    RECHARGE_USER_CARD_NAME_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),
    WITHDRAW_MAX_DAILY_REQUEST_EXCEEDED(CommonConstant.NEED_I18N_SUFFIX),
    WITHDRAW_MAX_SAME_TIME_REQUEST_EXCEEDED(CommonConstant.NEED_I18N_SUFFIX),
    WITHDRAW_LIMIT_NOT_YET_FINISH(CommonConstant.NEED_I18N_SUFFIX),
    WITHDRAW_NOT_IN_ENABLE_TIME(CommonConstant.NEED_I18N_SUFFIX),
    /***************************充提类***************************/

    /***************************三方充值配置类***************************/
    API_RECHARGE_CONFIG_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    API_RECHARGE_THIRD_CONFIG_FORMATTING_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    API_RECHARGE_HANDLER_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    API_RECHARGE_REQUEST_EXCEPTION(CommonConstant.NEED_I18N_SUFFIX),
    API_RECHARGE_REQUEST_RETURN_FAILED(CommonConstant.NEED_I18N_SUFFIX),
    API_RECHARGE_CALLBACK_PROCESS_FAILED(CommonConstant.NEED_I18N_SUFFIX),
    /***************************三方充值配置类***************************/

    /***************************API代付配置类***************************/
    API_WITHDRAW_CONFIG_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    API_WITHDRAW_THIRD_CONFIG_FORMATTING_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    API_WITHDRAW_CONFIG_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    API_WITHDRAW_HANDLER_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    API_WITHDRAW_REQUEST_EXCEPTION(CommonConstant.NEED_I18N_SUFFIX),
    API_WITHDRAW_REQUEST_RETURN_FAILED(CommonConstant.NEED_I18N_SUFFIX),
    API_WITHDRAW_CALLBACK_PROCESS_FAILED(CommonConstant.NEED_I18N_SUFFIX),
    /***************************API代付配置类***************************/

    /**************************银行卡相关***************************/
    BANK_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    BANK_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    BANK_NAME_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    BANK_CODE_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    USER_BANK_CARD_NUM_IN_BLACKLIST(CommonConstant.NEED_I18N_SUFFIX),
    USER_BANK_CARD_CARD_NUM_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    USER_BANK_CARD_REQUIRED(CommonConstant.NEED_I18N_SUFFIX),
    BANK_WITHDRAW_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    USER_BANK_CARD_MAX_BIND_COUNT_EXCEEDED(CommonConstant.NEED_I18N_SUFFIX),
    USER_BANK_CARD_NOT_YET_BIND(CommonConstant.NEED_I18N_SUFFIX),
    USER_BANK_CARD_NOT_FOUND(CommonConstant.NEED_I18N_SUFFIX),
    USER_BANK_CARD_DISABLED(CommonConstant.NEED_I18N_SUFFIX),
    /**************************银行卡相关***************************/

    /**************************朋友圈***************************/
    USER_MOMENTS_EXCEED_IMG_COUNT(CommonConstant.NEED_I18N_SUFFIX),
    USER_MOMENTS_NOT_READ(CommonConstant.NEED_I18N_SUFFIX),
    USER_MOMENTS_ALLOW(CommonConstant.NEED_I18N_SUFFIX),
    USER_MOMENTS_REJECT(CommonConstant.NEED_I18N_SUFFIX),
    /**************************朋友圈***************************/
    /**************************报表***************************/
    REPORT_DATA_MAX_SEARCH_DAY(CommonConstant.NEED_I18N_SUFFIX),
    REPORT_DATE_ERROR(CommonConstant.NEED_I18N_SUFFIX),
    REPORT_USER_EXISTED(CommonConstant.NEED_I18N_SUFFIX),
    /**************************报表***************************/

    /**************************国际化***************************/
    I18N_LANGUAGE_LIST_NOT_AVAILABLE(CommonConstant.NEED_I18N_SUFFIX),
    I18N_LANGUAGE_NOT_AVAILABLE(CommonConstant.NEED_I18N_SUFFIX),
    /**************************国际化***************************/

    /**************************红包***************************/
    ENVELOPE_EXPIRED(CommonConstant.NEED_I18N_SUFFIX),
    ENVELOPE_RECEIVED(CommonConstant.NEED_I18N_SUFFIX),
    ENVELOPE_ALL_RECEIVED(CommonConstant.NEED_I18N_SUFFIX),
    ENVELOPE_NOT_TO_SELF(CommonConstant.NEED_I18N_SUFFIX),
    ENVELOPE_MAX_NUM_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    ENVELOPE_AVERAGE_AMOUNT_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    /**************************红包***************************/

    /**************************TENCENT IM业务有关错误***************************/
    USER_IN_GROUP_CONFLICT(CommonConstant.NEED_I18N_SUFFIX),
    MESSAGE_CAN_NOT_WITHDRAW(CommonConstant.NEED_I18N_SUFFIX),
    USER_NOT_IN_GROUP(CommonConstant.NEED_I18N_SUFFIX),
    USER_GROUP_ALL_SHUTTING_UP(CommonConstant.NEED_I18N_SUFFIX),
    USER_GROUP_SHUTTING_UP(CommonConstant.NEED_I18N_SUFFIX),

    /**************************短信类***************************/
    SMS_THIRD_CONNECT_EXCEPTION(CommonConstant.NEED_I18N_SUFFIX),
    SMS_THIRD_CONNECT_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    SMS_VERIFICATION_CODE_EXPIRED(CommonConstant.NEED_I18N_SUFFIX),
    SMS_VERIFICATION_CODE_INCORRECT(CommonConstant.NEED_I18N_SUFFIX),
    SMS_COUNTRY_NOT_SUPPORT(CommonConstant.NEED_I18N_SUFFIX),
    /**************************短信类***************************/

    /**************************app版本管理***************************/
    APP_VERSION_CODE_MUST_INCREASE(CommonConstant.NEED_I18N_SUFFIX);
    /**************************app版本管理***************************/


    private String message;

    static Map<String, ResponseCode> enumMap = new HashMap<>();

    static {
        for (ResponseCode rsp : ResponseCode.values()) {
            enumMap.put(rsp.name(), rsp);
        }
    }

    ResponseCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseCode getEnum(String code) {
        return enumMap.get(code);
    }

    /**
     * 是否是正确返回编码
     *
     * @param rsp 编码
     * @return boolean
     */
    @JSONField(serialize = false)
    public static boolean isOkRsp(ResponseCode rsp) {
        return rsp != null && rsp == OK;
    }

    /**
     * 是否是正确返回编码
     *
     * @param code 编码
     * @return boolean
     */
    @JSONField(serialize = false)
    public static boolean isOkRsp(String code) {
        if (StrUtil.isBlank(code)) {
            return false;
        }
        ResponseCode rsp = getEnum(code);
        return isOkRsp(rsp);
    }

    @Override
    public String toString() {
        return this.name();
    }
}
