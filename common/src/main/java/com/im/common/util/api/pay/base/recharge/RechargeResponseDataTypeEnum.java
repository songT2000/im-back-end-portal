package com.im.common.util.api.pay.base.recharge;


import com.im.common.entity.enums.BaseEnum;

/**
 * 请求充值返回数据类型
 *
 * @author Barry
 * @date 2019-11-06
 */
public enum RechargeResponseDataTypeEnum implements BaseEnum {
    /**
     * 跳转链接
     **/
    URL("URL", "跳转链接，data就是链接，直接跳过去"),
    FORM("FORM", "FORM表单，参考RechargeResponseForm"),
    BANK_CARD("BANK_CARD", "银行卡，参考RechargeResponseBankCard");

    private String val;
    private String str;

    RechargeResponseDataTypeEnum(String val, String str) {
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
