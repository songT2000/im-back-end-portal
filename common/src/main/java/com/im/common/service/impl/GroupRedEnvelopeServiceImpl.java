package com.im.common.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.impl.TimGroupCache;
import com.im.common.cache.sysconfig.bo.RedEnvelopeConfigBO;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.GroupRedEnvelope;
import com.im.common.entity.GroupRedEnvelopeReceive;
import com.im.common.entity.PortalUser;
import com.im.common.entity.enums.CustomMessageTypeEnum;
import com.im.common.entity.enums.GroupRedEnvelopeStatusEnum;
import com.im.common.entity.enums.OnOrOffEnum;
import com.im.common.entity.enums.UserBillTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.exception.ImException;
import com.im.common.mapper.GroupRedEnvelopeMapper;
import com.im.common.param.GroupRedEnvelopeReceivePageAdminParam;
import com.im.common.param.GroupRedEnvelopeSendPortalParam;
import com.im.common.param.IdParam;
import com.im.common.param.TimGroupCustomMessageSendParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.*;
import com.im.common.util.*;
import com.im.common.util.api.im.tencent.entity.param.message.TiModifyGroupMessageParam;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgCustomItem;
import com.im.common.util.api.im.tencent.entity.param.message.TiOfflinePushInfoParam;
import com.im.common.util.api.im.tencent.entity.result.message.TiGroupMessageSendResult;
import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
import com.im.common.util.i18n.I18nTranslateUtil;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.GroupRedEnvelopePortalVO;
import com.im.common.vo.GroupRedEnvelopeReceiveAdminVO;
import com.im.common.vo.GroupRedEnvelopeReceivePortalVO;
import com.im.common.vo.PortalSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 群红包 服务实现类
 *
 * @author Barry
 * @date 2021-12-20
 */
