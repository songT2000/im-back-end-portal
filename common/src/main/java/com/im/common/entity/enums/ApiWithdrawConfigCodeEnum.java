package com.im.common.entity.enums;

/**
 * 用来定义目前系统支持的所有三方代付列表
 *
 * @author Barry
 * @date 2021-03-20
 */
public enum ApiWithdrawConfigCodeEnum implements BaseEnum {
    /**
     * 测试三方
     **/
    TEST_1("TEST_1", "测试API代付1"),
    TEST_2("TEST_2", "测试API代付2"),
    TEST_3("TEST_3", "测试API代付3");

    private String val;
    private String str;

    ApiWithdrawConfigCodeEnum(String val, String str) {
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
