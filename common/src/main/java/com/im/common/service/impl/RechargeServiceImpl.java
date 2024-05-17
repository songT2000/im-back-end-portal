package com.im.common.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.im.common.cache.impl.*;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.cache.sysconfig.bo.RechargeConfigBO;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.entity.*;
import com.im.common.entity.enums.*;
import com.im.common.param.RechargeRequestPortalParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.*;
import com.im.common.util.*;
import com.im.common.util.api.pay.base.recharge.*;
import com.im.common.vo.PortalSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 充值 服务实现类
 *
 * @author Barry
 * @date 2021-09-29
 */
@Service
public class RechargeServiceImpl implements RechargeService {
    /**
     * 三方充值回调地址
     */
    public static final String COMMON_CALLBACK_PATH = "/api/portal/recharge/common-callback/{orderNum}";

    private static final Log LOG = LogFactory.get();

    private SysConfigCache sysConfigCache;
    private PortalUserService portalUserService;
    private BankCardRechargeConfigCache bankCardRechargeConfigCache;
    private ApiRechargeConfigCache apiRechargeConfigCache;
    private UserBankCardService userBankCardService;
    private RechargeOrderService rechargeOrderService;
    private UserBillService userBillService;
    private RechargeOrderLogService rechargeOrderLogService;
    private BankCardRechargeConfigService bankCardRechargeConfigService;
    private BankCardRechargeConfigCardService bankCardRechargeConfigCardService;
    private ApiRechargeConfigService apiRechargeConfigService;
    private BankCache bankCache;
    private UserGroupUserCache userGroupUserCache;
    private UserGroupBankCardRechargeConfigCache userGroupBankCardRechargeConfigCache;
    private UserGroupApiRechargeConfigCache userGroupApiRechargeConfigCache;
    private PortalSystemMessageService portalSystemMessageService;

    @Autowired
    public void setRechargeOrderService(RechargeOrderService rechargeOrderService) {
        this.rechargeOrderService = rechargeOrderService;
    }

