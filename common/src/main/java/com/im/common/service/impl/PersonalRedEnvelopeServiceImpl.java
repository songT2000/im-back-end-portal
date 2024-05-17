package com.im.common.service.impl;

import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.RedEnvelopeConfigBO;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.entity.PersonalRedEnvelope;
import com.im.common.entity.PortalUser;
import com.im.common.entity.enums.CustomMessageTypeEnum;
import com.im.common.entity.enums.PersonalRedEnvelopeStatusEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserBillTypeEnum;
import com.im.common.entity.tim.TimFriend;
import com.im.common.exception.ImException;
import com.im.common.mapper.PersonalRedEnvelopeMapper;
import com.im.common.param.IdParam;
import com.im.common.param.PersonalRedEnvelopeSendPortalParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PersonalRedEnvelopeService;
import com.im.common.service.PortalUserService;
import com.im.common.service.TimFriendService;
import com.im.common.service.UserBillService;
import com.im.common.util.*;
import com.im.common.util.api.im.tencent.entity.param.message.TiMessage;
import com.im.common.util.api.im.tencent.entity.param.message.TiModifyC2cMessageParam;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgCustomItem;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageSendResult;
import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.PersonalRedEnvelopePortalVO;
import com.im.common.vo.PortalSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 个人红包 服务实现类
 *
 * @author Barry
 * @date 2021-12-20
 */
