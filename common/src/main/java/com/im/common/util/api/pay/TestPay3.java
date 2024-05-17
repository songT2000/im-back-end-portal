package com.im.common.util.api.pay;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.entity.ApiRechargeConfig;
import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.entity.RechargeOrder;
import com.im.common.entity.WithdrawOrder;
import com.im.common.entity.enums.ApiRechargeConfigCodeEnum;
import com.im.common.entity.enums.ApiWithdrawConfigCodeEnum;
import com.im.common.util.CollectionUtil;
import com.im.common.util.api.pay.base.recharge.*;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawHandler;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawHandlerProperty;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawRequestResponseVO;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawVerifyResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试
 *
 * @author Barry
 * @date 2021-03-23
 */
@ApiRechargeHandlerProperty(ApiRechargeConfigCodeEnum.TEST_3)
@ApiWithdrawHandlerProperty(ApiWithdrawConfigCodeEnum.TEST_3)
public class TestPay3 implements ApiRechargeHandler, ApiWithdrawHandler {
    private static final Log LOG = LogFactory.get();
    private static final String SUCCESS_OUTPUT = "SUCCESS";
    private static final String FAILED_OUTPUT = "FAILED";

    @Override
    public ApiRechargeRequestResponseVO requestRecharge(ApiRechargeConfig config,
                                                        RechargeOrder order,
                                                        String callbackUrl) throws Exception {
        // test
        Map<String, String> param = new HashMap<>();
        param.put("key1", "value1");
        param.put("key2", "value2");
        param.put("key3", "value3");
        param.put("key4", "value4");

        RechargeRequestResponseVO rsp = RechargeRequestResponseVO.form(order.getId(), "POST", "https://www.baidu.com", param);

        return ApiRechargeRequestResponseVO.success(rsp);
    }

    @Override
    public ApiRechargeVerifyResult verifyRechargeCallback(ApiRechargeConfig config, RechargeOrder order, Map<String, String> param) throws Exception {
        return ApiRechargeVerifyResult.finished(order.getId(), null, order.getRequestAmount(), SUCCESS_OUTPUT);
    }

    @Override
    public List<ApiRechargeVerifyResult> queryRechargeFromThird(ApiRechargeConfig config, List<RechargeOrder> orderList) throws Exception {
        List<ApiRechargeVerifyResult> rspList = new ArrayList<>();
        for (RechargeOrder order : orderList) {
            ApiRechargeVerifyResult rsp = ApiRechargeVerifyResult.finished(order.getId(), null,
                    order.getRequestAmount(), SUCCESS_OUTPUT);
            if (rsp != null) {
                rspList.add(rsp);
            }
        }
        return rspList;
    }

    @Override
    public String getRechargeSuccessOutput(ApiRechargeConfig config, RechargeOrder order, Map<String, String> param) {
        return SUCCESS_OUTPUT;
    }

    @Override
    public boolean isValidRechargeThirdConfig(String config) {
        return true;
    }

    @Override
    public ApiWithdrawRequestResponseVO requestWithdraw(ApiWithdrawConfig config, WithdrawOrder order, String callbackUrl) throws Exception {
        return ApiWithdrawRequestResponseVO.success();
    }

    @Override
    public ApiWithdrawVerifyResult verifyWithdrawCallback(ApiWithdrawConfig config, WithdrawOrder order, Map<String, String> params) throws Exception {
        return ApiWithdrawVerifyResult.paySuccess(order.getId(), SUCCESS_OUTPUT);
    }

    @Override
    public List<ApiWithdrawVerifyResult> queryWithdrawFromThird(ApiWithdrawConfig config, List<WithdrawOrder> orderList) throws Exception {
        return CollectionUtil.toList(orderList, e -> ApiWithdrawVerifyResult.paySuccess(e.getId(), SUCCESS_OUTPUT));
    }

    @Override
    public String getWithdrawSuccessOutput(ApiWithdrawConfig config, WithdrawOrder order, Map<String, String> params) {
        return SUCCESS_OUTPUT;
    }

    @Override
    public boolean isValidWithdrawThirdConfig(String config) {
        return true;
    }
}
