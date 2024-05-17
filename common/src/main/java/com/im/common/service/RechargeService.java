package com.im.common.service;

import com.im.common.param.RechargeRequestPortalParam;
import com.im.common.response.RestResponse;
import com.im.common.util.api.pay.base.recharge.RechargeConfigGroupPortalVO;
import com.im.common.util.api.pay.base.recharge.RechargeRequestResponseVO;
import com.im.common.vo.PortalSessionUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 充值 服务类
 *
 * @author Barry
 * @date 2021-09-29
 */
public interface RechargeService {
    /**
     * 列出前台用户可用的充值配置列表，该接口前台不可缓存数据
     *
     * @param sessionUser
     * @return
     */
    List<RechargeConfigGroupPortalVO> listConfigForPortal(PortalSessionUser sessionUser);

    /**
     * 请求充值
     *
     * @param sessionUser
     * @param param
     * @param requestIp
     * @return
     */
    RestResponse<RechargeRequestResponseVO> requestForPortal(PortalSessionUser sessionUser, RechargeRequestPortalParam param, String requestIp);

    /**
     * 处理三方充值回调
     *
     * @param requestParams 请求参数
     * @param orderNum      订单号
     * @param requestIp     请求IP
     * @return 返回的data是需要返回给三方的
     */
    RestResponse<String> callbackByApi(Map<String, String> requestParams, String orderNum, String requestIp);

    /**
     * 同步三方充值状态
     *
     * @param startDateTime 订单创建开始时间
     * @param endDateTime   订单创建结束时间
     */
    void syncStatusFromApi(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
