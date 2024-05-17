package com.im.common.util.api.im.tencent.request.api;

import lombok.Getter;

/**
 * 账号管理
 */
@Getter
public enum AccountApiEnum {

    account_import("v4/im_open_login_svc/account_import","导入单个帐号"),
    multi_account_import("v4/im_open_login_svc/multiaccount_import","导入多个帐号"),
    account_delete("v4/im_open_login_svc/account_delete","删除帐号"),
    account_check("v4/im_open_login_svc/account_check","查询帐号"),
    kick("v4/im_open_login_svc/kick","失效帐号登录态"),
    query_online_status("v4/openim/query_online_status","查询帐号在线状态");

    /**
     * 接口地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    AccountApiEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
