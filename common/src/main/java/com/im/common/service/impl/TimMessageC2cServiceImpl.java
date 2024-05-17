package com.im.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.C2cMsgFlagEnum;
import com.im.common.entity.enums.MsgTypeEnum;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.entity.tim.*;
import com.im.common.mapper.TimMessageC2cMapper;
import com.im.common.param.TimC2cMessagePortalPageParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.TimFriendService;
import com.im.common.service.TimMessageC2cElemRelService;
import com.im.common.service.TimMessageC2cService;
import com.im.common.service.TimUserLoginLogService;
import com.im.common.util.*;
import com.im.common.util.api.im.tencent.entity.param.message.TIMTextElem;
import com.im.common.util.api.im.tencent.entity.param.message.TiMessage;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.api.im.tencent.entity.param.message.TiWithdrawMessageParam;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageResult;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageSendResult;
import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.TimMessageC2cVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimMessageC2cServiceImpl extends MyBatisPlusServiceImpl<TimMessageC2cMapper, TimMessageC2c> implements TimMessageC2cService {

    private TimMessageC2cElemRelService timMessageC2cElemRelService;
    private TimFriendService timFriendService;
    private TiSingleChatService tiSingleChatService;
    private PortalUserCache portalUserCache;
    private TimUserLoginLogService timUserLoginLogService;
    private TimMessageC2cMapper timMessageC2cMapper;

    @Autowired
    public void setTimMessageC2cElemRelService(TimMessageC2cElemRelService timMessageC2cElemRelService) {
        this.timMessageC2cElemRelService = timMessageC2cElemRelService;
    }

    @Autowired
    public void setTimFriendService(TimFriendService timFriendService) {
        this.timFriendService = timFriendService;
    }

    @Autowired
    public void setTiSingleChatService(TiSingleChatService tiSingleChatService) {
        this.tiSingleChatService = tiSingleChatService;
    }

    @Autowired
    public void setTimUserLoginLogService(TimUserLoginLogService timUserLoginLogService) {
        this.timUserLoginLogService = timUserLoginLogService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setTimMessageC2cMapper(TimMessageC2cMapper timMessageC2cMapper) {
        this.timMessageC2cMapper = timMessageC2cMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(TimMessageC2c entity, List<TiMsgBody> msgBodies) {
        save(entity);
        //一般消息元素只有一条（目前好像腾讯IM也不支持多条
        for (TiMsgBody msgBody : msgBodies) {
            Object elem = TimMessageUtil.convertElem(msgBody);
            timMessageC2cElemRelService.saveC2cMessageElem(msgBody.getMsgType(), entity.getId(), elem);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdraw(Long fromUserId, Long toUserId, String msgKey) {
        TimMessageC2c one = lambdaQuery().eq(TimMessageC2c::getFromUserId, fromUserId)
                .eq(TimMessageC2c::getToUserId, toUserId)
                .eq(TimMessageC2c::getMsgKey, msgKey).one();
        if (one != null) {
            removeById(one.getId());
            timMessageC2cElemRelService.withdraw(one.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse withdrawForAdmin(Long fromUserId, Long toUserId, String msgKey) {
        //腾讯IM只能撤回一天内的消息
        TimMessageC2c one = lambdaQuery().eq(TimMessageC2c::getFromUserId, fromUserId)
                .eq(TimMessageC2c::getToUserId, toUserId)
                .eq(TimMessageC2c::getMsgKey, msgKey)
                .one();
        LocalDateTime yesterday = LocalDateTimeUtil.offset(LocalDateTime.now(), -1, ChronoUnit.DAYS);
        if (LocalDateTimeUtil.getTimestampOfDateTime(one.getSendTime()) < LocalDateTimeUtil.getTimestampOfDateTime(yesterday)) {
            return RestResponse.failed(ResponseCode.MESSAGE_CAN_NOT_WITHDRAW);
        }

        String fromAccount = portalUserCache.getUsernameByIdFromLocal(fromUserId);
        String toAccount = portalUserCache.getUsernameByIdFromLocal(toUserId);
        TiWithdrawMessageParam p = new TiWithdrawMessageParam(fromAccount, toAccount, msgKey);
        RestResponse restResponse = tiSingleChatService.withdraw(p);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }

        withdraw(fromUserId, toUserId, msgKey);
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync() {
        //同步昨天活跃过的用户单聊数据
        List<TimUserLoginLog> list = timUserLoginLogService.getByDate(LocalDate.parse(DateUtil.formatDate(DateUtil.yesterday())));
        //查询所有的好友对
        List<TimFriend> allFriends = timFriendService.list();
        if (CollUtil.isEmpty(allFriends) || CollUtil.isEmpty(list)) {
            return;
        }
        List<Long> userIds = list.stream().map(TimUserLoginLog::getUserId).collect(Collectors.toList());
        List<TimFriend> friends = allFriends.stream().filter(p -> userIds.contains(p.getFriendUserId()) || userIds.contains(p.getUserId())).collect(Collectors.toList());
        //一对好友只需要请求一次，使用用户id小到大以及下划线组合去重
        Set<String> set = new HashSet<>();
        for (TimFriend friend : friends) {
            if (friend.getUserId() > friend.getFriendUserId()) {
                set.add(friend.getFriendUserId() + StrUtil.UNDERLINE + friend.getUserId());
            } else {
                set.add(friend.getUserId() + StrUtil.UNDERLINE + friend.getFriendUserId());
            }
        }
        for (String string : set) {
            List<String> split = StrUtil.split(string, StrUtil.UNDERLINE);
            sync(Long.parseLong(split.get(0)), Long.parseLong(split.get(1)));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync(Long fromUserId, Long toUserId) {
        LocalDateTime startTime = LocalDateTimeUtil.of(DateUtil.offsetDay(new Date(), -7));
        LocalDateTime endTime = LocalDateTime.now();
        String fromAccount = portalUserCache.getUsernameByIdFromLocal(fromUserId);
        String toAccount = portalUserCache.getUsernameByIdFromLocal(toUserId);
        RestResponse<List<TiSingleMessageResult>> restResponse = tiSingleChatService.queryHistory(fromAccount, toAccount, startTime, endTime);
        if (!restResponse.isOkRsp() || CollUtil.isEmpty(restResponse.getData())) {
            return;
        }
        List<TiSingleMessageResult> data = restResponse.getData();

        List<String> deletedMsgKeyList = data.stream().filter(p -> p.getMsgFlagBits().equals(Integer.parseInt(C2cMsgFlagEnum.WITHDRAW.getVal())))
                .map(TiSingleMessageResult::getMsgKey).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(deletedMsgKeyList)) {
            //删除已撤回数据
            LambdaQueryWrapper<TimMessageC2c> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(TimMessageC2c::getToUserId, ListUtil.of(fromUserId, toUserId))
                    .in(TimMessageC2c::getFromUserId, ListUtil.of(fromUserId, toUserId))
                    .in(TimMessageC2c::getMsgKey, deletedMsgKeyList);
            remove(queryWrapper);
        }

        //查询本地数据
        List<TimMessageC2c> localMsgData = lambdaQuery().in(TimMessageC2c::getFromUserId, ListUtil.of(fromUserId, toUserId))
                .in(TimMessageC2c::getToUserId, ListUtil.of(fromUserId, toUserId))
                .gt(TimMessageC2c::getSendTime, startTime)
                .orderByDesc(TimMessageC2c::getSendTime).list();
        //远程数据
        List<TimMessageC2c> remoteMsgData = data.stream().filter(p -> p.getMsgFlagBits().equals(Integer.parseInt(C2cMsgFlagEnum.NORMAL.getVal())))
                .map(p -> {
                    TimMessageC2c messageC2c = new TimMessageC2c();
                    messageC2c.setFromUserId(portalUserCache.getIdByUsernameFromLocal(p.getFromAccount()));
                    messageC2c.setToUserId(portalUserCache.getIdByUsernameFromLocal(p.getToAccount()));
                    messageC2c.setMsgSeq(p.getMsgSeq());
                    messageC2c.setSendTime(LocalDateTimeUtil.of(p.getMsgTimeStamp() * 1000L));
                    messageC2c.setMsgKey(p.getMsgKey());
                    messageC2c.setMsgRandom(p.getMsgRandom());
                    return messageC2c;
                }).collect(Collectors.toList());
        if (CollUtil.isEmpty(remoteMsgData)) {
            return;
        }
        if (!CollUtil.isEmpty(localMsgData)) {
            remoteMsgData.removeAll(localMsgData);
        }

        if (CollUtil.isNotEmpty(remoteMsgData)) {
            saveBatch(remoteMsgData);
            //保存消息元素
            List<TimMessageElemBo> list = new ArrayList<>();
            for (TimMessageC2c messageC2c : remoteMsgData) {
                TiSingleMessageResult result = data.stream().filter(p -> messageC2c.getMsgKey().equals(p.getMsgKey())).findAny().orElse(null);
                if (result == null) {
                    continue;
                }
                for (TiMsgBody msgBody : result.getMsgBody()) {
                    Object elem = TimMessageUtil.convertElem(msgBody);
                    list.add(new TimMessageElemBo(messageC2c.getId(), elem, msgBody.getMsgType()));
                }
            }
            timMessageC2cElemRelService.batchSaveGroupMessageElem(list);
        }

    }

    @Override
    public Page<TimMessageC2c> getForImport(int current, int pageSize) {
        Page<TimMessageC2c> page = page(new Page<>(current, pageSize));
        List<TimMessageC2c> records = page.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<Long> messageIds = records.stream().map(TimMessageC2c::getId).collect(Collectors.toList());
            List<TimMessageBody> bodies = timMessageC2cElemRelService.getByIds(messageIds);
            for (TimMessageBody body : bodies) {
                for (TimMessageC2c record : records) {
                    if (record.getId().equals(body.getMessageId())) {
                        record.getMsgBody().add(body);
                    }
                }
            }
        }
        return page;
    }

    @Override
    public RestResponse sendTextMessageForAdmin(Long toUserId, String content) {
        TiMsgBody msgBody = new TiMsgBody(TiMsgTypeEnum.TIMTextElem, new TIMTextElem(content));
        String toAccount = portalUserCache.getUsernameByIdFromLocal(toUserId);
        TiMessage message = new TiMessage(toAccount, RandomUtil.randomInt(100000), ListUtil.of(msgBody));
        RestResponse<TiSingleMessageSendResult> restResponse = tiSingleChatService.sendMessage(message);

        if (!restResponse.isOkRsp()) {
            return restResponse;
        }

        //系统消息，暂不入库
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserHistoryMessage(Long userId) {
        int current = 1;
        int pageSize = 100;
        LambdaQueryWrapper<TimMessageC2c> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimMessageC2c::getFromUserId, userId);
        while (true) {
            Page<TimMessageC2c> page = page(new Page<>(current, pageSize), queryWrapper);
            List<TimMessageC2c> records = page.getRecords();
            if (CollectionUtil.isNotEmpty(records)) {
                for (TimMessageC2c record : records) {
                    String fromAccount = portalUserCache.getUsernameByIdFromLocal(record.getFromUserId());
                    String toAccount = portalUserCache.getUsernameByIdFromLocal(record.getToUserId());
                    TiWithdrawMessageParam param = new TiWithdrawMessageParam(fromAccount, toAccount, record.getMsgKey());
                    tiSingleChatService.withdraw(param);
                }
                List<Long> messageIds = records.stream().map(TimMessageC2c::getId).collect(Collectors.toList());
                removeByIds(messageIds);
            }
            if (!page.hasNext()) {
                break;
            }
            current++;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PageVO<TimMessageC2cVO> pageFormPortal(TimC2cMessagePortalPageParam param) {
        // 查询
        if (StrUtil.isNotBlank(param.getKeyword())) {
            //keyword不为空的情况下，只搜索文本消息
            param.setMsgType(MsgTypeEnum.TIMTextElem.getVal());
        }
        Page<TimMessageC2c> page = param.toPage();
        List<TimMessageC2c> list = timMessageC2cMapper.pageVOForPortal(page, param);

        if (CollectionUtil.isEmpty(list)) {
            return new PageVO<>(param);
        }

        // 组装额外数据
        List<TimMessageC2cVO> vos = combineMessageExtraDataForPortal(list);

        // 手动封装分页
        return new PageVO<>(page, vos);
    }

    private List<TimMessageC2cVO> combineMessageExtraDataForPortal(List<TimMessageC2c> list) {
        List<TimMessageC2cVO> records = list.stream().map(TimMessageC2cVO::new).collect(Collectors.toList());
        List<Long> messageIds = records.stream().map(TimMessageC2cVO::getId).collect(Collectors.toList());
        List<TimMessageBody> allTimMessageBodies = timMessageC2cElemRelService.getByIds(messageIds);
        for (TimMessageC2cVO record : records) {
            List<TimMessageBody> items = allTimMessageBodies.stream()
                    .filter(p -> p.getMessageId().equals(record.getId()))
                    .map(TimMessageUtil::customMessageConvert)
                    .collect(Collectors.toList());
            record.setMsgBody(items);
        }
        return records;
    }
}