@Service
public class PersonalRedEnvelopeServiceImpl
        extends MyBatisPlusServiceImpl<PersonalRedEnvelopeMapper, PersonalRedEnvelope>
        implements PersonalRedEnvelopeService {
    private PortalUserService portalUserService;
    private SysConfigCache sysConfigCache;
    private UserBillService userBillService;
    private TimFriendService timFriendService;
    private TiSingleChatService tiSingleChatService;

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setUserBillService(UserBillService userBillService) {
        this.userBillService = userBillService;
    }

    @Autowired
    public void setTimFriendService(TimFriendService timFriendService) {
        this.timFriendService = timFriendService;
    }

    @Autowired
    public void setTiSingleChatService(TiSingleChatService tiSingleChatService) {
        this.tiSingleChatService = tiSingleChatService;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PersonalRedEnvelopePortalVO getForPortal(PortalSessionUser sessionUser, IdParam param) {
        // 发送人和领取人都可以
        PersonalRedEnvelope envelope = lambdaQuery()
                .eq(PersonalRedEnvelope::getId, param.getId())
                .nested(e -> e.eq(PersonalRedEnvelope::getUserId, sessionUser.getId())
                        .or()
                        .eq(PersonalRedEnvelope::getReceiveUserId, sessionUser.getId()))
                .one();
        if (envelope == null) {
            return null;
        }

        return new PersonalRedEnvelopePortalVO(envelope);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<Long> sendForPortal(PortalSessionUser sessionUser, PersonalRedEnvelopeSendPortalParam param) {
        Long receiveUserId = UserUtil.getUserIdByUsernameFromLocal(param.getReceiveUsername(), PortalTypeEnum.PORTAL);
        if (receiveUserId == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        }
        if (receiveUserId.equals(sessionUser.getId())) {
            return RestResponse.failed(ResponseCode.ENVELOPE_NOT_TO_SELF);
        }
        if (!NumberUtil.isAmount(param.getAmount(), true, 2)) {
            return RestResponse.failed(ResponseCode.SYS_AMOUNT_MAX_PRECISION_INCORRECT, 2);
        }
        RedEnvelopeConfigBO redEnvelopeConfig = sysConfigCache.getRedEnvelopeConfigFromRedis();
        if (!NumberUtil.isBigDecimalInRangeStr(redEnvelopeConfig.getPersonalAmountRange(), param.getAmount())) {
            return RestResponse.failed(ResponseCode.SYS_AMOUNT_NOT_IN_RANGE, redEnvelopeConfig.getPersonalAmountRange());
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

        // 必须是双向好友关系
        {
            TimFriend myFriend = timFriendService.getByUserId(user.getId(), receiveUserId);
            if (myFriend == null) {
                return RestResponse.failed(ResponseCode.NOT_YOUR_FRIEND);
            }
            TimFriend receiveFriend = timFriendService.getByUserId(receiveUserId, user.getId());
            if (receiveFriend == null) {
                return RestResponse.failed(ResponseCode.NOT_YOUR_FRIEND);
            }
        }

        // 创建记录，过期时间为系统配置
        String orderNum = OrderUtil.orderNumber();
        String remark = StrUtil.trim(param.getRemark());
        PersonalRedEnvelope envelope = new PersonalRedEnvelope();
        envelope.setOrderNum(orderNum);
        envelope.setUserId(user.getId());
        envelope.setReceiveUserId(receiveUserId);
        envelope.setAmount(param.getAmount());
        envelope.setStatus(PersonalRedEnvelopeStatusEnum.UN_RECEIVED);
        envelope.setExpireTime(LocalDateTime.now().plusHours(redEnvelopeConfig.getExpireHours()));
        envelope.setRemark(remark);
        save(envelope);

        // 我方扣钱，需要对方领取红包扣再给对方加钱
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(envelope.getCreateTime(), reportConfig.getOffsetTime());
        RestResponse balanceRsp = portalUserService.addBalanceWithReportDate(user.getId(), param.getAmount().negate(), orderNum,
                UserBillTypeEnum.PERSONAL_RED_ENVELOPE_SEND, null, reportDate, false);
        if (!balanceRsp.isOkRsp()) {
            return balanceRsp;
        }

        // IM发送单聊消息(如果发送失败，回滚数据)
        {
            // 封装好的消息
            PersonalRedEnvelopePortalVO vo = new PersonalRedEnvelopePortalVO(envelope);
            String fromAccount = sessionUser.getUsername();
            String toAccount = param.getReceiveUsername();

            // 发送自定义消息
            TiMsgCustomItem tiMsgCustomItem = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_C2C.getVal(), vo, fromAccount);
            TiMessage tiMessage = new TiMessage(fromAccount, toAccount, tiMsgCustomItem);
            RestResponse<TiSingleMessageSendResult> rsp = tiSingleChatService.sendMessage(tiMessage);
            if (!rsp.isOkRsp()) {
                // 如果发送失败，回滚数据
                throw new ImException(rsp);
            }
            //更新红包消息的序列号
            lambdaUpdate()
                    .eq(PersonalRedEnvelope::getId, envelope.getId())
                    .set(PersonalRedEnvelope::getMsgKey, rsp.getData().getMsgKey())
                    .update();
        }

        // 后面定期检查红包过期时间
        return RestResponse.ok(envelope.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse receiveForPortal(PortalSessionUser sessionUser, IdParam param) {
        // 获取红包
        PersonalRedEnvelope envelope = lambdaQuery()
                .eq(PersonalRedEnvelope::getId, param.getId())
                .eq(PersonalRedEnvelope::getReceiveUserId, sessionUser.getId())
                .one();
        if (envelope == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }
        if (envelope.getStatus() == PersonalRedEnvelopeStatusEnum.EXPIRED) {
            return RestResponse.failed(ResponseCode.ENVELOPE_EXPIRED);
        }
        if (envelope.getStatus() == PersonalRedEnvelopeStatusEnum.RECEIVED) {
            return RestResponse.failed(ResponseCode.ENVELOPE_RECEIVED);
        }
        if (envelope.getStatus() != PersonalRedEnvelopeStatusEnum.UN_RECEIVED) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 红包不能是过期状态
        if (envelope.getExpireTime().isBefore(LocalDateTime.now())) {
            return RestResponse.failed(ResponseCode.ENVELOPE_EXPIRED);
        }

        // 必须是双向好友关系
        {

            TimFriend myFriend = timFriendService.getByUserId(sessionUser.getId(), envelope.getUserId());
            if (myFriend == null) {
                return RestResponse.failed(ResponseCode.NOT_YOUR_FRIEND);
            }
            TimFriend receiveFriend = timFriendService.getByUserId(envelope.getUserId(), sessionUser.getId());
            if (receiveFriend == null) {
                return RestResponse.failed(ResponseCode.NOT_YOUR_FRIEND);
            }
        }

        // 修改红包状态
        LocalDateTime now = LocalDateTime.now();
        boolean updated = lambdaUpdate()
                .eq(PersonalRedEnvelope::getId, envelope.getId())
                .eq(PersonalRedEnvelope::getStatus, PersonalRedEnvelopeStatusEnum.UN_RECEIVED)
                .set(PersonalRedEnvelope::getStatus, PersonalRedEnvelopeStatusEnum.RECEIVED)
                .set(PersonalRedEnvelope::getReceiveTime, now)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        envelope.setStatus(PersonalRedEnvelopeStatusEnum.RECEIVED);
        envelope.setReceiveTime(now);

        // 我方加钱
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());
        portalUserService.addBalanceWithReportDate(sessionUser.getId(), envelope.getAmount(), envelope.getOrderNum(),
                UserBillTypeEnum.PERSONAL_RED_ENVELOPE_RECEIVE, null, reportDate, true);

        // IM发送单聊消息(如果发送失败，回滚数据)
        {
            // 封装好的消息
            PersonalRedEnvelopePortalVO vo = new PersonalRedEnvelopePortalVO(envelope);
            String fromAccount = sessionUser.getUsername();
            String toAccount = UserUtil.getUsernameByIdFromLocal(envelope.getUserId(), PortalTypeEnum.PORTAL);

            // 发送自定义消息
            TiMsgCustomItem tiMsgCustomItem = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_C2C_RECEIVE.getVal(), vo, fromAccount);
            TiMessage tiMessage = new TiMessage(fromAccount, toAccount, tiMsgCustomItem);
            RestResponse<TiSingleMessageSendResult> rsp = tiSingleChatService.sendMessage(tiMessage);
            if (!rsp.isOkRsp()) {
                // 如果发送失败，回滚数据
                throw new ImException(rsp);
            }
            //更新发红包消息的状态
            TiMsgCustomItem item = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_C2C.getVal(), vo, fromAccount);
            TiModifyC2cMessageParam modifyC2cMessageParam = new TiModifyC2cMessageParam(fromAccount, toAccount, envelope.getMsgKey(), item);

            tiSingleChatService.modifyC2cMsg(modifyC2cMessageParam);
        }

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse refundForPortal(PortalSessionUser sessionUser, IdParam param) {
        // 获取红包
        PersonalRedEnvelope envelope = lambdaQuery()
                .eq(PersonalRedEnvelope::getId, param.getId())
                .eq(PersonalRedEnvelope::getReceiveUserId, sessionUser.getId())
                .one();
        if (envelope == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }
        if (envelope.getStatus() == PersonalRedEnvelopeStatusEnum.EXPIRED) {
            return RestResponse.failed(ResponseCode.ENVELOPE_EXPIRED);
        }
        if (envelope.getStatus() == PersonalRedEnvelopeStatusEnum.RECEIVED) {
            return RestResponse.failed(ResponseCode.ENVELOPE_RECEIVED);
        }
        if (envelope.getStatus() != PersonalRedEnvelopeStatusEnum.UN_RECEIVED) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        // 红包不能是过期状态
        if (envelope.getExpireTime().isBefore(LocalDateTime.now())) {
            return RestResponse.failed(ResponseCode.ENVELOPE_EXPIRED);
        }

        // 必须是双向好友关系
        {

            TimFriend myFriend = timFriendService.getByUserId(sessionUser.getId(), envelope.getUserId());
            if (myFriend == null) {
                return RestResponse.failed(ResponseCode.NOT_YOUR_FRIEND);
            }
            TimFriend receiveFriend = timFriendService.getByUserId(envelope.getUserId(), sessionUser.getId());
            if (receiveFriend == null) {
                return RestResponse.failed(ResponseCode.NOT_YOUR_FRIEND);
            }
        }

        // 修改红包状态
        LocalDateTime now = LocalDateTime.now();
        boolean updated = lambdaUpdate()
                .eq(PersonalRedEnvelope::getId, envelope.getId())
                .eq(PersonalRedEnvelope::getStatus, PersonalRedEnvelopeStatusEnum.UN_RECEIVED)
                .set(PersonalRedEnvelope::getStatus, PersonalRedEnvelopeStatusEnum.REFUND)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        envelope.setStatus(PersonalRedEnvelopeStatusEnum.REFUND);

        // 对方退钱
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());
        portalUserService.addBalanceWithReportDate(envelope.getUserId(), envelope.getAmount(), envelope.getOrderNum(),
                UserBillTypeEnum.PERSONAL_RED_ENVELOPE_REFUND, null, reportDate, true);

        // IM发送单聊消息(如果发送失败，回滚数据)
        {
            // 封装好的消息
            PersonalRedEnvelopePortalVO vo = new PersonalRedEnvelopePortalVO(envelope);
            String fromAccount = sessionUser.getUsername();
            String toAccount = UserUtil.getUsernameByIdFromLocal(envelope.getUserId(), PortalTypeEnum.PORTAL);

            // 退回红包自定义消息
            TiMsgCustomItem tiMsgCustomItem = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_C2C_REFUND.getVal(), vo, fromAccount);
            TiMessage tiMessage = new TiMessage(fromAccount, toAccount, tiMsgCustomItem);
            RestResponse rsp = tiSingleChatService.sendMessage(tiMessage);
            if (!rsp.isOkRsp()) {
                // 如果发送失败，回滚数据
                throw new ImException(rsp);
            }
            //更新发红包消息的状态
            TiMsgCustomItem item = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_C2C.getVal(), vo, fromAccount);
            TiModifyC2cMessageParam modifyC2cMessageParam = new TiModifyC2cMessageParam(fromAccount, toAccount, envelope.getMsgKey(), item);

            tiSingleChatService.modifyC2cMsg(modifyC2cMessageParam);
        }

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkExpired() {
        // 列出过期红包，一次只拿1000条

        List<PersonalRedEnvelope> list = lambdaQuery()
                .eq(PersonalRedEnvelope::getStatus, PersonalRedEnvelopeStatusEnum.UN_RECEIVED)
                .lt(PersonalRedEnvelope::getExpireTime, LocalDateTime.now())
                .last("limit 1000")
                .list();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        // 退回金额
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        for (PersonalRedEnvelope envelope : list) {
            // 修改红包状态
            boolean updated = lambdaUpdate()
                    .eq(PersonalRedEnvelope::getId, envelope.getId())
                    .eq(PersonalRedEnvelope::getStatus, envelope.getStatus())
                    .set(PersonalRedEnvelope::getStatus, PersonalRedEnvelopeStatusEnum.EXPIRED)
                    .update();
            if (!updated) {
                continue;
            }
            envelope.setStatus(PersonalRedEnvelopeStatusEnum.EXPIRED);

            // 我方加钱
            String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(LocalDateTime.now(), reportConfig.getOffsetTime());
            portalUserService.addBalanceWithReportDate(envelope.getUserId(), envelope.getAmount(), envelope.getOrderNum(),
                    UserBillTypeEnum.PERSONAL_RED_ENVELOPE_EXPIRE, null, reportDate, true);

            // IM发送单聊消息(如果发送失败，回滚数据)
            {
                // 封装好的消息
                PersonalRedEnvelopePortalVO vo = new PersonalRedEnvelopePortalVO(envelope);

                // 这里的效果就是接受红包的人给发红包的人发了一条退回的消息
                String toAccount = UserUtil.getUsernameByIdFromLocal(envelope.getUserId(), PortalTypeEnum.PORTAL);
                String fromAccount = UserUtil.getUsernameByIdFromLocal(envelope.getReceiveUserId(), PortalTypeEnum.PORTAL);

                // 发送自定义消息
                TiMsgCustomItem tiMsgCustomItem = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_C2C_EXPIRED.getVal(), vo, fromAccount);
                TiMessage tiMessage = new TiMessage(fromAccount, toAccount, tiMsgCustomItem);
                RestResponse rsp = tiSingleChatService.sendMessage(tiMessage);
                if (!rsp.isOkRsp()) {
                    // 如果发送失败，回滚数据
                    throw new ImException(rsp);
                }
                //更新发红包消息的状态
                TiMsgCustomItem item = new TiMsgCustomItem(CustomMessageTypeEnum.RED_PACKET_C2C.getVal(), vo, fromAccount);
                TiModifyC2cMessageParam modifyC2cMessageParam = new TiModifyC2cMessageParam(fromAccount, toAccount, envelope.getMsgKey(), item);

                tiSingleChatService.modifyC2cMsg(modifyC2cMessageParam);
            }
        }
    }
}
