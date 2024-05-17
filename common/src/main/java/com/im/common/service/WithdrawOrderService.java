package com.im.common.service;

import com.im.common.entity.WithdrawOrder;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.WithdrawOrderAdminVO;

/**
 * 兑换提现订单 服务类
 *
 * @author Barry
 * @date 2021-09-29
 */
public interface WithdrawOrderService extends MyBatisPlusService<WithdrawOrder> {
    /**
     * 审核锁定/解锁
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<WithdrawOrderAdminVO> approveLockUnlockForAdmin(AdminSessionUser sessionUser, IdLockParam param);

    /**
     * 审核通过/拒绝
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<WithdrawOrderAdminVO> approveForAdmin(AdminSessionUser sessionUser, IdApproveParam param);

    /**
     * 打款锁定/解锁
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<WithdrawOrderAdminVO> payLockUnlockForAdmin(AdminSessionUser sessionUser, IdLockParam param);

    /**
     * 打款
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<WithdrawOrderAdminVO> payForAdmin(AdminSessionUser sessionUser, WithdrawOrderPayParam param);

    /**
     * 到账
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<WithdrawOrderAdminVO> payFinishForAdmin(AdminSessionUser sessionUser, IdFinishParam param);

    /**
     * 获取上次提现记录
     *
     * @param username
     * @return
     */
    WithdrawOrder getLastOrder(String username);

    /**
     * 人工提现
     *
     * @param sessionUser
     * @param param
     * @param requestIp
     * @return
     */
    RestResponse adminAddForAdmin(AdminSessionUser sessionUser, WithdrawOrderAdminAddParam param, String requestIp);
}
