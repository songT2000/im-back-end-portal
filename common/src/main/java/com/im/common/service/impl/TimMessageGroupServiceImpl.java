package com.im.common.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.MsgTypeEnum;
import com.im.common.entity.tim.*;
import com.im.common.mapper.TimMessageGroupMapper;
import com.im.common.param.TimGroupMessagePortalPageParam;
import com.im.common.param.TimGroupSystemMessageSendParam;
import com.im.common.param.TimMessageGroupWithdrawParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupMemberService;
import com.im.common.service.TimGroupService;
import com.im.common.service.TimMessageGroupElemRelService;
import com.im.common.service.TimMessageGroupService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.TimMessageUtil;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMessageQueryParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMessageWithdrawParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupSystemNotificationParam;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMessageHistoryResult;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMessageResult;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMessageWithdrawResult;
import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.TimMessageGroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimMessageGroupServiceImpl extends MyBatisPlusServiceImpl<TimMessageGroupMapper, TimMessageGroup> implements TimMessageGroupService {

    private TimMessageGroupElemRelService timMessageGroupElemRelService;
    private TimGroupService timGroupService;
    private TiGroupService tiGroupService;
    private PortalUserCache portalUserCache;
    private TimGroupMemberService timGroupMemberService;
    private TimMessageGroupMapper timMessageGroupMapper;

    @Autowired
    public void setTimMessageGroupElemRelService(TimMessageGroupElemRelService timMessageGroupElemRelService) {
        this.timMessageGroupElemRelService = timMessageGroupElemRelService;
    }

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @Autowired
    public void setTiGroupService(TiGroupService tiGroupService) {
        this.tiGroupService = tiGroupService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setTimGroupMemberService(TimGroupMemberService timGroupMemberService) {
        this.timGroupMemberService = timGroupMemberService;
    }

    @Autowired
    public void setTimMessageGroupMapper(TimMessageGroupMapper timMessageGroupMapper) {
        this.timMessageGroupMapper = timMessageGroupMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(TimMessageGroup entity, List<TiMsgBody> msgBodies) {
        entity.setId(IdWorker.getId());
        save(entity);
        //一般消息元素只有一条（目前好像腾讯IM也不支持多条）
        for (TiMsgBody msgBody : msgBodies) {
            Object elem = TimMessageUtil.convertElem(msgBody);
            timMessageGroupElemRelService.saveGroupMessageElem(msgBody.getMsgType(), entity.getId(), elem);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByGroupId(String groupId) {
        //todo 待核实是否腾讯IM解散群组后，会删除所有聊天记录
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync() {
        List<TimGroup> groups = timGroupService.lambdaQuery().select(TimGroup::getGroupId).list();
        if (CollectionUtil.isEmpty(groups)) {
            return;
        }
        List<String> groupIds = groups.stream().map(TimGroup::getGroupId).collect(Collectors.toList());
        for (String groupId : groupIds) {
            sync(groupId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync(String groupId) {
        //查询两天前的最大msgSeq，作为起始点（最长撤回时间是1天）
        LocalDateTime dateTime = LocalDateTimeUtil.of(DateUtil.offsetDay(new Date(), -2));
        LambdaQueryWrapper<TimMessageGroup> queryWrapper = new LambdaQueryWrapper<TimMessageGroup>().eq(TimMessageGroup::getGroupId, groupId)
                .lt(TimMessageGroup::getSendTime, dateTime)
                .orderByDesc(TimMessageGroup::getMsgSeq);
        Page<TimMessageGroup> page = page(new Page<>(0, 1), queryWrapper);

        Long msgSeq = CollectionUtil.isEmpty(page.getRecords()) ? 0 : page.getRecords().get(0).getMsgSeq();
        Long reqMsgSeq = null;
        Set<TiGroupMessageResult> set = new HashSet<>();
        while (true) {
            List<TiGroupMessageResult> msgList = getFromRestApi(groupId, reqMsgSeq);
            if (CollectionUtil.isEmpty(msgList)) {
                break;
            }
            set.addAll(msgList);
            reqMsgSeq = msgList.get(msgList.size() - 1).getMsgSeq();
            if (msgList.size() < TiGroupMessageQueryParam.pageSize) {
                break;
            }
            if (reqMsgSeq <= msgSeq) {
                break;
            }
        }
        //过滤系统消息
        List<TiGroupMessageResult> results = set.stream().filter(p -> p.getIsSystemMsg() == null).sorted(Comparator.comparingLong(TiGroupMessageResult::getMsgSeq)).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(results)) {
            return;
        }
        //删除所有已撤回的消息
        List<Long> deletedMsgSeq = results.stream().filter(p -> p.getIsPlaceMsg() != 0).map(TiGroupMessageResult::getMsgSeq).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(deletedMsgSeq)) {
            List<TimMessageGroup> list = lambdaQuery().in(TimMessageGroup::getMsgSeq, deletedMsgSeq)
                    .eq(TimMessageGroup::getGroupId, groupId).list();
            List<Long> messageIds = list.stream().map(TimMessageGroup::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(messageIds)) {
                removeByIds(messageIds);
                //删除关联消息元素
                timMessageGroupElemRelService.deleteByMessageIds(messageIds);
            }
        }

        //查询本地消息
        List<TimMessageGroup> localGroupMessages = lambdaQuery().ge(TimMessageGroup::getMsgSeq, results.get(0).getMsgSeq()).eq(TimMessageGroup::getGroupId, groupId).list();
        List<TiGroupMessageResult> needInsertList;
        //对比本地和腾讯IM得到的数据，匹配出需要新增的数据
        if (CollectionUtil.isEmpty(localGroupMessages)) {
            needInsertList = results.stream().filter(p -> p.getIsPlaceMsg() == 0).collect(Collectors.toList());
        } else {
            List<Long> localMsgSeq = localGroupMessages.stream().map(TimMessageGroup::getMsgSeq).collect(Collectors.toList());
            List<Long> serverMsgSeq = results.stream().filter(p -> p.getIsPlaceMsg() == 0).map(TiGroupMessageResult::getMsgSeq).collect(Collectors.toList());
            serverMsgSeq.removeAll(localMsgSeq);
            needInsertList = results.stream().filter(p -> serverMsgSeq.contains(p.getMsgSeq())).collect(Collectors.toList());
        }
        if (CollectionUtil.isNotEmpty(needInsertList)) {
            //执行新增
            List<TimMessageGroup> groupMessages = needInsertList.stream().map(p -> {
                TimMessageGroup message = new TimMessageGroup();
                message.setGroupId(groupId);
                message.setFromUserId(portalUserCache.getIdByUsernameFromLocal(p.getFromAccount()));
                message.setMsgRandom(p.getMsgRandom());
                message.setMsgSeq(p.getMsgSeq());
                message.setSendTime(LocalDateTimeUtil.of(p.getMsgTimeStamp() * 1000));
                return message;
            }).collect(Collectors.toList());

            saveBatch(groupMessages);
            //保存消息元素
            List<TimMessageElemBo> list = new ArrayList<>();
            for (TimMessageGroup groupMessage : groupMessages) {
                TiGroupMessageResult result = needInsertList.stream().filter(p -> p.getMsgSeq().equals(groupMessage.getMsgSeq())).findAny().orElse(null);
                if (result == null) {
                    continue;
                }
                for (TiMsgBody msgBody : result.getMsgBody()) {
                    if (msgBody == null) {
                        continue;
                    }
                    Object elem = TimMessageUtil.convertElem(msgBody);
                    list.add(new TimMessageElemBo(groupMessage.getId(), elem, msgBody.getMsgType()));
                }
            }
            timMessageGroupElemRelService.batchSaveGroupMessageElem(list);
        }
    }

    private List<TiGroupMessageResult> getFromRestApi(String groupId, Long reqMsgSeq) {
        TiGroupMessageQueryParam param = new TiGroupMessageQueryParam(groupId, reqMsgSeq);
        RestResponse<TiGroupMessageHistoryResult> restResponse = tiGroupService.getGroupMessageHistory(param);
        return restResponse.getSuccess() ? restResponse.getData().getMsgList() : null;
    }

    @Override
    public Page<TimMessageGroup> getForImport(String groupId, LocalDateTime startTime, int current, int pageSize) {
        LambdaQueryWrapper<TimMessageGroup> queryWrapper = new LambdaQueryWrapper<TimMessageGroup>()
                .gt(TimMessageGroup::getSendTime, startTime)
                .eq(TimMessageGroup::getGroupId, groupId).orderByAsc(TimMessageGroup::getSendTime);
        Page<TimMessageGroup> page = page(new Page<>(current, pageSize), queryWrapper);
        List<TimMessageGroup> records = page.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            List<Long> messageIds = records.stream().map(TimMessageGroup::getId).collect(Collectors.toList());
            List<TimMessageBody> bodies = timMessageGroupElemRelService.getByIds(messageIds);
            for (TimMessageBody body : bodies) {
                for (TimMessageGroup record : records) {
                    if (record.getId().equals(body.getMessageId())) {
                        record.getMsgBody().add(body);
                    }
                }
            }
        }
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse withdraw(TimMessageGroupWithdrawParam param) {

        //腾讯IM只能撤回一天内的消息
        List<TimMessageGroup> list = lambdaQuery().eq(TimMessageGroup::getGroupId, param.getGroupId())
                .in(TimMessageGroup::getMsgSeq, param.getMsgSeqList())
                .list();
        LocalDateTime yesterday = LocalDateTimeUtil.offset(LocalDateTime.now(), -1, ChronoUnit.DAYS);
        for (TimMessageGroup messageGroup : list) {
            if (LocalDateTimeUtil.getTimestampOfDateTime(messageGroup.getSendTime()) < LocalDateTimeUtil.getTimestampOfDateTime(yesterday)) {
                return RestResponse.failed(ResponseCode.MESSAGE_CAN_NOT_WITHDRAW);
            }
        }

        List<TiGroupMessageWithdrawParam.MsgSeq> msgSeqList = param.getMsgSeqList().stream().map(TiGroupMessageWithdrawParam.MsgSeq::new).collect(Collectors.toList());
        RestResponse<TiGroupMessageWithdrawResult> restResponse = tiGroupService.withdraw(new TiGroupMessageWithdrawParam(param.getGroupId(), msgSeqList));
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        TiGroupMessageWithdrawResult data = restResponse.getData();
        for (TiGroupMessageWithdrawResult.RecallRet recallRet : data.getRecallRets()) {
            if (recallRet.getRetCode() == 0) {
                LambdaQueryWrapper<TimMessageGroup> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TimMessageGroup::getGroupId, param.getGroupId())
                        .eq(TimMessageGroup::getMsgSeq, recallRet.getMsgSeq());
                remove(queryWrapper);
            }
        }
        return RestResponse.ok(data);
    }

    @Transactional
    @Override
    public void deleteLocalMessage(TimMessageGroupWithdrawParam param) {
        LambdaQueryWrapper<TimMessageGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimMessageGroup::getGroupId, param.getGroupId())
                .in(TimMessageGroup::getMsgSeq, param.getMsgSeqList());
        List<TimMessageGroup> list = list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            remove(queryWrapper);
            List<Long> messageIds = list.stream().map(TimMessageGroup::getId).collect(Collectors.toList());
            timMessageGroupElemRelService.deleteByMessageIds(messageIds);
        }
    }

    @Override
    public RestResponse sendGroupSystemNotification(TimGroupSystemMessageSendParam param) {
        TiGroupSystemNotificationParam p = new TiGroupSystemNotificationParam(param.getGroupId(), param.getContent());
        RestResponse restResponse = tiGroupService.sendGroupSystemNotification(p);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserHistoryMessage(Long userId) {

        String fromAccount = portalUserCache.getUsernameByIdFromLocal(userId);
        List<TimGroupMember> list = timGroupMemberService.lambdaQuery().eq(TimGroupMember::getUserId, userId).list();
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> groupIds = list.stream().map(TimGroupMember::getGroupId).collect(Collectors.toList());
            for (String groupId : groupIds) {
                tiGroupService.deleteGroupMsgBySender(groupId, fromAccount);
            }
            LambdaQueryWrapper<TimMessageGroup> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TimMessageGroup::getFromUserId, userId);
            remove(queryWrapper);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PageVO<TimMessageGroupVO> pageFormPortal(TimGroupMessagePortalPageParam param) {
        // 查询
        if(StrUtil.isNotBlank(param.getKeyword())){
            //keyword不为空的情况下，只搜索文本消息
            param.setMsgType(MsgTypeEnum.TIMTextElem.getVal());
        }
        Page<TimMessageGroup> page = param.toPage();
        List<TimMessageGroup> list = timMessageGroupMapper.pageVOForPortal(page, param);

        if (CollectionUtil.isEmpty(list)) {
            return new PageVO<>(param);
        }

        // 组装额外数据
        List<TimMessageGroupVO> vos = combineMessageExtraDataForPortal(list);

        // 手动封装分页
        return new PageVO<>(page, vos);
    }

    private List<TimMessageGroupVO> combineMessageExtraDataForPortal(List<TimMessageGroup> list) {
        List<TimMessageGroupVO> records = list.stream().map(TimMessageGroupVO::new).collect(Collectors.toList());
        List<Long> messageIds = records.stream().map(TimMessageGroupVO::getId).collect(Collectors.toList());
        List<TimMessageBody> allTimMessageBodies = timMessageGroupElemRelService.getByIds(messageIds);
        TimGroup timGroup = timGroupService.getByGroupId(list.get(0).getGroupId());
        for (TimMessageGroupVO record : records) {
            List<TimMessageBody> items = allTimMessageBodies.stream()
                    .filter(p -> p.getMessageId().equals(record.getId()))
                    .map(TimMessageUtil::customMessageConvert)
                    .collect(Collectors.toList());
            record.setGroupName(timGroup.getGroupName());
            record.setMsgBody(items);
        }

        return records;
    }
}