@Service
public class GroupRedEnvelopeServiceImpl
        extends MyBatisPlusServiceImpl<GroupRedEnvelopeMapper, GroupRedEnvelope>
        implements GroupRedEnvelopeService {
    private PortalUserService portalUserService;
    private TimGroupService timGroupService;
    private TimGroupMemberService timGroupMemberService;
    private SysConfigCache sysConfigCache;
    private GroupRedEnvelopeMapper groupRedEnvelopeMapper;
    private GroupRedEnvelopeReceiveService groupRedEnvelopeReceiveService;
    private TimGroupCache timGroupCache;
    private TiGroupService tiGroupService;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @Autowired
    public void setTimGroupMemberService(TimGroupMemberService timGroupMemberService) {
        this.timGroupMemberService = timGroupMemberService;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setGroupRedEnvelopeMapper(GroupRedEnvelopeMapper groupRedEnvelopeMapper) {
        this.groupRedEnvelopeMapper = groupRedEnvelopeMapper;
    }

    @Autowired
    public void setGroupRedEnvelopeReceiveService(GroupRedEnvelopeReceiveService groupRedEnvelopeReceiveService) {
        this.groupRedEnvelopeReceiveService = groupRedEnvelopeReceiveService;
    }

    @Autowired
    public void setTimGroupCache(TimGroupCache timGroupCache) {
        this.timGroupCache = timGroupCache;
    }

    @Autowired
    public void setTiGroupService(TiGroupService tiGroupService) {
        this.tiGroupService = tiGroupService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public GroupRedEnvelopePortalVO getForPortal(PortalSessionUser sessionUser, IdParam param) {
        // 当前用户必须在群里
        GroupRedEnvelope envelope = getById(param.getId());
        Long groupId = envelope.getGroupId();

        // 必须在群里，不能被禁言
        {
            String timGroupId = timGroupCache.getTimIdBySysIdFromLocal(groupId);
            TimGroupMember groupMember = timGroupMemberService.getByGroupIdAndUserId(timGroupId, sessionUser.getId());
            if (groupMember == null) {
                return null;
            }
        }

        // 当前用户是否已领取
        Boolean currentUserReceived = false;
        {
            Integer count = groupRedEnvelopeReceiveService
                    .lambdaQuery()
                    .eq(GroupRedEnvelopeReceive::getEnvelopeId, envelope.getId())
                    .eq(GroupRedEnvelopeReceive::getReceiveUserId, sessionUser.getId())
                    .count();
            currentUserReceived = NumberUtil.isGreatThenZero(count);
        }

        return new GroupRedEnvelopePortalVO(envelope, currentUserReceived);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<Long> sendForPortal(PortalSessionUser sessionUser, GroupRedEnvelopeSendPortalParam param) {
        if (!NumberUtil.isAmount(param.getAmount(), true, 2)) {
            return RestResponse.failed(ResponseCode.SYS_AMOUNT_MAX_PRECISION_INCORRECT, 2);
        }
        RedEnvelopeConfigBO redEnvelopeConfig = sysConfigCache.getRedEnvelopeConfigFromRedis();
        if (param.getNum() > redEnvelopeConfig.getGroupMaxNum()) {
            return RestResponse.failed(ResponseCode.ENVELOPE_MAX_NUM_INCORRECT, redEnvelopeConfig.getGroupMaxNum());
        }
        BigDecimal averageAmount = NumberUtil.div(param.getAmount(), param.getNum());
        if (!NumberUtil.isBigDecimalInRangeStr(redEnvelopeConfig.getGroupAverageAmountRange(), averageAmount)) {
            return RestResponse.failed(ResponseCode.ENVELOPE_AVERAGE_AMOUNT_INCORRECT, redEnvelopeConfig.getGroupAverageAmountRange());
        }

        // 我方必须余额足
        PortalUser user = portalUserService.getById(sessionUser.getId());
        {
            if (NumberUtil.isGreater(param.getAmount(), user.getBalance())) {
                return RestResponse.failed(ResponseCode.USER_INSUFFICIENT_BALANCE);
            }
        }

        // 我方提现权限必须是正常
        if (!Boolean.TRUE.equals(user.getWithdrawEnabled())) {
            return RestResponse.failed(ResponseCode.USER_WITHDRAW_DISABLED);
        }

        // 群不能被全员禁言
        TimGroup group = timGroupService.getByGroupId(param.getGroupId());
        {
            if (group == null) {
                return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
            }
            // 群全员禁言状态,on或者off
            if (group.getShutUpState() == OnOrOffEnum.ON) {
                return RestResponse.failed(ResponseCode.USER_GROUP_ALL_SHUTTING_UP);
            }
        }

        // 必须在群里，不能被禁言
        {
            TimGroupMember groupMember = timGroupMemberService.getByGroupIdAndUserId(param.getGroupId(), sessionUser.getId());
            if (groupMember == null) {
                return RestResponse.failed(ResponseCode.USER_NOT_IN_GROUP);
            }
            if (groupMember.getShutUpEndTime() != null && DateTimeUtil.isBeforeOrEqual(LocalDateTime.now(), groupMember.getShutUpEndTime())) {
                return RestResponse.failed(ResponseCode.USER_GROUP_SHUTTING_UP);
            }
        }

        // 创建记录，过期时间为系统配置
        String orderNum = OrderUtil.orderNumber();
        String remark = StrUtil.trim(param.getRemark());
        GroupRedEnvelope envelope = new GroupRedEnvelope();
        envelope.setOrderNum(orderNum);
        envelope.setUserId(user.getId());
        envelope.setGroupId(group.getId());
        envelope.setAmount(param.getAmount());
        envelope.setNum(param.getNum());
        envelope.setStatus(GroupRedEnvelopeStatusEnum.UN_RECEIVED);
        envelope.setReceivedAmount(BigDecimal.ZERO);
        envelope.setReceivedNum(CommonConstant.INT_0);
        envelope.setExpireTime(LocalDateTime.now().plusHours(redEnvelopeConfig.getExpireHours()));
        envelope.setRemark(remark);
        save(envelope);

        // 我方扣钱，需要对方领取红包扣再给对方加钱
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(envelope.getCreateTime(), reportConfig.getOffsetTime());
        RestResponse balanceRsp = portalUserService.addBalanceWithReportDate(user.getId(), param.getAmount().negate(), orderNum,
                UserBillTypeEnum.GROUP_RED_ENVELOPE_SEND, null, reportDate, false);
        if (!balanceRsp.isOkRsp()) {
            return balanceRsp;
        }

        // IM发送群聊消息(如果发送失败，回滚数据)
        {
            // 封装好的消息
            GroupRedEnvelopePortalVO vo = new GroupRedEnvelopePortalVO(envelope, false);
            String fromAccount = sessionUser.getUsername();
            String groupId = group.getGroupId();

            // 发送红包自定义消息
            String offlineMessage = I18nTranslateUtil.translate("RSP_MSG.OFFLINE_PUSH_INFO_MESSAGE#I18N");
            TiOfflinePushInfoParam offlinePushInfo = new TiOfflinePushInfoParam(group.getGroupName(), offlineMessage);
            TiMsgCustomItem item = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_GROUP.getVal(), vo, fromAccount);
            TimGroupCustomMessageSendParam sendParam = new TimGroupCustomMessageSendParam(groupId, item, fromAccount);
            sendParam.setOfflinePushInfo(offlinePushInfo);
            RestResponse<TiGroupMessageSendResult> rsp = tiGroupService.sendCustomMessage(sendParam);
            if (!rsp.isOkRsp()) {
                // 如果发送失败，回滚数据
                throw new ImException(rsp);
            }
            //更新群消息的序列号
            lambdaUpdate()
                    .eq(GroupRedEnvelope::getId, envelope.getId())
                    .set(GroupRedEnvelope::getMsgSeq, rsp.getData().getMsgSeq())
                    .update();
        }

        // 后面定期检查红包过期时间
        return RestResponse.ok(envelope.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<BigDecimal> receiveForPortal(PortalSessionUser sessionUser, IdParam param) {
        // 获取红包
        GroupRedEnvelope envelope = getById(param.getId());
        if (envelope == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }
        if (envelope.getStatus() == GroupRedEnvelopeStatusEnum.EXPIRED) {
            return RestResponse.failed(ResponseCode.ENVELOPE_EXPIRED);
        }
        if (envelope.getStatus() == GroupRedEnvelopeStatusEnum.ALL_RECEIVED) {
            return RestResponse.failed(ResponseCode.ENVELOPE_ALL_RECEIVED);
        }
        if (envelope.getStatus() != GroupRedEnvelopeStatusEnum.UN_RECEIVED && envelope.getStatus() != GroupRedEnvelopeStatusEnum.PART_RECEIVED) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        if (NumberUtil.isGreaterOrEqual(envelope.getReceivedAmount(), envelope.getAmount())) {
            return RestResponse.failed(ResponseCode.ENVELOPE_ALL_RECEIVED);
        }
        // 红包不能是过期状态
        if (envelope.getExpireTime().isBefore(LocalDateTime.now())) {
            return RestResponse.failed(ResponseCode.ENVELOPE_EXPIRED);
        }

        // 必须在群里
        TimGroup group = timGroupService.getById(envelope.getGroupId());
        {
            TimGroupMember groupMember = timGroupMemberService.getByGroupIdAndUserId(group.getGroupId(), sessionUser.getId());
            if (groupMember == null) {
                return RestResponse.failed(ResponseCode.USER_NOT_IN_GROUP);
            }
        }

        // 当前用户是否已领取
        {
            Integer count = groupRedEnvelopeReceiveService
                    .lambdaQuery()
                    .eq(GroupRedEnvelopeReceive::getEnvelopeId, envelope.getId())
                    .eq(GroupRedEnvelopeReceive::getReceiveUserId, sessionUser.getId())
                    .count();
            if (NumberUtil.isGreatThenZero(count)) {
                return RestResponse.failed(ResponseCode.ENVELOPE_RECEIVED);
            }
        }

        // 随机一个金额，最多2位小数
        // 如果剩余个数>1，那么随机min~max
        // 如果剩余个数=1，那么take all

        // min~max随机规则
        // min是配置的average最小，max是剩余金额均值的两倍(比如剩余100块/10个，max = 100 / 10 * 2 = 20)
        // 第1次假设随机走了1个20块，剩余80/9，max = 80 / 9 * 2 = 17.7
        // 第2次假设随机走了1个5块，剩余75/8，max = 75 / 8 * 2 = 18.75
        // 第3次假设随机走了1个8块，剩余67/7，max = 67 / 7 * 2 = 19.14
        // 第4次假设随机走了1个2块，剩余65/6，max = 65 / 6 * 2 = 21.66

        BigDecimal remainAmount = NumberUtil.sub(envelope.getAmount(), envelope.getReceivedAmount());
        int remainNum = envelope.getNum() - envelope.getReceivedNum();

        BigDecimal randomAmount;
        GroupRedEnvelopeStatusEnum status;
        if (remainNum == CommonConstant.INT_1) {
            randomAmount = remainAmount;
            status = GroupRedEnvelopeStatusEnum.ALL_RECEIVED;
        } else {
            RedEnvelopeConfigBO redEnvelopeConfig = sysConfigCache.getRedEnvelopeConfigFromRedis();
            BigDecimal minAmount = Convert.toBigDecimal(redEnvelopeConfig.getGroupAverageAmountRange().split("~")[0]);
            BigDecimal maxAmount = NumberUtil.mul(NumberUtil.div(remainAmount, remainNum), 2);
            randomAmount = RandomUtil.randomBigDecimal(minAmount, maxAmount);
            randomAmount = randomAmount.setScale(2, RoundingMode.DOWN);
            status = GroupRedEnvelopeStatusEnum.PART_RECEIVED;
        }

        // 修改红包状态，通过mapper修改
        boolean received = groupRedEnvelopeMapper.addReceived(envelope.getId(), randomAmount, status);
        if (!received) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        envelope.setReceivedAmount(NumberUtil.add(envelope.getReceivedAmount(), randomAmount));
        envelope.setReceivedNum(envelope.getReceivedNum() + 1);
        envelope.setStatus(status);

        // 保存领取记录
        LocalDateTime now = LocalDateTime.now();
        GroupRedEnvelopeReceive receive = new GroupRedEnvelopeReceive();
        receive.setEnvelopeId(envelope.getId());
        receive.setReceiveUserId(sessionUser.getId());
        receive.setAmount(randomAmount);
        groupRedEnvelopeReceiveService.save(receive);

        // 我方加钱
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());
        portalUserService.addBalanceWithReportDate(sessionUser.getId(), randomAmount, envelope.getOrderNum(),
                UserBillTypeEnum.GROUP_RED_ENVELOPE_RECEIVE, null, reportDate, true);

        // IM发送群聊消息(如果发送失败，回滚数据)
        {
            // 封装好的消息
            GroupRedEnvelopePortalVO vo = new GroupRedEnvelopePortalVO(envelope, true);
            vo.setReceivedUserNickname(UserUtil.getUserNicknameByIdFromLocal(sessionUser.getId()));
            String fromAccount = sessionUser.getUsername();
            String groupId = group.getGroupId();

            TiMsgCustomItem item = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_GROUP_RECEIVE.getVal(), vo, fromAccount);
            TimGroupCustomMessageSendParam sendParam = new TimGroupCustomMessageSendParam(groupId, item, fromAccount);
            RestResponse rsp = tiGroupService.sendCustomMessage(sendParam);
            if (!rsp.isOkRsp()) {
                // 如果发送失败，回滚数据
                throw new ImException(rsp);
            }

            //更新发红包消息的状态
            String sendUsername = portalUserCache.getUsernameByIdFromLocal(envelope.getUserId());
            TiMsgCustomItem modifyItem = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_GROUP.getVal(), vo, sendUsername);
            TiModifyGroupMessageParam modifyGroupMessageParam = new TiModifyGroupMessageParam(groupId, modifyItem, envelope.getMsgSeq());
            tiGroupService.modifyGroupMsg(modifyGroupMessageParam);
        }

        return RestResponse.ok(randomAmount);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<GroupRedEnvelopeReceivePortalVO> listReceivedByEnvelopeIdForPortal(PortalSessionUser sessionUser, IdParam param) {
        // 先获取红包记录，得到系统的组ID
        GroupRedEnvelope envelope = getById(param.getId());
        if (envelope == null) {
            return new ArrayList<>();
        }

        // 必须在群里
        TimGroup group = timGroupService.getById(envelope.getGroupId());
        TimGroupMember groupMember = timGroupMemberService.getByGroupIdAndUserId(group.getGroupId(), sessionUser.getId());
        if (groupMember == null) {
            return new ArrayList<>();
        }

        // 返回记录
        List<GroupRedEnvelopeReceive> list = groupRedEnvelopeReceiveService
                .lambdaQuery()
                .eq(GroupRedEnvelopeReceive::getEnvelopeId, envelope.getId())
                .orderByDesc(GroupRedEnvelopeReceive::getId)
                .list();

        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 群成员
        List<TimGroupMember> memberList = timGroupMemberService
                .lambdaQuery()
                .select(TimGroupMember::getUserId, TimGroupMember::getNameCard)
                .eq(TimGroupMember::getGroupId, group.getGroupId())
                .list();

        return CollectionUtil.toList(list, e -> new GroupRedEnvelopeReceivePortalVO(e, memberList));
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PageVO<GroupRedEnvelopeReceiveAdminVO> pageReceivedByEnvelopeIdForAdmin(GroupRedEnvelopeReceivePageAdminParam param) {
        // 先获取红包记录，得到系统的组ID
        GroupRedEnvelope envelope = getById(param.getEnvelopeId());
        if (envelope == null) {
            return new PageVO<>(param);
        }

        TimGroup group = timGroupService.getById(envelope.getGroupId());

        // 群成员
        List<TimGroupMember> memberList = timGroupMemberService
                .lambdaQuery()
                .select(TimGroupMember::getUserId, TimGroupMember::getNameCard)
                .eq(TimGroupMember::getGroupId, group.getGroupId())
                .list();

        // 返回记录
        PageVO<GroupRedEnvelopeReceiveAdminVO> pageVO = groupRedEnvelopeReceiveService.pageVO(param, e -> new GroupRedEnvelopeReceiveAdminVO(e, memberList));

        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkExpired() {
        // 列出过期红包，一次只拿1000条

        List<GroupRedEnvelope> list = lambdaQuery()
                .in(GroupRedEnvelope::getStatus, CollectionUtil.toList(GroupRedEnvelopeStatusEnum.UN_RECEIVED, GroupRedEnvelopeStatusEnum.PART_RECEIVED))
                .lt(GroupRedEnvelope::getExpireTime, LocalDateTime.now())
                .last("limit 1000")
                .list();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        // 退回金额
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        for (GroupRedEnvelope envelope : list) {
            // 修改红包状态
            boolean updated = lambdaUpdate()
                    .eq(GroupRedEnvelope::getId, envelope.getId())
                    .eq(GroupRedEnvelope::getStatus, envelope.getStatus())
                    .set(GroupRedEnvelope::getStatus, GroupRedEnvelopeStatusEnum.EXPIRED)
                    .update();
            if (!updated) {
                continue;
            }
            envelope.setStatus(GroupRedEnvelopeStatusEnum.EXPIRED);

            // 我方加钱
            BigDecimal remainAmount = NumberUtil.sub(envelope.getAmount(), envelope.getReceivedAmount());
            String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(LocalDateTime.now(), reportConfig.getOffsetTime());
            portalUserService.addBalanceWithReportDate(envelope.getUserId(), remainAmount, envelope.getOrderNum(),
                    UserBillTypeEnum.GROUP_RED_ENVELOPE_EXPIRE, null, reportDate, true);

            // IM发送群聊消息(如果发送失败，回滚数据)
            {
                // 封装好的消息
                GroupRedEnvelopePortalVO vo = new GroupRedEnvelopePortalVO(envelope, false);
                String groupId = timGroupCache.getTimIdBySysIdFromLocal(envelope.getGroupId());
                String toAccount = portalUserCache.getUsernameByIdFromLocal(envelope.getUserId());

                // 发送红包退回自定义消息
                TiMsgCustomItem item = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_GROUP_EXPIRED.getVal(), vo, toAccount);
                TimGroupCustomMessageSendParam sendParam = new TimGroupCustomMessageSendParam(groupId, item, ListUtil.of(toAccount));
                RestResponse rsp = tiGroupService.sendCustomMessage(sendParam);
                if (!rsp.isOkRsp()) {
                    // 如果发送失败，回滚数据
                    throw new ImException(rsp);
                }

                //更新发红包消息的状态
                String sendUsername = portalUserCache.getUsernameByIdFromLocal(envelope.getUserId());
                TiMsgCustomItem modifyItem = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_GROUP.getVal(), vo, sendUsername);
                TiModifyGroupMessageParam modifyGroupMessageParam = new TiModifyGroupMessageParam(groupId, modifyItem, envelope.getMsgSeq());
                tiGroupService.modifyGroupMsg(modifyGroupMessageParam);
            }
        }
    }
}
