package com.im.common.entity.enums;

import lombok.Getter;

/**
 * 申请加群处理方式
 */
@Getter
public enum GroupApplyJoinOptionEnum implements BaseEnum{
    FreeAccess("FreeAccess","自由加入"),
    NeedPermission("NeedPermission","需要验证"),
    DisableApply("DisableApply","禁止加群"),
    ;
    GroupApplyJoinOptionEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    private String val;
    private String str;

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
