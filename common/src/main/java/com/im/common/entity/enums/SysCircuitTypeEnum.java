package com.im.common.entity.enums;

/**
 * 系统菜单和权限表 -> 类型
 *
 * @author Barry
 * @date 2019-11-06
 */
public enum SysCircuitTypeEnum implements BaseEnum {
    /**
     * 菜单
     **/
    WEB("1", "WEB"),

    /**
     * 按钮
     **/
    APP("2", "APP");

    private String val;
    private String str;

    SysCircuitTypeEnum(String val, String str) {
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
