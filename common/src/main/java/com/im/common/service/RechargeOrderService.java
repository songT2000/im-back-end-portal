package com.im.common.service;

import com.im.common.entity.RechargeOrder;
import com.im.common.param.RechargeOrderAdminAddParam;
import com.im.common.param.RechargeOrderPatchAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * 充值订单 服务类
 *
 * @author Barry
 * @date 2021-06-26
 */
public interface RechargeOrderService extends MyBatisPlusService<RechargeOrder> {
    /**
     * 补单
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse patchForAdmin(AdminSessionUser sessionUser, RechargeOrderPatchAdminParam param);

    /**
     * 人工充值
     *
     * @param sessionUser
     * @param param
     * @param requestIp
     * @return
     */
    RestResponse adminAddForAdmin(AdminSessionUser sessionUser, RechargeOrderAdminAddParam param, String requestIp);

    /**
     * 获取上次充值记录
     *
     * @param username
     * @return
     */
    RechargeOrder getLastOrder(String username);
}
