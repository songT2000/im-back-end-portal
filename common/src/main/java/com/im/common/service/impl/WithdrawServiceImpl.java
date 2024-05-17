package com.im.common.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.im.common.cache.impl.*;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.cache.sysconfig.bo.WithdrawConfigBO;
import com.im.common.entity.*;
import com.im.common.entity.enums.*;
import com.im.common.param.WithdrawRequestPortalParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.*;
import com.im.common.util.*;
import com.im.common.util.api.pay.base.withdraw.*;
import com.im.common.vo.PortalSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 提现 服务实现类
 *
 * @author Barry
 * @date 2021-09-29
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {
    /**
     * API代付回调地址
     */
    public static final String COMMON_CALLBACK_PATH = "/api/portal/withdraw/common-callback/{orderNum}";

    private static final Log LOG = LogFactory.get();

    private SysConfigCache sysConfigCache;
    private PortalUserService portalUserService;
    private BankCardWithdrawConfigCache bankCardWithdrawConfigCache;
    private ApiWithdrawConfigCache apiWithdrawConfigCache;
    private BankCardWithdrawConfigService bankCardWithdrawConfigService;
    private ApiWithdrawConfigService apiWithdrawConfigService;
    private UserBankCardService userBankCardService;
    private WithdrawOrderService withdrawOrderService;
    private WithdrawOrderLogService withdrawOrderLogService;
    private BankCache bankCache;
    private PortalSystemMessageService portalSystemMessageService;
    private UserBankCardBlackCache userBankCardBlackCache;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setBankCardWithdrawConfigCache(BankCardWithdrawConfigCache bankCardWithdrawConfigCache) {
        this.bankCardWithdrawConfigCache = bankCardWithdrawConfigCache;
    }

    @Autowired
    public void setApiWithdrawConfigCache(ApiWithdrawConfigCache apiWithdrawConfigCache) {
        this.apiWithdrawConfigCache = apiWithdrawConfigCache;
    }

    @Autowired
    public void setBankCardWithdrawConfigService(BankCardWithdrawConfigService bankCardWithdrawConfigService) {
        this.bankCardWithdrawConfigService = bankCardWithdrawConfigService;
    }

    @Autowired
    public void setApiWithdrawConfigService(ApiWithdrawConfigService apiWithdrawConfigService) {
        this.apiWithdrawConfigService = apiWithdrawConfigService;
    }

    @Autowired
    public void setUserBankCardService(UserBankCardService userBankCardService) {
        this.userBankCardService = userBankCardService;
    }

    @Autowired
    public void setWithdrawOrderLogService(WithdrawOrderLogService withdrawOrderLogService) {
        this.withdrawOrderLogService = withdrawOrderLogService;
    }

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @Autowired
    public void setPortalSystemMessageService(PortalSystemMessageService portalSystemMessageService) {
        this.portalSystemMessageService = portalSystemMessageService;
    }

    @Autowired
    public void setUserBankCardBlackCache(UserBankCardBlackCache userBankCardBlackCache) {
        this.userBankCardBlackCache = userBankCardBlackCache;
    }

    @Autowired
    public void setWithdrawOrderService(WithdrawOrderService withdrawOrderService) {
        this.withdrawOrderService = withdrawOrderService;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<WithdrawConfigGroupPortalVO> listConfigForPortal(PortalSessionUser sessionUser) {
        PortalUser user = portalUserService.getById(sessionUser.getId());
        if (!Boolean.TRUE.equals(user.getWithdrawEnabled())) {
            // 没有提现权限，返回空的
            return new ArrayList<>();
        }

        // 所有配置
        List<WithdrawConfigGroupConfigPortalVO> configList = new ArrayList<>();

        // 列出可用的银行卡提现配置
        {
            List<BankCardWithdrawConfig> list = bankCardWithdrawConfigCache.listEnabledFromRedisForPortal();
            if (CollectionUtil.isNotEmpty(list)) {
                configList.addAll(CollectionUtil.toList(
                        list,
                        e -> new WithdrawConfigGroupConfigPortalVO(e)));
            }
        }

        // todo 分组并排序，银行卡/虚拟币，后续再支持自定义排序
        List<WithdrawConfigGroupPortalVO> sortList = new ArrayList<>();
        WithdrawConfigGroupEnum[] groups = WithdrawConfigGroupEnum.values();
        for (WithdrawConfigGroupEnum group : groups) {
            List<WithdrawConfigGroupConfigPortalVO> groupConfigList = CollectionUtil.filterList(configList, e -> e.getGroup() == group);

            if (CollectionUtil.isNotEmpty(groupConfigList)) {
                WithdrawConfigGroupPortalVO groupVO = new WithdrawConfigGroupPortalVO();
                groupVO.setGroup(group);
                groupVO.setList(groupConfigList);
                sortList.add(groupVO);
            }
        }

        return sortList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse requestForPortal(PortalSessionUser sessionUser, WithdrawRequestPortalParam param, String requestIp) {
        PortalUser user = portalUserService.getById(sessionUser.getId());

        // 检查参数及状态等
        {
            RestResponse checkRsp = checkRequestForPortalRequired(user, param);
            if (!checkRsp.isOkRsp()) {
                return checkRsp;
            }
        }

        if (param.getConfigSource() == WithdrawConfigSourceEnum.BANK_CARD_WITHDRAW_CONFIG) {
            // 银行卡充值（公司入款）
            return bankCardWithdrawRequestForPortal(user, param, requestIp);
        }

        return RestResponse.failed(ResponseCode.SYS_METHOD_UNDER_CONSTRUCTION);
    }

    /**
     * 银行卡提现
     *
     * @param user
     * @param param
     * @param requestIp
     * @return
     */
    private RestResponse bankCardWithdrawRequestForPortal(PortalUser user, WithdrawRequestPortalParam param, String requestIp) {
        if (param.getBankCardId() == null) {
            return RestResponse.failed(ResponseCode.USER_BANK_CARD_REQUIRED);
        }

        // 配置状态
        BankCardWithdrawConfig config = bankCardWithdrawConfigService.lambdaQuery().eq(BankCardWithdrawConfig::getId, param.getConfigId()).one();
        if (config == null || !bankCardWithdrawConfigCache.isAvailableConfig(config)) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        // 检查金额是否在允许范围内
        RestResponse amountRsp = NumberUtil.isBigDecimalInRangeStr(config.getAmountRange(), config.getAmountMaxPrecision(), param.getAmount());
        if (!amountRsp.isOkRsp()) {
            return amountRsp;
        }

        // 检查钱包
        if (user == null || NumberUtil.isLess(user.getBalance(), param.getAmount())) {
            return RestResponse.failed(ResponseCode.USER_INSUFFICIENT_BALANCE);
        }
        if (!Boolean.TRUE.equals(user.getWithdrawEnabled())) {
            return RestResponse.failed(ResponseCode.USER_WITHDRAW_DISABLED);
        }

        // 检查银行卡，以及是否是黑名单
        UserBankCard bankCard = userBankCardService.getByUserIdAndCardId(user.getId(), param.getBankCardId());
        if (bankCard == null) {
            return RestResponse.failed(ResponseCode.USER_BANK_CARD_NOT_FOUND);
        }
        if (!Boolean.TRUE.equals(bankCard.getEnabled())) {
            return RestResponse.failed(ResponseCode.USER_BANK_CARD_DISABLED);
        }
        // 检查提现银行配置
        Bank bank = bankCache.getByIdFromRedis(bankCard.getBankId());
        if (bank == null) {
            return RestResponse.failed(ResponseCode.BANK_NOT_FOUND);
        }
        if (!Boolean.TRUE.equals(bank.getWithdrawEnabled())) {
            return RestResponse.failed(ResponseCode.BANK_WITHDRAW_DISABLED);
        }
        RestResponse cardNumBlackRsp = userBankCardBlackCache.checkUserBankCardBlack(bankCard.getCardNum());
        if (!cardNumBlackRsp.isOkRsp()) {
            return cardNumBlackRsp;
        }

        // 扣钱，直接扣提单金额即可
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        String orderNum = OrderUtil.withdrawOrderNumber();
        LocalDateTime now = LocalDateTime.now();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());

        RestResponse balanceRsp = portalUserService.addBalanceWithReportDate(user.getId(), param.getAmount().negate(), orderNum,
                UserBillTypeEnum.USER_WITHDRAW, null, reportDate, false);
        if (!balanceRsp.isOkRsp()) {
            return balanceRsp;
        }

        // 日志
        List<WithdrawOrderLog> logList = new ArrayList<>();

        // 服务费，到账金额
        BigDecimal serviceCharge = NumberUtil.mul(param.getAmount(), config.getServiceChargePercent());
        BigDecimal receiveAmount = NumberUtil.sub(param.getAmount(), serviceCharge);

        // 保存记录
        WithdrawOrder order = new WithdrawOrder();
        order.setOrderNum(orderNum);
        order.setUserId(user.getId());
        order.setRequestAmount(param.getAmount());
        order.setReceiveAmount(receiveAmount);
        order.setServiceChargePercent(config.getServiceChargePercent());
        order.setServiceCharge(serviceCharge);
        order.setStatus(WithdrawOrderStatusEnum.WAITING);
        order.setType(WithdrawOrderTypeEnum.USER_REQUEST);
        order.setRequestWithdrawConfigSource(param.getConfigSource());
        order.setRequestWithdrawConfigId(config.getId());
        order.setUserBankCardName(user.getWithdrawName());
        order.setUserBankId(bankCard.getBankId());
        order.setUserBankCardNum(bankCard.getCardNum());
        order.setUserBankCardBranch(bankCard.getBranch());
        order.setRequestIp(requestIp);
        order.setReportDate(reportDate);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        boolean saved = withdrawOrderService.save(order);
        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        logList.add(new WithdrawOrderLog(order.getId(), "用户从IP[{}]申请提现，等待处理",
                order.getRequestIp()));

        // 保存日志
        if (CollectionUtil.isNotEmpty(logList)) {
            withdrawOrderLogService.saveBatch(logList);
        }

        //发送系统消息
        portalSystemMessageService.sendWithdrawMessage();

        return RestResponse.OK;
    }

    private RestResponse checkRequestForPortalRequired(PortalUser user, WithdrawRequestPortalParam param) {
        // 权限
        if (!Boolean.TRUE.equals(user.getWithdrawEnabled())) {
            return RestResponse.failed(ResponseCode.SYS_NO_PERMISSION);
        }

        // 检查金额
        if (!NumberUtil.isGreatThenZero(param.getAmount())) {
            return RestResponse.failed(ResponseCode.SYS_AMOUNT_INCORRECT);
        }

        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();
        // 服务时间
        if (!withdrawConfig.isDuringEnableTime()) {
            return RestResponse.failed(ResponseCode.WITHDRAW_NOT_IN_ENABLE_TIME, withdrawConfig.getEnableTime());
        }

        // 银行卡提现必须要绑定提现姓名
        if (param.getConfigSource() == WithdrawConfigSourceEnum.BANK_CARD_WITHDRAW_CONFIG) {
            if (StrUtil.isBlank(user.getWithdrawName())) {
                return RestResponse.failed(ResponseCode.USER_WITHDRAW_NAME_NOT_YET_BIND);
            }
        }

        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();

        // 每日最多提交笔数，处理中和成功，都算
        if (NumberUtil.isGreatThenZero(withdrawConfig.getMaxDailyRequest())) {
            LocalDateTime todayStartTime = DateTimeUtil.getOffsetStartDateTime(0, reportConfig.getOffsetTime());
            LocalDateTime todayEndTime = DateTimeUtil.getOffsetEndDateTime(0, reportConfig.getOffsetTime());

            // 提现笔数
            int todayRequestCount = withdrawOrderService
                    .lambdaQuery()
                    .eq(WithdrawOrder::getUserId, user.getId())
                    .ge(WithdrawOrder::getCreateTime, todayStartTime)
                    .le(WithdrawOrder::getCreateTime, todayEndTime)
                    .in(WithdrawOrder::getStatus,
                            CollectionUtil.toList(WithdrawOrderStatusEnum.WAITING, WithdrawOrderStatusEnum.APPROVE_SUCCESS,
                                    WithdrawOrderStatusEnum.PAYING, WithdrawOrderStatusEnum.PAY_SUCCESS))
                    .count();


            // 累计提现笔数
            if (NumberUtil.isGreaterOrEqual(todayRequestCount, withdrawConfig.getMaxDailyRequest())) {
                return RestResponse.failed(ResponseCode.WITHDRAW_MAX_DAILY_REQUEST_EXCEEDED, withdrawConfig.getMaxDailyRequest());
            }
        }

        // 同时最大提现笔数，处理中，都算
        if (NumberUtil.isGreatThenZero(withdrawConfig.getMaxSameTimeRequest())) {
            // 避免数据过大，只查最近15天
            final int queryPastDay = -15;

            // 提现笔数
            int sameTimeCount = withdrawOrderService
                    .lambdaQuery()
                    .eq(WithdrawOrder::getUserId, user.getId())
                    .ge(WithdrawOrder::getCreateTime, DateTimeUtil.getOffsetStartDateTime(queryPastDay, reportConfig.getOffsetTime()))
                    .in(WithdrawOrder::getStatus,
                            CollectionUtil.toList(WithdrawOrderStatusEnum.WAITING, WithdrawOrderStatusEnum.APPROVE_SUCCESS,
                                    WithdrawOrderStatusEnum.PAYING))
                    .count();
            if (NumberUtil.isGreaterOrEqual(sameTimeCount, withdrawConfig.getMaxSameTimeRequest())) {
                return RestResponse.failed(ResponseCode.WITHDRAW_MAX_SAME_TIME_REQUEST_EXCEEDED, withdrawConfig.getMaxSameTimeRequest());
            }
        }

        // 检查资金密码
        // 必须先绑定资金密码
        if (StrUtil.isBlank(user.getFundPwd())) {
            return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_NOT_YET_BIND);
        }
        if (!PasswordUtil.validatePwd(user.getFundPwd(), param.getFundPwd())) {
            return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_INCORRECT);
        }

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<String> callbackByApi(Map<String, String> requestParams, String orderNum, String requestIp) {
        // 查找订单
        WithdrawOrder order = withdrawOrderService.lambdaQuery().eq(WithdrawOrder::getOrderNum, orderNum).one();
        if (order == null) {
            LOG.error("收到API代付回调但处理失败，订单号{}没有找到，内容：{}，回调IP：{}", orderNum, JSON.toJSONString(requestParams), requestIp);
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        if (order.getPayType() != WithdrawOrderPayTypeEnum.API_PAY) {
            LOG.error("收到API代付回调但处理失败，订单号{}不属于三方订单，内容：{}，回调IP：{}", orderNum, JSON.toJSONString(requestParams), requestIp);
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        // 保存回调日志
        List<WithdrawOrderLog> logList = new ArrayList<>();
        logList.add(new WithdrawOrderLog(order.getId(), "收到API代付回调，内容：{}，回调IP：{}，正在处理",
                JSON.toJSONString(requestParams), requestIp));

        // 检查IP白名单
        if (!apiWithdrawConfigCache.isWhitelistCallbackIp(order.getApiWithdrawConfigId(), requestIp)) {
            logList.add(new WithdrawOrderLog(order.getId(), "回调处理失败，对方IP[{}]不在白名单中：{}",
                    requestIp, apiWithdrawConfigCache.getWhitelistCallbackIp(order.getApiWithdrawConfigId())));
            withdrawOrderLogService.saveBatch(logList);
            return RestResponse.failed(ResponseCode.IP_NOT_ALLOWED, requestIp);
        }

        ApiWithdrawConfig config = apiWithdrawConfigCache.getByIdFromRedis(order.getApiWithdrawConfigId());
        if (config == null || Boolean.TRUE.equals(config.getDeleted())) {
            return RestResponse.failed(ResponseCode.API_WITHDRAW_CONFIG_NOT_FOUND);
        }

        ApiWithdrawHandler handler = ApiWithdrawHandlerFactory.getWithdrawHandler(config.getCode());
        if (handler == null) {
            logList.add(new WithdrawOrderLog(order.getId(), "回调处理失败, 代付处理类未找到: {}，请检查代码", config.getCode().getVal()));
            withdrawOrderLogService.saveBatch(logList);
            return RestResponse.failed(ResponseCode.API_WITHDRAW_HANDLER_NOT_FOUND);
        }

        // 已完成，响应出去
        if (order.getStatus() == WithdrawOrderStatusEnum.PAY_SUCCESS) {
            String output = handler.getWithdrawSuccessOutput(config, order, requestParams);

            logList.add(new WithdrawOrderLog(order.getId(), "回调处理失败, 订单已是完成状态(重复回调?), 本次不处理订单并直接向三方返回成功: {}", output));
            withdrawOrderLogService.saveBatch(logList);

            return RestResponse.ok(output);
        }
        if (order.getStatus() != WithdrawOrderStatusEnum.PAYING) {

            logList.add(new WithdrawOrderLog(order.getId(), "回调处理失败, 订单状态不是打款中状态"));
            withdrawOrderLogService.saveBatch(logList);

            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        ApiWithdrawVerifyResult verifyResult;
        try {
            verifyResult = handler.verifyWithdrawCallback(config, order, requestParams);
            if (verifyResult == null) {
                logList.add(new WithdrawOrderLog(order.getId(), "回调处理失败，处理类返回了空，请检查代码"));
                withdrawOrderLogService.saveBatch(logList);

                return RestResponse.failed(ResponseCode.API_WITHDRAW_CALLBACK_PROCESS_FAILED);
            }
        } catch (Exception e) {
            logList.add(new WithdrawOrderLog(order.getId(), "回调处理失败，发生异常: {}", ExceptionUtil.getMessage(e)));
            withdrawOrderLogService.saveBatch(logList);
            return RestResponse.failed(ResponseCode.API_WITHDRAW_CALLBACK_PROCESS_FAILED);
        }

        // 三方回调的状态是处理中，没有用
        if (verifyResult.getStatus() == WithdrawOrderStatusEnum.PAYING) {
            logList.add(new WithdrawOrderLog(order.getId(), "回调处理失败，对方状态仍是处理中"));
            withdrawOrderLogService.saveBatch(logList);

            return RestResponse.failed(ResponseCode.API_WITHDRAW_CALLBACK_PROCESS_FAILED);
        }

        // 处理结果
        RestResponse rsp = processVerityResult(config, order, verifyResult, WithdrawOrderFinishTypeEnum.API_CALLBACK, requestIp, logList);
        if (!rsp.isOkRsp()) {
            logList.add(new WithdrawOrderLog(order.getId(), "回调处理失败，我方状态码：{}", rsp.getCode()));
            withdrawOrderLogService.saveBatch(logList);

            return rsp;
        }

        logList.add(new WithdrawOrderLog(order.getId(), "回调处理成功，向三方返回成功：{}", verifyResult.getOutput()));
        withdrawOrderLogService.saveBatch(logList);

        // 输出
        return RestResponse.ok(verifyResult.getOutput());
    }

    private RestResponse processVerityResult(ApiWithdrawConfig config,
                                             WithdrawOrder order,
                                             ApiWithdrawVerifyResult verifyRsp,
                                             WithdrawOrderFinishTypeEnum finishType,
                                             String callbackIp,
                                             List<WithdrawOrderLog> logList) {
        if (verifyRsp.getStatus() == WithdrawOrderStatusEnum.PAY_SUCCESS) {
            order.setStatus(verifyRsp.getStatus());
            order.setFinishType(finishType);
            order.setPayFinishTime(LocalDateTime.now());

            // 修改订单
            boolean updated = withdrawOrderService
                    .lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.PAYING)
                    .set(WithdrawOrder::getStatus, order.getStatus())
                    .set(WithdrawOrder::getFinishType, order.getFinishType())
                    .set(WithdrawOrder::getPayFinishTime, order.getPayFinishTime())
                    .set(StrUtil.isNotBlank(callbackIp),
                            WithdrawOrder::getApiWithdrawCallbackIp, callbackIp)
                    .update();
            if (!updated) {
                return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
            }

            // 报表 TODO

            // 修改用户首提数据
            portalUserService.updateFirstAndTotalWithdraw(order.getUserId(), order.getRequestAmount(), order.getCreateTime());

            logList.add(new WithdrawOrderLog(order.getId(), "对方状态为成功，处理成功"));

            return RestResponse.OK;
        } else if (verifyRsp.getStatus() == WithdrawOrderStatusEnum.PAY_FAILED) {
            // 修改订单
            boolean updated = withdrawOrderService
                    .lambdaUpdate()
                    .eq(WithdrawOrder::getId, order.getId())
                    .eq(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.PAYING)
                    .set(WithdrawOrder::getStatus, verifyRsp.getStatus())
                    .set(WithdrawOrder::getFinishType, finishType)
                    .set(WithdrawOrder::getPayFinishTime, LocalDateTime.now())
                    .set(StrUtil.isNotBlank(callbackIp),
                            WithdrawOrder::getApiWithdrawCallbackIp, callbackIp)
                    .update();
            if (!updated) {
                return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
            }

            logList.add(new WithdrawOrderLog(order.getId(), "对方状态为失败，订单失败，原因：{}", verifyRsp.getMessage()));

            return RestResponse.OK;
        } else if (verifyRsp.getStatus() == WithdrawOrderStatusEnum.WAITING) {
            logList.add(new WithdrawOrderLog(order.getId(), "对方状态为处理中"));

            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        return RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void syncStatusFromApi(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<WithdrawOrder> list = withdrawOrderService
                .lambdaQuery()
                .eq(WithdrawOrder::getStatus, WithdrawOrderStatusEnum.PAYING)
                .eq(WithdrawOrder::getPayType, WithdrawOrderPayTypeEnum.API_PAY)
                .ge(WithdrawOrder::getPayRequestTime, startDateTime)
                .le(WithdrawOrder::getPayRequestTime, endDateTime)
                .list();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        LOG.info("正在处理{}笔API代付订单状态同步", list.size());
        syncStatusFromThird(list);
        LOG.info("处理{}笔API代付订单状态同步完成", list.size());
    }

    private void syncStatusFromThird(List<WithdrawOrder> list) {
        // 分组
        Map<Long, List<WithdrawOrder>> configOrderMap = CollectionUtil.toMapList(list, e -> e.getApiWithdrawConfigId());

        List<WithdrawOrderLog> logList = new ArrayList<>();

        configOrderMap.forEach((configId, configOrderList) -> {
            ApiWithdrawConfig config = apiWithdrawConfigCache.getByIdFromRedis(configId);
            if (config != null && !Boolean.TRUE.equals(config.getDeleted())) {
                ApiWithdrawHandler handler = ApiWithdrawHandlerFactory.getWithdrawHandler(config.getCode());
                if (handler != null) {
                    // 记录日志
                    logList.addAll(CollectionUtil.toList(configOrderList, e -> new WithdrawOrderLog(e.getId(), "系统开始向三方同步订单状态，本批次共处理{}条订单", configOrderList.size())));

                    try {
                        syncStatusFromThirdByOrder(configOrderList, config, handler, logList);
                    } catch (Exception ex) {
                        logList.addAll(CollectionUtil.toList(configOrderList, e -> new WithdrawOrderLog(e.getId(), "同步订单状态时发生异常，{}", ExceptionUtil.getMessage(ex))));
                        LOG.error(ex, "同步{}代付订单异常", config.getName());
                    }
                }
            }
        });

        if (CollectionUtil.isNotEmpty(logList)) {
            withdrawOrderLogService.saveBatch(logList);
        }
    }

    private void syncStatusFromThirdByOrder(List<WithdrawOrder> configOrderList,
                                            ApiWithdrawConfig config,
                                            ApiWithdrawHandler handler,
                                            List<WithdrawOrderLog> logList) throws Exception {


        List<ApiWithdrawVerifyResult> resultList = handler.queryWithdrawFromThird(config, configOrderList);
        if (CollectionUtil.isEmpty(resultList)) {
            return;
        }
        for (ApiWithdrawVerifyResult verifyResult : resultList) {
            WithdrawOrder order = CollectionUtil.findFirst(configOrderList, e -> e.getId().equals(verifyResult.getOrderId()));
            processVerityResult(config, order, verifyResult, WithdrawOrderFinishTypeEnum.AUTOMATIC_SYNC, null, logList);
        }
    }
}
