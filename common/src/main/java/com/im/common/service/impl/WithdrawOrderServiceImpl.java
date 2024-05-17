package com.im.common.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.cache.impl.ApiWithdrawConfigCache;
import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.BankCardWithdrawConfigCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.cache.sysconfig.bo.WithdrawConfigBO;
import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.entity.PortalUser;
import com.im.common.entity.WithdrawOrder;
import com.im.common.entity.WithdrawOrderLog;
import com.im.common.entity.enums.*;
import com.im.common.mapper.WithdrawOrderMapper;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.ApiWithdrawConfigService;
import com.im.common.service.PortalUserService;
import com.im.common.service.WithdrawOrderLogService;
import com.im.common.service.WithdrawOrderService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.OrderUtil;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawHandler;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawHandlerFactory;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawRequestResponseVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.WithdrawOrderAdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 兑换提现订单 服务实现类
 *
 * @author Barry
 * @date 2021-09-29
 */
@Service
public class WithdrawOrderServiceImpl
        extends MyBatisPlusServiceImpl<WithdrawOrderMapper, WithdrawOrder>
        implements WithdrawOrderService {
    private static final Log LOG = LogFactory.get();

    private SysConfigCache sysConfigCache;
    private BankCardWithdrawConfigCache bankCardWithdrawConfigCache;
    private BankCache bankCache;
    private ApiWithdrawConfigCache apiWithdrawConfigCache;
    private PortalUserService portalUserService;
    private WithdrawOrderLogService withdrawOrderLogService;
    private ApiWithdrawConfigService apiWithdrawConfigService;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setBankCardWithdrawConfigCache(BankCardWithdrawConfigCache bankCardWithdrawConfigCache) {
        this.bankCardWithdrawConfigCache = bankCardWithdrawConfigCache;
    }

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @Autowired
    public void setApiWithdrawConfigCache(ApiWithdrawConfigCache apiWithdrawConfigCache) {
        this.apiWithdrawConfigCache = apiWithdrawConfigCache;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setWithdrawOrderLogService(WithdrawOrderLogService withdrawOrderLogService) {
        this.withdrawOrderLogService = withdrawOrderLogService;
    }

    @Autowired
    public void setApiWithdrawConfigService(ApiWithdrawConfigService apiWithdrawConfigService) {
        this.apiWithdrawConfigService = apiWithdrawConfigService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<WithdrawOrderAdminVO> approveLockUnlockForAdmin(AdminSessionUser sessionUser, IdLockParam param) {
        WithdrawOrder order = getById(param.getId());
        if (order == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();
        WithdrawOrderAdminVO vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);

        // 是否允许锁定
        if (Boolean.TRUE.equals(param.getLock()) && !Boolean.TRUE.equals(vo.getAllowApproveLock())) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        // 是否允许解锁
        if (Boolean.FALSE.equals(param.getLock()) && !Boolean.TRUE.equals(vo.getAllowApproveUnlock())) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 再次锁定不做任何操作
        if (Boolean.TRUE.equals(param.getLock()) && sessionUser.getId().equals(order.getApproveAdminId())) {
            return RestResponse.ok(vo);
        }

        // 日志
        List<WithdrawOrderLog> logList = new ArrayList<>();

        if (Boolean.TRUE.equals(param.getLock())) {
            // 锁定
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .isNull(WithdrawOrder::getApproveAdminId)
                    .set(WithdrawOrder::getApproveAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getApproveTime, LocalDateTime.now())
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}审核锁定", sessionUser.getUsername()));
        } else {
            // 解锁
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .eq(WithdrawOrder::getApproveAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getApproveAdminId, null)
                    .set(WithdrawOrder::getApproveTime, null)
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}审核解锁", sessionUser.getUsername()));
        }

        if (CollectionUtil.isNotEmpty(logList)) {
            withdrawOrderLogService.saveBatch(logList);
        }

        order = getById(param.getId());
        vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);
        return RestResponse.ok(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<WithdrawOrderAdminVO> approveForAdmin(AdminSessionUser sessionUser, IdApproveParam param) {
        WithdrawOrder order = getById(param.getId());
        if (order == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();
        WithdrawOrderAdminVO vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);

        // 是否允许审核
        if (!Boolean.TRUE.equals(vo.getAllowApprove())) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 日志
        List<WithdrawOrderLog> logList = new ArrayList<>();

        if (Boolean.TRUE.equals(param.getApprove())) {
            // 通过
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .eq(WithdrawOrder::getApproveAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.APPROVE_SUCCESS)
                    .set(WithdrawOrder::getApproveTime, LocalDateTime.now())
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}审核通过", sessionUser.getUsername()));
        } else {
            // 拒绝必填备注
            if (StrUtil.isBlank(param.getRemark())) {
                return RestResponse.failed(ResponseCode.SYS_REMARK_REQUIRED);
            }
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .eq(WithdrawOrder::getApproveAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.APPROVE_DENY)
                    .set(WithdrawOrder::getApproveTime, LocalDateTime.now())
                    .set(WithdrawOrder::getRemark, StrUtil.trim(param.getRemark()))
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}审核拒绝，金额已原路退回，备注：{}",
                    sessionUser.getUsername(), StrUtil.blankToDefault(param.getRemark(), "")));

            // 退回提单金额
            portalUserService.addBalanceWithReportDate(order.getUserId(), order.getRequestAmount(), order.getOrderNum(),
                    UserBillTypeEnum.USER_WITHDRAW_FALLBACK, StrUtil.trim(param.getRemark()), order.getReportDate(), true);
        }

        if (CollectionUtil.isNotEmpty(logList)) {
            withdrawOrderLogService.saveBatch(logList);
        }

        order = getById(param.getId());
        vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);
        return RestResponse.ok(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<WithdrawOrderAdminVO> payLockUnlockForAdmin(AdminSessionUser sessionUser, IdLockParam param) {
        WithdrawOrder order = getById(param.getId());
        if (order == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();
        WithdrawOrderAdminVO vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);

        // 是否允许锁定
        if (Boolean.TRUE.equals(param.getLock()) && !Boolean.TRUE.equals(vo.getAllowPayLock())) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        // 是否允许解锁
        if (Boolean.FALSE.equals(param.getLock()) && !Boolean.TRUE.equals(vo.getAllowPayUnlock())) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 再次锁定不做任何操作
        if (Boolean.TRUE.equals(param.getLock()) && sessionUser.getId().equals(order.getPayAdminId())) {
            return RestResponse.ok(vo);
        }

        // 日志
        List<WithdrawOrderLog> logList = new ArrayList<>();

        if (Boolean.TRUE.equals(param.getLock())) {
            // 锁定
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .isNull(WithdrawOrder::getPayAdminId)
                    .set(WithdrawOrder::getPayAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getPayRequestTime, LocalDateTime.now())
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}打款锁定", sessionUser.getUsername()));
        } else {
            // 解锁
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .eq(WithdrawOrder::getPayAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getPayAdminId, null)
                    .set(WithdrawOrder::getPayRequestTime, null)
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}打款解锁", sessionUser.getUsername()));
        }

        if (CollectionUtil.isNotEmpty(logList)) {
            withdrawOrderLogService.saveBatch(logList);
        }

        order = getById(param.getId());
        vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);
        return RestResponse.ok(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<WithdrawOrderAdminVO> payForAdmin(AdminSessionUser sessionUser, WithdrawOrderPayParam param) {
        WithdrawOrder order = getById(param.getId());
        if (order == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();
        WithdrawOrderAdminVO vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);

        // 是否允许打款
        if (!Boolean.TRUE.equals(vo.getAllowPay())) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        if (Boolean.TRUE.equals(param.getApprove())) {
            if (param.getApiWithdrawConfigId() == null) {
                // 手动打款
                return manualPay(sessionUser, order, param, withdrawConfig);
            } else {
                // 使用API代付
                return apiPay(sessionUser, order, param, withdrawConfig);
            }
        } else {
            // 打款拒绝
            return payDeny(sessionUser, order, param, withdrawConfig);
        }
    }

    private RestResponse<WithdrawOrderAdminVO> manualPay(AdminSessionUser sessionUser, WithdrawOrder order,
                                                         WithdrawOrderPayParam param, WithdrawConfigBO withdrawConfig) {
        List<WithdrawOrderLog> logList = new ArrayList<>();

        // 手动打款
        WithdrawOrderStatusEnum toStatus;
        if (Boolean.TRUE.equals(withdrawConfig.getManualPayDirectSuccess())) {
            toStatus = WithdrawOrderStatusEnum.PAY_SUCCESS;
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}手动打款完成", sessionUser.getUsername()));
        } else {
            toStatus = WithdrawOrderStatusEnum.PAYING;
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}正在手动打款中", sessionUser.getUsername()));
        }

        // 修改
        boolean updated = lambdaUpdate()
                .eq(WithdrawOrder::getId, order.getId())
                .eq(WithdrawOrder::getStatus, order.getStatus())
                .eq(WithdrawOrder::getPayAdminId, sessionUser.getId())
                .set(WithdrawOrder::getStatus, toStatus)
                .set(WithdrawOrder::getPayType, WithdrawOrderPayTypeEnum.MANUAL_PAY)
                .set(WithdrawOrder::getPayRequestTime, LocalDateTime.now())
                .set(toStatus == WithdrawOrderStatusEnum.PAY_SUCCESS, WithdrawOrder::getFinishType, WithdrawOrderFinishTypeEnum.ADMIN_CONFIRMED)
                .set(toStatus == WithdrawOrderStatusEnum.PAY_SUCCESS, WithdrawOrder::getPayFinishTime, LocalDateTime.now())
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 直接完成订单需要修改报表等
        if (toStatus == WithdrawOrderStatusEnum.PAY_SUCCESS) {
            // 报表 TODO

            // 修改用户首提数据
            portalUserService.updateFirstAndTotalWithdraw(order.getUserId(), order.getRequestAmount(), order.getCreateTime());
        }

        withdrawOrderLogService.saveBatch(logList);

        order = getById(param.getId());
        WithdrawOrderAdminVO vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);
        return RestResponse.ok(vo);
    }

    private RestResponse<WithdrawOrderAdminVO> apiPay(AdminSessionUser sessionUser, WithdrawOrder order,
                                                      WithdrawOrderPayParam param, WithdrawConfigBO withdrawConfig) {
        // API代付
        ApiWithdrawConfig config = apiWithdrawConfigService.getByIdNotDeleted(param.getApiWithdrawConfigId());
        if (config == null) {
            return RestResponse.failed(ResponseCode.API_WITHDRAW_CONFIG_NOT_FOUND);
        }
        if (!Boolean.TRUE.equals(config.getEnabled())) {
            return RestResponse.failed(ResponseCode.API_WITHDRAW_CONFIG_DISABLED);
        }
        ApiWithdrawHandler handler = ApiWithdrawHandlerFactory.getWithdrawHandler(config.getCode());
        if (handler == null) {
            return RestResponse.failed(ResponseCode.API_WITHDRAW_HANDLER_NOT_FOUND);
        }

        List<WithdrawOrderLog> logList = new ArrayList<>();
        logList.add(new WithdrawOrderLog(order.getId(), "管理员{}开始使用API代付[{}]打款", sessionUser.getUsername(), config.getName()));

        // 开始请求API代付
        try {
            // 发往三方的回调地址，我们自己的
            GlobalConfigBO globalConfig = sysConfigCache.getGlobalConfigFromRedis();
            String callbackUrl = com.im.common.util.StrUtil.format("{}{}", globalConfig.getApiCallbackUrl(), WithdrawServiceImpl.COMMON_CALLBACK_PATH);
            callbackUrl = com.im.common.util.StrUtil.format(callbackUrl, MapUtil.of("orderNum", order.getOrderNum()));

            ApiWithdrawRequestResponseVO apiRsp = handler.requestWithdraw(config, order, callbackUrl);
            if (apiRsp == null) {
                // 返回了空，这是低级错误，不应出现
                logList.add(new WithdrawOrderLog(order.getId(), "请求API代付失败，原因：{}", "处理类未正确返回响应，请检查代码"));
                withdrawOrderLogService.saveBatch(logList);
                return RestResponse.failed(ResponseCode.API_WITHDRAW_REQUEST_RETURN_FAILED, "处理类未正确返回响应，请检查代码");
            }

            if (!Boolean.TRUE.equals(apiRsp.getSuccess())) {
                logList.add(new WithdrawOrderLog(order.getId(), "请求API代付失败，原因：{}", apiRsp.getMessage()));
                withdrawOrderLogService.saveBatch(logList);
                return RestResponse.failed(ResponseCode.API_WITHDRAW_REQUEST_RETURN_FAILED, apiRsp.getMessage());
            }

            // 修改订单
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .eq(WithdrawOrder::getPayAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.PAYING)
                    .set(WithdrawOrder::getPayType, WithdrawOrderPayTypeEnum.API_PAY)
                    .set(WithdrawOrder::getPayRequestTime, LocalDateTime.now())
                    .set(WithdrawOrder::getApiWithdrawConfigId, config.getId())
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }

            // 请求成功
            logList.add(new WithdrawOrderLog(order.getId(), "请求API代付成功，等待对方系统回调或我方同步"));
            withdrawOrderLogService.saveBatch(logList);
            order = getById(param.getId());
            WithdrawOrderAdminVO vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                    bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);
            return RestResponse.ok(vo);
        } catch (Exception e) {
            LOG.error(e, "请求API代付异常，请求订单号：{}", order.getOrderNum());
            logList.add(new WithdrawOrderLog(order.getId(), "请求API代付异常，{}", ExceptionUtil.getMessage(e)));
            withdrawOrderLogService.saveBatch(logList);

            return RestResponse.failed(ResponseCode.API_WITHDRAW_REQUEST_EXCEPTION, ExceptionUtil.getMessage(e));
        }
    }

    private RestResponse<WithdrawOrderAdminVO> payDeny(AdminSessionUser sessionUser, WithdrawOrder order,
                                                       WithdrawOrderPayParam param, WithdrawConfigBO withdrawConfig) {
        // 拒绝必填备注
        if (StrUtil.isBlank(param.getRemark())) {
            return RestResponse.failed(ResponseCode.SYS_REMARK_REQUIRED);
        }

        boolean updated = lambdaUpdate()
                .eq(WithdrawOrder::getId, order.getId())
                .eq(WithdrawOrder::getStatus, order.getStatus())
                .eq(WithdrawOrder::getPayAdminId, sessionUser.getId())
                .set(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.PAY_DENY)
                .set(WithdrawOrder::getPayRequestTime, LocalDateTime.now())
                .set(WithdrawOrder::getFinishType, WithdrawOrderFinishTypeEnum.ADMIN_CONFIRMED)
                .set(WithdrawOrder::getPayFinishTime, LocalDateTime.now())
                .set(WithdrawOrder::getRemark, StrUtil.trim(param.getRemark()))
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        // 退回提单金额
        portalUserService.addBalanceWithReportDate(order.getUserId(), order.getReceiveAmount(), order.getOrderNum(),
                UserBillTypeEnum.USER_WITHDRAW_FALLBACK, StrUtil.trim(param.getRemark()), order.getReportDate(), true);

        withdrawOrderLogService.save(new WithdrawOrderLog(order.getId(), "管理员{}打款拒绝，金额已原路退回，备注：{}",
                sessionUser.getUsername(), StrUtil.blankToDefault(param.getRemark(), "")));

        order = getById(param.getId());
        WithdrawOrderAdminVO vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);
        return RestResponse.ok(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<WithdrawOrderAdminVO> payFinishForAdmin(AdminSessionUser sessionUser, IdFinishParam param) {
        WithdrawOrder order = getById(param.getId());
        if (order == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();
        WithdrawOrderAdminVO vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);

        // 是否允许到账
        if (!Boolean.TRUE.equals(vo.getAllowPayFinish())) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 日志
        List<WithdrawOrderLog> logList = new ArrayList<>();

        if (Boolean.TRUE.equals(param.getFinish())) {
            // 到账
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .eq(WithdrawOrder::getPayAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.PAY_SUCCESS)
                    .set(WithdrawOrder::getFinishType, WithdrawOrderFinishTypeEnum.ADMIN_CONFIRMED)
                    .set(WithdrawOrder::getPayFinishTime, LocalDateTime.now())
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}手动确认到账成功", sessionUser.getUsername()));

            // 报表 TODO

            // 修改用户首提数据
            portalUserService.updateFirstAndTotalWithdraw(order.getUserId(), order.getRequestAmount(), order.getCreateTime());
        } else {
            // 不到账必填备注
            if (StrUtil.isBlank(param.getRemark())) {
                return RestResponse.failed(ResponseCode.SYS_REMARK_REQUIRED);
            }
            boolean updated = lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, order.getStatus())
                    .eq(WithdrawOrder::getPayAdminId, sessionUser.getId())
                    .set(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.PAY_FAILED)
                    .set(WithdrawOrder::getFinishType, WithdrawOrderFinishTypeEnum.ADMIN_CONFIRMED)
                    .set(WithdrawOrder::getPayFinishTime, LocalDateTime.now())
                    .set(WithdrawOrder::getRemark, StrUtil.trim(param.getRemark()))
                    .update();
            if (!updated) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
            logList.add(new WithdrawOrderLog(order.getId(), "管理员{}手动确认到账失败，金额已原路退回，备注：{}",
                    sessionUser.getUsername(), StrUtil.blankToDefault(param.getRemark(), "")));

            // 退回提单金额
            portalUserService.addBalanceWithReportDate(order.getUserId(), order.getRequestAmount(), order.getOrderNum(),
                    UserBillTypeEnum.USER_WITHDRAW_FALLBACK, StrUtil.trim(param.getRemark()), order.getReportDate(), true);
        }

        if (CollectionUtil.isNotEmpty(logList)) {
            withdrawOrderLogService.saveBatch(logList);
        }

        order = getById(param.getId());
        vo = new WithdrawOrderAdminVO(order, bankCardWithdrawConfigCache,
                bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig);
        return RestResponse.ok(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public WithdrawOrder getLastOrder(String username) {
        Long userId = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
        if (userId == null) {
            return null;
        }

        List<WithdrawOrder> list = lambdaQuery()
                .eq(WithdrawOrder::getUserId, userId)
                .eq(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.PAY_SUCCESS)
                .orderByDesc(WithdrawOrder::getId)
                .last("limit 1")
                .list();

        return CollectionUtil.isEmpty(list) ? null : list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse adminAddForAdmin(AdminSessionUser sessionUser, WithdrawOrderAdminAddParam param, String requestIp) {
        PortalUser user = portalUserService.getByUsername(param.getUsername());
        if (user == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND, param.getUsername());
        }

        // 检查余额
        if (NumberUtil.isLess(user.getBalance(), param.getAmount())) {
            return RestResponse.failed(ResponseCode.USER_INSUFFICIENT_BALANCE);
        }

        // 日志
        List<WithdrawOrderLog> logList = new ArrayList<>();
        // 人工提现记账日就记在当时
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        LocalDateTime now = LocalDateTime.now();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());

        // 保存记录
        WithdrawOrder order = new WithdrawOrder();
        order.setOrderNum(OrderUtil.withdrawOrderNumber());
        order.setUserId(user.getId());
        order.setRequestAmount(param.getAmount());
        order.setReceiveAmount(param.getAmount());
        order.setServiceChargePercent(BigDecimal.ZERO);
        order.setServiceCharge(BigDecimal.ZERO);
        order.setStatus(WithdrawOrderStatusEnum.PAY_SUCCESS);
        order.setType(WithdrawOrderTypeEnum.ADMIN_ADD);
        order.setApproveAdminId(sessionUser.getId());
        order.setApproveTime(now);
        order.setPayAdminId(sessionUser.getId());
        order.setPayRequestTime(now);
        order.setPayFinishTime(now);
        order.setRequestIp(requestIp);
        order.setReportDate(reportDate);
        order.setRemark(StrUtil.trim(param.getRemark()));
        order.setCreateTime(now);
        order.setUpdateTime(now);

        // 先扣余额
        RestResponse balanceRsp = portalUserService.addBalanceWithReportDate(order.getUserId(), param.getAmount().negate(),
                order.getOrderNum(), UserBillTypeEnum.ADMIN_WITHDRAW, order.getRemark(), order.getReportDate(), false);
        if (!balanceRsp.isOkRsp()) {
            return balanceRsp;
        }

        // 再保存订单
        save(order);

        logList.add(new WithdrawOrderLog(order.getId(), "管理员{}人工提现, 备注: {}", sessionUser.getUsername(),
                StrUtil.blankToDefault(order.getRemark(), "")));

        // 保存日志
        if (CollectionUtil.isNotEmpty(logList)) {
            withdrawOrderLogService.saveBatch(logList);
        }

        // TODO 报表

        // 修改用户首提数据
        portalUserService.updateFirstAndTotalWithdraw(order.getUserId(), order.getRequestAmount(), order.getCreateTime());

        return RestResponse.OK;
    }
}
