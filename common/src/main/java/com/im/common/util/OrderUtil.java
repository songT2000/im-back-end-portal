package com.im.common.util;

import java.time.LocalDateTime;

/**
 * 注单工具类
 *
 * @author Barry
 * @date 2018/5/14
 */
public final class OrderUtil {

    /**
     * 生成20位的注单号，重复生成的机率几乎可以忽略
     *
     * @return
     */
    public synchronized static String orderNumberToMs() {
        String dateInfo = DateTimeUtil.toStr(LocalDateTime.now(), "yyMMddHHmmssSSS");
        return dateInfo ;
    }

    /**
     * 生成20位的注单号，重复生成的机率几乎可以忽略
     *
     * @return
     */
    public synchronized static String orderNumber() {
        String dateInfo = DateTimeUtil.toStr(LocalDateTime.now(), "yyMMddHHmmss");

        String randomStr = RandomUtil.randomString(8, true, false);

        return dateInfo + randomStr;
    }

    /**
     * 充值订单号
     *
     * @return
     */
    public synchronized static String rechargeOrderNumber() {
        return StrUtil.format("{}{}", "R", orderNumber());
    }


    /**
     * 提现订单号
     *
     * @return
     */
    public synchronized static String withdrawOrderNumber() {
        return StrUtil.format("{}{}", "W", orderNumber());
    }
}
