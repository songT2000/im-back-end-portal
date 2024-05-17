package com.im.common.util.api.pay.base.withdraw;

import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.entity.WithdrawOrder;

import java.util.List;
import java.util.Map;

/**
 * API代付处理基类
 *
 * @author Barry
 * @date 2020-08-29
 */
public interface ApiWithdrawHandler {
    /**
     * 请求代付
     *
     * @param config      配置
     * @param order       订单，不可以修改订单，改了也没用
     * @param callbackUrl 异步回调地址
     * @return
     * @throws Exception
     */
    ApiWithdrawRequestResponseVO requestWithdraw(ApiWithdrawConfig config, WithdrawOrder order, String callbackUrl) throws Exception;

    /**
     * 验证回调
     * 如果没有回调，则空实现则可
     *
     * @param config 配置
     * @param order  订单
     * @param params 三方请求参数
     * @return
     * @throws Exception
     */
    ApiWithdrawVerifyResult verifyWithdrawCallback(ApiWithdrawConfig config, WithdrawOrder order, Map<String, String> params) throws Exception;

    /**
     * 从三方批量查询订单
     * 如果没有查询，则返回空即可
     *
     * @param config    配置
     * @param orderList 订单列表
     * @return
     * @throws Exception
     */
    List<ApiWithdrawVerifyResult> queryWithdrawFromThird(ApiWithdrawConfig config, List<WithdrawOrder> orderList) throws Exception;

    /**
     * 获取成功时的输出，防止三方重复回调时，直接输出成功的内容给三方
     *
     * @param config 配置
     * @param order  订单
     * @param params 三方请求参数
     * @return
     */
    String getWithdrawSuccessOutput(ApiWithdrawConfig config, WithdrawOrder order, Map<String, String> params);

    /**
     * 是否是合法的三方配置
     *
     * @param config
     * @return
     */
    boolean isValidWithdrawThirdConfig(String config);
}
