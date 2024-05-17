package com.im.common.util.api.pay.base.recharge;

import com.im.common.entity.ApiRechargeConfig;
import com.im.common.entity.RechargeOrder;

import java.util.List;
import java.util.Map;

/**
 * 三方充值处理基类
 *
 * @author Barry
 * @date 2020-08-29
 */
public interface ApiRechargeHandler {
    /**
     * 请求充值，子类可以填类订单的一些三方返回的属性，会存入数据库
     *
     * @param config      配置
     * @param order       订单，子类可以填类订单的一些三方返回的属性，会存入数据库
     * @param callbackUrl 异步回调地址
     * @return
     * @throws Exception
     */
    ApiRechargeRequestResponseVO requestRecharge(ApiRechargeConfig config,
                                                 RechargeOrder order,
                                                 String callbackUrl) throws Exception;

    /**
     * 验证回调
     * 如果没有回调，返回null则可
     *
     * @param config 充值配置
     * @param order  订单
     * @param param 三方请求参数
     * @return
     * @throws Exception
     */
    ApiRechargeVerifyResult verifyRechargeCallback(ApiRechargeConfig config, RechargeOrder order, Map<String, String> param) throws Exception;

    /**
     * 从三方批量查询订单
     * 如果没有查询接口，返回null则可
     *
     * @param config    充值配置
     * @param orderList 订单列表
     * @return
     * @throws Exception
     */
    List<ApiRechargeVerifyResult> queryRechargeFromThird(ApiRechargeConfig config, List<RechargeOrder> orderList) throws Exception;

    /**
     * 获取成功时的输出，防止三方重复回调时，直接输出成功的内容给三方
     *
     * @param config 充值配置
     * @param order  订单
     * @param param 三方请求参数
     * @return
     */
    String getRechargeSuccessOutput(ApiRechargeConfig config, RechargeOrder order, Map<String, String> param);

    /**
     * 是否是合法的三方配置
     *
     * @param config
     * @return
     */
    boolean isValidRechargeThirdConfig(String config);
}
