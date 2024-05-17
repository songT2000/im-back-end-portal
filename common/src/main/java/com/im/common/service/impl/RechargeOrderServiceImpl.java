package com.im.common.service.impl;

import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.entity.PortalUser;
import com.im.common.entity.RechargeOrder;
import com.im.common.entity.RechargeOrderLog;
import com.im.common.entity.enums.*;
import com.im.common.mapper.RechargeOrderMapper;
import com.im.common.param.RechargeOrderAdminAddParam;
import com.im.common.param.RechargeOrderPatchAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserService;
import com.im.common.service.RechargeOrderLogService;
import com.im.common.service.RechargeOrderService;
import com.im.common.util.*;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.RechargeOrderAdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 充值订单 服务实现类
 *
 * @author Barry
 * @date 2021-08-21
 */
@Service
public class RechargeOrderServiceImpl
        extends MyBatisPlusServiceImpl<RechargeOrderMapper, RechargeOrder>
        implements RechargeOrderService {
    private PortalUserService portalUserService;
    private RechargeOrderLogService rechargeOrderLogService;
    private SysConfigCache sysConfigCache;

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setRechargeOrderLogService(RechargeOrderLogService rechargeOrderLogService) {
        this.rechargeOrderLogService = rechargeOrderLogService;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse patchForAdmin(AdminSessionUser sessionUser, RechargeOrderPatchAdminParam param) {
        if (!NumberUtil.isGreatThenZero(param.getPayAmount())) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        RechargeOrder order = getById(param.getId());
        if (order == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        RechargeOrderAdminVO vo = new RechargeOrderAdminVO(order, null, null, null);
        if (!Boolean.TRUE.equals(vo.getAllowPatch())) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        BigDecimal serviceCharge = NumberUtil.mul(param.getPayAmount(), param.getServiceChargePercent());
        BigDecimal receiveAmount = NumberUtil.sub(param.getPayAmount(), serviceCharge);

        order.setPayAmount(param.getPayAmount());
        order.setReceiveAmount(receiveAmount);
        order.setServiceChargePercent(param.getServiceChargePercent());
        order.setServiceCharge(serviceCharge);
        order.setStatus(RechargeOrderStatusEnum.FINISHED);
        order.setFinishType(RechargeOrderFinishTypeEnum.ADMIN_PATCHED);
        order.setFinishTime(LocalDateTime.now());
        order.setRemark(StrUtil.trim(param.getRemark()));

        // 修改订单
        boolean updated = lambdaUpdate()
                .eq(RechargeOrder::getId, order.getId())
                .eq(RechargeOrder::getStatus, RechargeOrderStatusEnum.WAITING)
                .set(RechargeOrder::getPayAmount, order.getPayAmount())
                .set(RechargeOrder::getPayAmount, order.getPayAmount())
                .set(RechargeOrder::getReceiveAmount, order.getReceiveAmount())
                .set(RechargeOrder::getServiceChargePercent, order.getServiceChargePercent())
                .set(RechargeOrder::getServiceCharge, order.getServiceCharge())
                .set(RechargeOrder::getStatus, order.getStatus())
                .set(RechargeOrder::getFinishType, order.getFinishType())
                .set(RechargeOrder::getFinishTime, order.getFinishTime())
                .set(RechargeOrder::getRemark, order.getRemark())
                .update();

        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        // 给用户加余额，以到账为准
        portalUserService.addBalanceWithReportDate(order.getUserId(), order.getReceiveAmount(), order.getOrderNum(),
                UserBillTypeEnum.USER_RECHARGE, order.getRemark(), order.getReportDate(), true);

        // TODO 报表

        // 修改用户首充数据
        portalUserService.updateFirstAndTotalRecharge(order.getUserId(), order.getPayAmount(), order.getCreateTime());

        // 日志
        RechargeOrderLog log = new RechargeOrderLog(order.getId(), "管理员{}补单，实付：{}，手续费比例：{}，备注: {}",
                sessionUser.getUsername(), NumberUtil.toStr(param.getPayAmount()),
                NumberUtil.pointToStr(param.getServiceChargePercent()), StrUtil.blankToDefault(order.getRemark(), ""));
        rechargeOrderLogService.save(log);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse adminAddForAdmin(AdminSessionUser sessionUser, RechargeOrderAdminAddParam param, String requestIp) {
        PortalUser user = portalUserService.getByUsername(param.getUsername());
        if (user == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND, param.getUsername());
        }
        // 充值金额
        if (param.getAmount() != null && NumberUtil.isLess(param.getAmount(), BigDecimal.ZERO)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }
        // 赠送金额
        if (param.getGiveAmount() != null && NumberUtil.isLess(param.getGiveAmount(), BigDecimal.ZERO)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }
        if (NumberUtil.isNullOrZero(param.getAmount()) && NumberUtil.isNullOrZero(param.getGiveAmount())) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        // 日志
        List<RechargeOrderLog> logList = new ArrayList<>();
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        LocalDateTime now = LocalDateTime.now();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());

        // 充值金额
        if (NumberUtil.isGreatThenZero(param.getAmount())) {
            RechargeOrder order = new RechargeOrder();
            order.setOrderNum(OrderUtil.rechargeOrderNumber());
            order.setUserId(user.getId());
            order.setRequestAmount(param.getAmount());
            order.setPayAmount(param.getAmount());
            order.setReceiveAmount(param.getAmount());
            order.setServiceCharge(BigDecimal.ZERO);
            order.setServiceChargePercent(BigDecimal.ZERO);
            order.setStatus(RechargeOrderStatusEnum.FINISHED);
            order.setType(RechargeOrderTypeEnum.ADMIN_ADD);
            order.setRequestIp(requestIp);
            order.setRemark(StrUtil.trim(param.getRemark()));
            order.setFinishTime(now);
            order.setCreateTime(now);
            order.setUpdateTime(now);
            order.setReportDate(reportDate);
            save(order);

            // 给用户加余额，以实际支付为准
            portalUserService.addBalanceWithReportDate(order.getUserId(), order.getReceiveAmount(), order.getOrderNum(),
                    UserBillTypeEnum.ADMIN_RECHARGE, order.getRemark(), order.getReportDate(), true);

            // TODO 报表

            // 修改用户首充数据
            portalUserService.updateFirstAndTotalRecharge(order.getUserId(), order.getPayAmount(), order.getCreateTime());

            logList.add(new RechargeOrderLog(order.getId(), "管理员{}人工充值, 备注: {}", sessionUser.getUsername(),
                    StrUtil.blankToDefault(order.getRemark(), "")));
        }

        // 赠送金额
        if (NumberUtil.isGreatThenZero(param.getGiveAmount())) {
            RechargeOrder order = new RechargeOrder();
            order.setOrderNum(OrderUtil.rechargeOrderNumber());
            order.setUserId(user.getId());
            order.setRequestAmount(param.getGiveAmount());
            order.setPayAmount(param.getGiveAmount());
            order.setReceiveAmount(param.getGiveAmount());
            order.setServiceCharge(BigDecimal.ZERO);
            order.setServiceChargePercent(BigDecimal.ZERO);
            order.setStatus(RechargeOrderStatusEnum.FINISHED);
            order.setType(RechargeOrderTypeEnum.ADMIN_ADD_GIVE);
            order.setRequestIp(requestIp);
            order.setRemark(StrUtil.trim(param.getRemark()));
            order.setFinishTime(now);
            order.setCreateTime(now);
            order.setUpdateTime(now);
            order.setReportDate(reportDate);
            save(order);

            // 给用户加余额，以实际支付为准
            portalUserService.addBalanceWithReportDate(order.getUserId(), order.getReceiveAmount(), order.getOrderNum(),
                    UserBillTypeEnum.ADMIN_RECHARGE_GIVE, order.getRemark(), order.getReportDate(), true);

            // TODO 报表

            // 修改用户首充数据
            portalUserService.updateFirstAndTotalRecharge(order.getUserId(), order.getPayAmount(), order.getCreateTime());

            logList.add(new RechargeOrderLog(order.getId(), "管理员{}人工充值赠送, 备注: {}",
                    sessionUser.getUsername(), StrUtil.blankToDefault(order.getRemark(), "")));
        }

        // 保存日志
        if (CollectionUtil.isNotEmpty(logList)) {
            rechargeOrderLogService.saveBatch(logList);
        }

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public RechargeOrder getLastOrder(String username) {
        Long userId = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
        if (userId == null) {
            return null;
        }

        List<RechargeOrder> list = lambdaQuery()
                .eq(RechargeOrder::getUserId, userId)
                .eq(RechargeOrder::getStatus, RechargeOrderStatusEnum.FINISHED)
                .orderByDesc(RechargeOrder::getId)
                .last("limit 1")
                .list();

        return CollectionUtil.isEmpty(list) ? null : list.get(0);
    }
}