    @Autowired
    public void setUserBillService(UserBillService userBillService) {
        this.userBillService = userBillService;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setBankCardRechargeConfigCache(BankCardRechargeConfigCache bankCardRechargeConfigCache) {
        this.bankCardRechargeConfigCache = bankCardRechargeConfigCache;
    }

    @Autowired
    public void setApiRechargeConfigCache(ApiRechargeConfigCache apiRechargeConfigCache) {
        this.apiRechargeConfigCache = apiRechargeConfigCache;
    }

    @Autowired
    public void setUserBankCardService(UserBankCardService userBankCardService) {
        this.userBankCardService = userBankCardService;
    }

    @Autowired
    public void setRechargeOrderLogService(RechargeOrderLogService rechargeOrderLogService) {
        this.rechargeOrderLogService = rechargeOrderLogService;
    }

    @Autowired
    public void setBankCardRechargeConfigService(BankCardRechargeConfigService bankCardRechargeConfigService) {
        this.bankCardRechargeConfigService = bankCardRechargeConfigService;
    }

    @Autowired
    public void setBankCardRechargeConfigCardService(BankCardRechargeConfigCardService bankCardRechargeConfigCardService) {
        this.bankCardRechargeConfigCardService = bankCardRechargeConfigCardService;
    }

    @Autowired
    public void setApiRechargeConfigService(ApiRechargeConfigService apiRechargeConfigService) {
        this.apiRechargeConfigService = apiRechargeConfigService;
    }

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @Autowired
    public void setUserGroupUserCache(UserGroupUserCache userGroupUserCache) {
        this.userGroupUserCache = userGroupUserCache;
    }

    @Autowired
    public void setUserGroupBankCardRechargeConfigCache(UserGroupBankCardRechargeConfigCache userGroupBankCardRechargeConfigCache) {
        this.userGroupBankCardRechargeConfigCache = userGroupBankCardRechargeConfigCache;
    }

    @Autowired
    public void setUserGroupApiRechargeConfigCache(UserGroupApiRechargeConfigCache userGroupApiRechargeConfigCache) {
        this.userGroupApiRechargeConfigCache = userGroupApiRechargeConfigCache;
    }

    @Autowired
    public void setPortalSystemMessageService(PortalSystemMessageService portalSystemMessageService) {
        this.portalSystemMessageService = portalSystemMessageService;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<RechargeConfigGroupPortalVO> listConfigForPortal(PortalSessionUser sessionUser) {
        PortalUser user = portalUserService.getById(sessionUser.getId());
        if (!Boolean.TRUE.equals(user.getRechargeEnabled())) {
            // 没有充值权限，返回空的
            return new ArrayList<>();
        }

        // 所有配置
        List<RechargeConfigGroupConfigPortalVO> configList = new ArrayList<>();

        // 列出可用的银行卡充值配置
        {
            List<BankCardRechargeConfig> list = bankCardRechargeConfigCache.listEnabledFromRedisForPortal(user.getId(), userGroupUserCache, userGroupBankCardRechargeConfigCache);
            if (CollectionUtil.isNotEmpty(list)) {
                configList.addAll(CollectionUtil.toList(
                        list, e -> new RechargeConfigGroupConfigPortalVO(e)));
            }
        }

        // 列出可用的三方充值配置
        {
            List<ApiRechargeConfig> list = apiRechargeConfigCache.listEnabledFromRedisForPortal(user.getId(), userGroupUserCache, userGroupApiRechargeConfigCache);
            if (CollectionUtil.isNotEmpty(list)) {
                configList.addAll(CollectionUtil.toList(
                        list, e -> new RechargeConfigGroupConfigPortalVO(e)));
            }
        }

        // todo 分组并排序，银行卡转账/支付宝/微信/虚拟币，后续再支持自定义排序
        List<RechargeConfigGroupPortalVO> sortList = new ArrayList<>();
        RechargeConfigGroupEnum[] groups = RechargeConfigGroupEnum.values();
        for (RechargeConfigGroupEnum group : groups) {
            List<RechargeConfigGroupConfigPortalVO> groupConfigList = CollectionUtil.filterList(configList, e -> e.getGroup() == group);

            if (CollectionUtil.isNotEmpty(groupConfigList)) {
                RechargeConfigGroupPortalVO groupVO = new RechargeConfigGroupPortalVO();
                groupVO.setGroup(group);
                groupVO.setList(groupConfigList);
                sortList.add(groupVO);
            }
        }

        return sortList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<RechargeRequestResponseVO> requestForPortal(PortalSessionUser sessionUser, RechargeRequestPortalParam param, String requestIp) {
        PortalUser user = portalUserService.getById(sessionUser.getId());

        // 检查参数及状态等
        {
            RestResponse checkRsp = checkRequestForPortalRequired(user, param);
            if (!checkRsp.isOkRsp()) {
                return checkRsp;
            }
        }

        if (param.getConfigSource() == RechargeConfigSourceEnum.BANK_CARD_RECHARGE_CONFIG) {
            // 银行卡充值（公司入款）
            return bankCardRechargeRequestForPortal(user, param, requestIp);
        } else if (param.getConfigSource() == RechargeConfigSourceEnum.API_RECHARGE_CONFIG) {
            // 三方充值
            return apiRechargeRequestForPortal(user, param, requestIp);
        }

        return RestResponse.failed(ResponseCode.SYS_METHOD_UNDER_CONSTRUCTION);
    }

    /**
     * 银行卡充值（公司入款）
     *
     * @param user
     * @param param
     * @param requestIp
     * @return
     */
    private RestResponse<RechargeRequestResponseVO> bankCardRechargeRequestForPortal(PortalUser user, RechargeRequestPortalParam param, String requestIp) {
        RestResponse<BankCardRechargeConfig> configEnabledRsp = bankCardRechargeConfigCache.getEnabledFromRedisForPortal(param.getConfigId(), user.getId(), userGroupUserCache, userGroupBankCardRechargeConfigCache);
        if (!configEnabledRsp.isOkRsp()) {
            return RestResponse.buildClearData(configEnabledRsp);
        }

        // 配置状态
        BankCardRechargeConfig config = bankCardRechargeConfigService
                .lambdaQuery()
                .eq(BankCardRechargeConfig::getId, param.getConfigId())
                .eq(BankCardRechargeConfig::getDeleted, false)
                .one();
        if (config == null || !bankCardRechargeConfigCache.isAvailableConfig(config)) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 检查付款人
        if (Boolean.TRUE.equals(config.getNeedInputUserCardName()) && StrUtil.isBlank(param.getUserCardName())) {
            return RestResponse.failed(ResponseCode.RECHARGE_USER_CARD_NAME_REQUIRED);
        }

        // todo 付款人黑白名单

        // 用户充值权限
        if (user != null && !Boolean.TRUE.equals(user.getRechargeEnabled())) {
            return RestResponse.failed(ResponseCode.USER_RECHARGE_DISABLED);
        }

        // 检查金额是否在允许范围内
        RestResponse amountRsp = NumberUtil.isBigDecimalInRangeStr(config.getAmountRange(), config.getAmountMaxPrecision(), param.getAmount());
        if (!amountRsp.isOkRsp()) {
            return amountRsp;
        }

        // 日志
        long orderId = IdWorker.getId();
        List<RechargeOrderLog> logList = new ArrayList<>();
        if (Boolean.TRUE.equals(config.getNeedInputUserCardName())) {
            logList.add(new RechargeOrderLog(orderId, "用户从IP[{}]申请充值并输入付款人[{}], 正在查找配置下的银行卡列表",
                    requestIp, StrUtil.trim(param.getUserCardName())));
        } else {
            logList.add(new RechargeOrderLog(orderId, "用户从IP[{}]申请充值，正在查找配置下的银行卡列表",
                    requestIp));
        }

        // 找配置下的卡，并随机选一张
        List<BankCardRechargeConfigCard> cardList = bankCardRechargeConfigCardService.listEnabledByConfigId(config.getId());
        if (CollectionUtil.isEmpty(cardList)) {
            return RestResponse.failed(ResponseCode.RECHARGE_NO_CARD_FOUND);
        }
        // 随机选卡
        BankCardRechargeConfigCard card = CollectionUtil.randomSelect(cardList);
        logList.add(new RechargeOrderLog(orderId, "随机选到：{}/{}, 等待确认", card.getCardName(), card.getCardNum()));

        String orderNum = OrderUtil.rechargeOrderNumber();
        BigDecimal payAmount = param.getAmount();
        BigDecimal serviceCharge = NumberUtil.mul(payAmount, config.getServiceChargePercent());
        BigDecimal receiveAmount = NumberUtil.sub(payAmount, serviceCharge);

        // 以创建时间作为报表数据
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        LocalDateTime now = LocalDateTime.now();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());

        // 保存记录
        RechargeOrder order = new RechargeOrder();
        order.setId(orderId);
        order.setOrderNum(orderNum);
        order.setUserId(user.getId());
        order.setRequestAmount(param.getAmount());
        order.setPayAmount(param.getAmount());
        order.setReceiveAmount(receiveAmount);
        order.setServiceChargePercent(config.getServiceChargePercent());
        order.setServiceCharge(serviceCharge);
        order.setStatus(RechargeOrderStatusEnum.WAITING);
        order.setType(RechargeOrderTypeEnum.USER_REQUEST);
        order.setRechargeConfigSource(RechargeConfigSourceEnum.BANK_CARD_RECHARGE_CONFIG);
        order.setRechargeConfigId(config.getId());
        order.setUserCardName(StrUtil.trim(param.getUserCardName()));
        order.setReceiveBankId(card.getBankId());
        order.setReceiveBankCardName(card.getCardName());
        order.setReceiveBankCardNum(card.getCardNum());
        order.setReceiveBankCardBranch(card.getCardBranch());
        order.setRequestIp(requestIp);
        order.setReportDate(reportDate);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        boolean saved = rechargeOrderService.save(order);

        if (saved) {
            // 日志
            if (CollectionUtil.isNotEmpty(logList)) {
                rechargeOrderLogService.saveBatch(logList);
            }

            //发送系统消息
            portalSystemMessageService.sendRechargeMessage();

            String bankName = bankCache.getNameByIdFromLocal(order.getReceiveBankId());

            return RestResponse.ok(RechargeRequestResponseVO.bankCard(order.getId(), bankName, order.getReceiveBankCardName(),
                    order.getReceiveBankCardNum(), order.getReceiveBankCardBranch()));
        }

        return RestResponse.SYS_DATA_STATUS_ERROR;
    }

    /**
     * 三方充值
     *
     * @param user
     * @param param
     * @param requestIp
     * @return
     */
    private RestResponse<RechargeRequestResponseVO> apiRechargeRequestForPortal(PortalUser user, RechargeRequestPortalParam param, String requestIp) {
        RestResponse<ApiRechargeConfig> configEnabledRsp = apiRechargeConfigCache.getEnabledFromRedisForPortal(param.getConfigId(), user.getId(), userGroupUserCache, userGroupApiRechargeConfigCache);
        if (!configEnabledRsp.isOkRsp()) {
            return RestResponse.buildClearData(configEnabledRsp);
        }

        // 配置状态
        ApiRechargeConfig config = apiRechargeConfigService
                .lambdaQuery()
                .eq(ApiRechargeConfig::getId, param.getConfigId())
                .eq(ApiRechargeConfig::getDeleted, false)
                .one();
        if (config == null || !apiRechargeConfigCache.isAvailableConfig(config)) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 检查付款人
        if (Boolean.TRUE.equals(config.getNeedInputUserCardName()) && StrUtil.isBlank(param.getUserCardName())) {
            return RestResponse.failed(ResponseCode.RECHARGE_USER_CARD_NAME_REQUIRED);
        } else {
            // 移除空格
            param.setUserCardName(StrUtil.trim(param.getUserCardName()));
        }

        // todo 付款人黑白名单

        // 用户充值权限
        if (user != null && !Boolean.TRUE.equals(user.getRechargeEnabled())) {
            return RestResponse.failed(ResponseCode.USER_RECHARGE_DISABLED);
        }

        // 检查金额是否在允许范围内
        RestResponse amountRsp = NumberUtil.isBigDecimalInRangeStr(config.getAmountRange(), config.getAmountMaxPrecision(), param.getAmount());
        if (!amountRsp.isOkRsp()) {
            return amountRsp;
        }

        // 预先创建订单，后续请求三方失败也要保存订单
        BigDecimal payAmount = param.getAmount();
        BigDecimal serviceCharge = NumberUtil.mul(payAmount, config.getServiceChargePercent());
        BigDecimal receiveAmount = NumberUtil.sub(payAmount, serviceCharge);

        // 以创建时间作为报表数据
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        LocalDateTime now = LocalDateTime.now();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());

        RechargeOrder order = new RechargeOrder();
        order.setId(IdWorker.getId());
        order.setOrderNum(OrderUtil.rechargeOrderNumber());
        order.setUserId(user.getId());
        order.setRequestAmount(param.getAmount());
        order.setPayAmount(payAmount);
        order.setReceiveAmount(receiveAmount);
        order.setServiceChargePercent(config.getServiceChargePercent());
        order.setServiceCharge(serviceCharge);
        order.setStatus(RechargeOrderStatusEnum.WAITING);
        order.setType(RechargeOrderTypeEnum.USER_REQUEST);
        order.setRechargeConfigSource(RechargeConfigSourceEnum.API_RECHARGE_CONFIG);
        order.setRechargeConfigId(config.getId());
        order.setUserCardName(StrUtil.trim(param.getUserCardName()));
        order.setRequestIp(requestIp);
        order.setReportDate(reportDate);
        order.setCreateTime(now);
        order.setUpdateTime(now);

        // 日志
        List<RechargeOrderLog> logList = new ArrayList<>();
        if (Boolean.TRUE.equals(config.getNeedInputUserCardName())) {
            logList.add(new RechargeOrderLog(order.getId(), "用户从IP[{}]申请三方充值并输入付款人[{}], 开始请求三方",
                    requestIp, StrUtil.trim(param.getUserCardName())));
        } else {
            logList.add(new RechargeOrderLog(order.getId(), "用户从IP[{}]申请三方充值, 开始请求三方",
                    requestIp));
        }

        // 请求三方
        ApiRechargeHandler handler = ApiRechargeHandlerFactory.getRechargeHandler(config.getCode());
        if (handler == null) {
            // 这里不保存订单，因为没有创建handler属于低级错误，不太可能出现
            return RestResponse.failed(ResponseCode.API_RECHARGE_HANDLER_NOT_FOUND);
        }

        // 发往三方的回调地址，我们自己的
        GlobalConfigBO globalConfig = sysConfigCache.getGlobalConfigFromRedis();
        String callbackUrl = StrUtil.format("{}{}", globalConfig.getApiCallbackUrl(), COMMON_CALLBACK_PATH);
        callbackUrl = StrUtil.format(callbackUrl, MapUtil.of("orderNum", order.getOrderNum()));

        RestResponse<RechargeRequestResponseVO> rsp;
        try {
            ApiRechargeRequestResponseVO apiRsp = handler.requestRecharge(config, order, callbackUrl);
            if (apiRsp == null) {
                // 返回了空，这是低级错误，不应出现
                order.setStatus(RechargeOrderStatusEnum.FAILED);
                rsp = RestResponse.failed(ResponseCode.API_RECHARGE_REQUEST_RETURN_FAILED, "处理类未正确返回响应");

                logList.add(new RechargeOrderLog(order.getId(), "请求失败，原因：{}", "处理类未正确返回响应"));
            } else {
                if (Boolean.TRUE.equals(apiRsp.getSuccess())) {
                    // 三方正确返回
                    rsp = RestResponse.ok(apiRsp.getResponse());
                    logList.add(new RechargeOrderLog(order.getId(), "请求成功，对方返回：{}", JSON.toJSONString(apiRsp.getResponse().getData())));
                } else {
                    // 三方返回了错误
                    order.setStatus(RechargeOrderStatusEnum.FAILED);
                    rsp = RestResponse.failed(ResponseCode.API_RECHARGE_REQUEST_RETURN_FAILED, apiRsp.getMessage());

                    logList.add(new RechargeOrderLog(order.getId(), "请求失败，原因：{}", apiRsp.getMessage()));
                }
            }
        } catch (Exception e) {
            LOG.error(e, "请求三方充值异常，请求参数：{}", JSON.toJSONString(param));
            order.setStatus(RechargeOrderStatusEnum.FAILED);
            rsp = RestResponse.failed(ResponseCode.API_RECHARGE_REQUEST_EXCEPTION, ExceptionUtil.getMessage(e));
        }

        boolean saved = rechargeOrderService.save(order);

        if (saved) {
            // 日志
            if (CollectionUtil.isNotEmpty(logList)) {
                rechargeOrderLogService.saveBatch(logList);
            }
            //发送系统消息
            portalSystemMessageService.sendRechargeMessage();
            return rsp;
        }

        return RestResponse.SYS_DATA_STATUS_ERROR;
    }

    private RestResponse checkRequestForPortalRequired(PortalUser user, RechargeRequestPortalParam param) {
        // 权限
        if (!Boolean.TRUE.equals(user.getRechargeEnabled())) {
            return RestResponse.failed(ResponseCode.SYS_NO_PERMISSION);
        }

        // 检查金额
        if (!NumberUtil.isGreatThenZero(param.getAmount())) {
            return RestResponse.failed(ResponseCode.SYS_AMOUNT_INCORRECT);
        }

        RechargeConfigBO rechargeConfig = sysConfigCache.getRechargeConfigFromRedis();
        // 充值是否必须先绑定资金密码
        if (Boolean.TRUE.equals(rechargeConfig.getFundPwdBindRequired()) && StrUtil.isBlank(user.getFundPwd())) {
            return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_NOT_YET_BIND);
        }
        // 充值是否必须先绑定银行卡
        if (Boolean.TRUE.equals(rechargeConfig.getBankCardBindRequired())) {
            int count = userBankCardService.countUserEnabledBankCard(user.getId());
            if (!NumberUtil.isGreatThenZero(count)) {
                return RestResponse.failed(ResponseCode.USER_BANK_CARD_NOT_YET_BIND);
            }
        }

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<String> callbackByApi(Map<String, String> requestParams, String orderNum, String requestIp) {
        // 查找订单
        RechargeOrder order = rechargeOrderService.lambdaQuery().eq(RechargeOrder::getOrderNum, orderNum).one();
        if (order == null) {
            LOG.error("收到三方充值回调但处理失败，订单号{}没有找到，内容：{}，回调IP：{}", orderNum, JSON.toJSONString(requestParams), requestIp);
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        if (order.getRechargeConfigSource() != RechargeConfigSourceEnum.API_RECHARGE_CONFIG) {
            LOG.error("收到三方充值回调但处理失败，订单号{}不属于三方订单，内容：{}，回调IP：{}", orderNum, JSON.toJSONString(requestParams), requestIp);
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        // 保存回调日志
        List<RechargeOrderLog> logList = new ArrayList<>();
        logList.add(new RechargeOrderLog(order.getId(), "收到三方充值回调，内容：{}，回调IP：{}，正在处理",
                JSON.toJSONString(requestParams), requestIp));

        // 检查IP白名单
        if (!apiRechargeConfigCache.isWhitelistCallbackIp(order.getRechargeConfigId(), requestIp)) {
            logList.add(new RechargeOrderLog(order.getId(), "回调处理失败，对方IP[{}]不在白名单中：{}",
                    requestIp, apiRechargeConfigCache.getWhitelistCallbackIp(order.getRechargeConfigId())));
            rechargeOrderLogService.saveBatch(logList);
            return RestResponse.failed(ResponseCode.IP_NOT_ALLOWED, requestIp);
        }

        ApiRechargeConfig config = apiRechargeConfigCache.getByIdFromRedis(order.getRechargeConfigId());
        if (config == null || Boolean.TRUE.equals(config.getDeleted())) {
            return RestResponse.failed(ResponseCode.API_RECHARGE_CONFIG_NOT_FOUND);
        }

        ApiRechargeHandler handler = ApiRechargeHandlerFactory.getRechargeHandler(config.getCode());
        if (handler == null) {
            logList.add(new RechargeOrderLog(order.getId(), "回调处理失败, 充值处理类未找到: {}，请检查代码", config.getCode().getVal()));
            rechargeOrderLogService.saveBatch(logList);
            return RestResponse.failed(ResponseCode.API_RECHARGE_HANDLER_NOT_FOUND);
        }

        // 已完成，响应出去
        if (order.getStatus() == RechargeOrderStatusEnum.FINISHED) {
            String output = handler.getRechargeSuccessOutput(config, order, requestParams);

            logList.add(new RechargeOrderLog(order.getId(), "回调处理失败, 订单已是完成状态(重复回调?), 本次不处理订单并直接向三方返回成功: {}", output));
            rechargeOrderLogService.saveBatch(logList);

            return RestResponse.ok(output);
        }
        if (order.getStatus() != RechargeOrderStatusEnum.WAITING) {

            logList.add(new RechargeOrderLog(order.getId(), "回调处理失败, 订单状态不是待处理状态"));
            rechargeOrderLogService.saveBatch(logList);

            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        ApiRechargeVerifyResult verifyResult;
        try {
            verifyResult = handler.verifyRechargeCallback(config, order, requestParams);
            if (verifyResult == null) {
                logList.add(new RechargeOrderLog(order.getId(), "回调处理失败，处理类返回了空，请检查代码"));
                rechargeOrderLogService.saveBatch(logList);

                return RestResponse.failed(ResponseCode.API_RECHARGE_CALLBACK_PROCESS_FAILED);
            }
        } catch (Exception e) {
            logList.add(new RechargeOrderLog(order.getId(), "回调处理失败，发生异常: {}", ExceptionUtil.getMessage(e)));
            rechargeOrderLogService.saveBatch(logList);
            return RestResponse.failed(ResponseCode.API_RECHARGE_CALLBACK_PROCESS_FAILED);
        }

        // 三方回调的状态是处理中，没有用
        if (verifyResult.getStatus() == RechargeOrderStatusEnum.WAITING) {
            logList.add(new RechargeOrderLog(order.getId(), "回调处理失败，对方状态仍是处理中"));
            rechargeOrderLogService.saveBatch(logList);

            return RestResponse.failed(ResponseCode.API_RECHARGE_CALLBACK_PROCESS_FAILED);
        }

        // 处理结果
        RestResponse rsp = processVerityResult(config, order, verifyResult, RechargeOrderFinishTypeEnum.API_CALLBACK, requestIp, logList);
        if (!rsp.isOkRsp()) {
            logList.add(new RechargeOrderLog(order.getId(), "回调处理失败，我方状态码：{}", rsp.getCode()));
            rechargeOrderLogService.saveBatch(logList);

            return rsp;
        }

        logList.add(new RechargeOrderLog(order.getId(), "回调处理成功，向三方返回成功：{}", verifyResult.getOutput()));
        rechargeOrderLogService.saveBatch(logList);

        // 输出
        return RestResponse.ok(verifyResult.getOutput());
    }

    private RestResponse processVerityResult(ApiRechargeConfig config,
                                             RechargeOrder order,
                                             ApiRechargeVerifyResult verifyRsp,
                                             RechargeOrderFinishTypeEnum finishType,
                                             String callbackIp,
                                             List<RechargeOrderLog> logList) {
        if (verifyRsp.getStatus() == RechargeOrderStatusEnum.FINISHED) {
            // 兑换前实付
            BigDecimal payAmount = Optional.ofNullable(verifyRsp.getPayAmount()).orElse(order.getRequestAmount());
            String oldUserCardName = order.getUserCardName();

            // 兑换虚拟币
            BigDecimal serviceCharge = NumberUtil.mul(payAmount, config.getServiceChargePercent());
            BigDecimal receiveAmount = NumberUtil.sub(payAmount, serviceCharge);

            order.setPayAmount(payAmount);
            order.setReceiveAmount(receiveAmount);
            order.setServiceCharge(serviceCharge);
            order.setStatus(verifyRsp.getStatus());
            order.setFinishType(finishType);
            order.setFinishTime(LocalDateTime.now());
            if (StrUtil.isNotBlank(verifyRsp.getUserCardName())) {
                order.setUserCardName(verifyRsp.getUserCardName());
            }

            // 修改订单
            boolean updated = rechargeOrderService
                    .lambdaUpdate()
                    .eq(RechargeOrder::getId, order.getId())
                    .eq(RechargeOrder::getStatus, RechargeOrderStatusEnum.WAITING)
                    .set(RechargeOrder::getPayAmount, order.getPayAmount())
                    .set(RechargeOrder::getReceiveAmount, order.getReceiveAmount())
                    .set(RechargeOrder::getServiceCharge, order.getServiceCharge())
                    .set(RechargeOrder::getStatus, order.getStatus())
                    .set(RechargeOrder::getFinishType, order.getFinishType())
                    .set(RechargeOrder::getFinishTime, order.getFinishTime())
                    .set(StrUtil.isNotBlank(verifyRsp.getUserCardName()),
                            RechargeOrder::getUserCardName, verifyRsp.getUserCardName())
                    .set(StrUtil.isNotBlank(callbackIp),
                            RechargeOrder::getApiRechargeCallbackIp, callbackIp)
                    .update();
            if (!updated) {
                return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
            }

            // 给用户加余额，以到账为准
            portalUserService.addBalanceWithReportDate(order.getUserId(), order.getReceiveAmount(), order.getOrderNum(),
                    UserBillTypeEnum.USER_RECHARGE, null, order.getReportDate(), true);

            // TODO 报表

            // 修改用户首充数据
            portalUserService.updateFirstAndTotalRecharge(order.getUserId(), order.getPayAmount(), order.getCreateTime());

            if (StrUtil.isNotBlank(verifyRsp.getUserCardName()) && !StrUtil.equals(verifyRsp.getUserCardName(), oldUserCardName)) {
                logList.add(new RechargeOrderLog(order.getId(), "对方状态为成功，并从结果中得到实际付款人为[{}]，成功为用户上分", verifyRsp.getUserCardName()));
            } else {
                logList.add(new RechargeOrderLog(order.getId(), "对方状态为成功，成功为用户上分"));
            }

            return RestResponse.OK;
        } else if (verifyRsp.getStatus() == RechargeOrderStatusEnum.FAILED) {
            String oldUserCardName = order.getUserCardName();

            // 修改订单
            boolean updated = rechargeOrderService
                    .lambdaUpdate()
                    .eq(RechargeOrder::getId, order.getId())
                    .eq(RechargeOrder::getStatus, RechargeOrderStatusEnum.WAITING)
                    .set(RechargeOrder::getStatus, verifyRsp.getStatus())
                    .set(RechargeOrder::getFinishType, finishType)
                    .set(RechargeOrder::getFinishTime, LocalDateTime.now())
                    .set(StrUtil.isNotBlank(verifyRsp.getUserCardName()),
                            RechargeOrder::getUserCardName, verifyRsp.getUserCardName())
                    .set(StrUtil.isNotBlank(callbackIp),
                            RechargeOrder::getApiRechargeCallbackIp, callbackIp)
                    .update();
            if (!updated) {
                return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
            }


            if (StrUtil.isNotBlank(verifyRsp.getUserCardName()) && !StrUtil.equals(verifyRsp.getUserCardName(), oldUserCardName)) {
                logList.add(new RechargeOrderLog(order.getId(), "对方状态为失败，并从结果中得到实际付款人为[{}]，订单失败，原因：{}",
                        verifyRsp.getMessage(), verifyRsp.getUserCardName()));
            } else {
                logList.add(new RechargeOrderLog(order.getId(), "对方状态为失败，订单失败，原因：{}", verifyRsp.getMessage()));
            }

            return RestResponse.OK;
        } else if (verifyRsp.getStatus() == RechargeOrderStatusEnum.WAITING) {
            logList.add(new RechargeOrderLog(order.getId(), "对方状态为处理中"));

            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        return RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void syncStatusFromApi(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<RechargeOrder> list = rechargeOrderService
                .lambdaQuery()
                .eq(RechargeOrder::getStatus, RechargeOrderStatusEnum.WAITING)
                .eq(RechargeOrder::getRechargeConfigSource, RechargeConfigSourceEnum.API_RECHARGE_CONFIG)
                .ge(RechargeOrder::getCreateTime, startDateTime)
                .le(RechargeOrder::getCreateTime, endDateTime)
                .list();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        LOG.info("正在处理{}笔三方充值订单状态同步", list.size());
        syncStatusFromThird(list);
        LOG.info("处理{}笔三方充值订单状态同步完成", list.size());
    }

    private void syncStatusFromThird(List<RechargeOrder> list) {
        // 分组
        Map<Long, List<RechargeOrder>> configOrderMap = CollectionUtil.toMapList(list, e -> e.getRechargeConfigId());

        List<RechargeOrderLog> logList = new ArrayList<>();

        configOrderMap.forEach((configId, configOrderList) -> {
            ApiRechargeConfig config = apiRechargeConfigCache.getByIdFromRedis(configId);
            if (config != null && !Boolean.TRUE.equals(config.getDeleted())) {
                ApiRechargeHandler handler = ApiRechargeHandlerFactory.getRechargeHandler(config.getCode());
                if (handler != null) {
                    // 记录日志
                    logList.addAll(CollectionUtil.toList(configOrderList, e -> new RechargeOrderLog(e.getId(), "系统开始向三方同步订单状态，本批次共处理{}条订单", configOrderList.size())));

                    try {
                        syncStatusFromThirdByOrder(configOrderList, config, handler, logList);
                    } catch (Exception ex) {
                        logList.addAll(CollectionUtil.toList(configOrderList, e -> new RechargeOrderLog(e.getId(), "同步订单状态时发生异常，{}", ExceptionUtil.getMessage(ex))));
                        LOG.error(ex, "同步{}充值订单异常", config.getAdminName());
                    }
                }
            }
        });

        if (CollectionUtil.isNotEmpty(logList)) {
            rechargeOrderLogService.saveBatch(logList);
        }
    }

    private void syncStatusFromThirdByOrder(List<RechargeOrder> configOrderList,
                                            ApiRechargeConfig config,
                                            ApiRechargeHandler handler,
                                            List<RechargeOrderLog> logList) throws Exception {


        List<ApiRechargeVerifyResult> resultList = handler.queryRechargeFromThird(config, configOrderList);
        if (CollectionUtil.isEmpty(resultList)) {
            return;
        }
        for (ApiRechargeVerifyResult verifyResult : resultList) {
            RechargeOrder order = CollectionUtil.findFirst(configOrderList, e -> e.getId().equals(verifyResult.getOrderId()));
            processVerityResult(config, order, verifyResult, RechargeOrderFinishTypeEnum.AUTOMATIC_SYNC, null, logList);
        }
    }
}
