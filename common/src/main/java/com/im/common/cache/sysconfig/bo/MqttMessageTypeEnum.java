package com.im.common.cache.sysconfig.bo;

import com.im.common.entity.enums.BaseEnum;

/**
 * MQTT消息类型
 *
 * @author Barry
 * @date 2021-03-01
 */
public enum MqttMessageTypeEnum implements BaseEnum {
    /**
     * 市场详情
     **/
    MARKET_DETAIL("MARKET_DETAIL", "市场详情"),
    /**
     * 市场K线
     **/
    MARKET_KLINE("MARKET_KLINE", "市场K线"),
    /**
     * 成交明细
     **/
    MARKET_TRADE("MARKET_TRADE", "成交明细"),
    /**
     * 极速交易订单中奖
     **/
    TIMING_TRADE_ORDER_WIN("TIMING_TRADE_ORDER_WIN", "极速交易订单中奖");

    private String val;
    private String str;

    MqttMessageTypeEnum(String val, String str) {
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
