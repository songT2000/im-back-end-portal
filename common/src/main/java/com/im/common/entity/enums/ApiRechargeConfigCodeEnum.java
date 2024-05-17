package com.im.common.entity.enums;

/**
 * 用来定义目前系统支持的所有三方充值列表
 *
 * @author Barry
 * @date 2021-03-20
 */
public enum ApiRechargeConfigCodeEnum implements BaseEnum {
    /**
     * 测试三方
     **/
    TEST_1("TEST_1", "测试三方1"),
    TEST_2("TEST_2", "测试三方2"),
    TEST_3("TEST_3", "测试三方3");

    private String val;
    private String str;

    ApiRechargeConfigCodeEnum(String val, String str) {
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
