package com.im.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.*;
import com.im.common.entity.tim.TimGroup;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.mapper.TimGroupMapper;
import com.im.common.param.TimGroupAddParam;
import com.im.common.param.TimGroupCustomMessageSendParam;
import com.im.common.param.TimGroupEditParam;
import com.im.common.param.TimGroupMemberShutUpParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupMemberService;
import com.im.common.service.TimGroupService;
import com.im.common.service.TimMessageGroupService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupCreateParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupIdQueryParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMemberImportParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupUpdateParam;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgCustomItem;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.group.*;
import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.TimGroupVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TimGroupServiceImpl extends MyBatisPlusServiceImpl<TimGroupMapper, TimGroup> implements TimGroupService {

    private TimGroupMemberService timGroupMemberService;
    private TiGroupService tiGroupService;
    private PortalUserCache portalUserCache;
    private TimMessageGroupService timMessageGroupService;
    private CacheProxy cacheProxy;

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setTimGroupMemberService(TimGroupMemberService timGroupMemberService) {
        this.timGroupMemberService = timGroupMemberService;
    }

    @Autowired
    public void setTiGroupService(TiGroupService tiGroupService) {
        this.tiGroupService = tiGroupService;
    }

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    public TimGroup getByGroupId(String groupId) {
        return lambdaQuery().eq(TimGroup::getGroupId, groupId).one();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(TimGroupAddParam param) {

        TimGroup timGroup = new TimGroup();
        BeanUtil.copyProperties(param, timGroup);

        save(timGroup);

        List<TimGroupMember> members = param.getMemberIds().stream().map(memberId ->
                new TimGroupMember(memberId, timGroup.getGroupId())).collect(Collectors.toList());
        TimGroupMember owner = new TimGroupMember();
        owner.setGroupId(timGroup.getGroupId());
        owner.setRole(GroupMemberRoleEnum.Owner);
        owner.setJoinTime(LocalDateTime.now());
        owner.setUserId(param.getOwnerUserId());
        members.add(0, owner);
        timGroupMemberService.saveBatch(members);

        //同步到腾讯云IM
        String ownerAccount = portalUserCache.getUsernameByIdFromLocal(param.getOwnerUserId());
        List<String> memberAccounts = param.getMemberIds().stream().map(memberId ->
                portalUserCache.getUsernameByIdFromLocal(memberId)).collect(Collectors.toList());
        TiGroupCreateParam tiGroupCreateParam = new TiGroupCreateParam(timGroup, ownerAccount, memberAccounts);
        tiGroupService.createGroup(tiGroupCreateParam);

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_GROUP);
    }

    @Override
    public RestResponse edit(TimGroupEditParam param) {
        TiGroupUpdateParam p = new TiGroupUpdateParam(param);
        RestResponse<TiBaseResult> restResponse = tiGroupService.updateGroup(p);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }

        // 修改
        boolean updated = lambdaUpdate()
                .eq(TimGroup::getGroupId, param.getGroupId())
                .set(TimGroup::getGroupName, param.getGroupName())
                .set(TimGroup::getIntroduction, param.getIntroduction())
                .set(TimGroup::getNotification, param.getNotification())
                .set(TimGroup::getFaceUrl, param.getFaceUrl())
                .set(TimGroup::getShutUpState, param.getShutUpState())
                .set(TimGroup::getShowMemberEnabled, param.getShowMemberEnabled())
                .set(TimGroup::getUploadEnabled, param.getUploadEnabled())
                .set(TimGroup::getAddMemberEnabled, param.getAddMemberEnabled())
                .set(TimGroup::getAddFriendEnabled, param.getAddFriendEnabled())
                .set(TimGroup::getAnonymousChatEnabled, param.getAnonymousChatEnabled())
                .set(TimGroup::getExitEnabled, param.getExitEnabled())
                .set(TimGroup::getMsgIntervalTime, param.getMsgIntervalTime())
                .update();
        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_GROUP);

        sendGroupSettingCustomMsgToTencent(param.getGroupId(), CommonConstant.ADMINISTRATOR);

        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void destroyLocal(String groupId) {
        TimGroup timGroup = getByGroupId(groupId);
        if (timGroup == null) {
            return;
        }
        //删除群组
        removeById(timGroup.getId());

        //删除群成员
        timGroupMemberService.removeByGroupId(groupId);

        //删除所有聊天记录
        timMessageGroupService.removeByGroupId(groupId);

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_GROUP);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse destroyForAdmin(String groupId) {
        RestResponse restResponse = tiGroupService.destroy(groupId);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        destroyLocal(groupId);
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse shutUpAllMember(String groupId, boolean enable) {
        TiGroupUpdateParam param = new TiGroupUpdateParam();
        param.setGroupId(groupId);
        param.setShutUpAllMember(enable ? OnOrOffEnum.ON : OnOrOffEnum.OFF);
        RestResponse<TiBaseResult> restResponse = tiGroupService.updateGroup(param);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getShutUpState, enable ? OnOrOffEnum.ON : OnOrOffEnum.OFF)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse updateShowMemberEnabled(String groupId, boolean enable, String opUsername) {
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getShowMemberEnabled, enable)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        sendGroupSettingCustomMsgToTencent(groupId, opUsername);
        return RestResponse.ok(new TimGroupVO(getByGroupId(groupId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse updateAnonymousChatEnabled(String groupId, boolean enable, String opUsername) {
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getAnonymousChatEnabled, enable)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        sendGroupSettingCustomMsgToTencent(groupId, opUsername);
        return RestResponse.ok(new TimGroupVO(getByGroupId(groupId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse updateUploadEnabled(String groupId, boolean enable, String opUsername) {
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getUploadEnabled, enable)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        sendGroupSettingCustomMsgToTencent(groupId, opUsername);
        return RestResponse.ok(new TimGroupVO(getByGroupId(groupId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse updateAddMemberEnabled(String groupId, boolean enable, String opUsername) {
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getAddMemberEnabled, enable)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        sendGroupSettingCustomMsgToTencent(groupId, opUsername);
        return RestResponse.ok(new TimGroupVO(getByGroupId(groupId)));
    }

    @Override
    public RestResponse updateIntroduction(String groupId, String introduction, String opUsername) {
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getIntroduction, introduction)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        sendGroupSettingCustomMsgToTencent(groupId, opUsername);
        return RestResponse.ok(new TimGroupVO(getByGroupId(groupId)));
    }

    @Override
    public RestResponse updateNotification(String groupId, String notification, String opUsername) {
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getNotification, notification)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        sendGroupSettingCustomMsgToTencent(groupId, opUsername);
        return RestResponse.ok(new TimGroupVO(getByGroupId(groupId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse updateAddFriendEnabled(String groupId, boolean enable, String opUsername) {
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getAddFriendEnabled, enable)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        sendGroupSettingCustomMsgToTencent(groupId, opUsername);
        return RestResponse.ok(new TimGroupVO(getByGroupId(groupId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse updateExitEnabled(String groupId, boolean enable, String opUsername) {
        boolean updated = lambdaUpdate().eq(TimGroup::getGroupId, groupId)
                .set(TimGroup::getExitEnabled, enable)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        sendGroupSettingCustomMsgToTencent(groupId, opUsername);
        return RestResponse.ok(new TimGroupVO(getByGroupId(groupId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync() {

        List<String> groupIds = new ArrayList<>();
        long next = 0L;
        do {
            TiGroupIdResult result = getGroupIdFromRestApi(next);
            if (result == null) {
                return;
            }
            List<String> collect = result.getGroupIdList().stream().map(TiGroupIdResult.GroupIdListDTO::getGroupId).collect(Collectors.toList());
            groupIds.addAll(collect);
            next = result.getNext();
        } while (next != 0);
        if (CollectionUtil.isEmpty(groupIds)) {
            return;
        }

        //查询本地群组ID集合
        List<TimGroup> list = lambdaQuery().select(TimGroup::getGroupId).list();
        List<String> localGroupIds = list.stream().map(TimGroup::getGroupId).collect(Collectors.toList());
        //得到本地存在远程不存在的数据ID
        localGroupIds.removeAll(groupIds);
        if (CollectionUtil.isNotEmpty(localGroupIds)) {

            //删除已经解散的群组
            LambdaQueryWrapper<TimGroup> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(TimGroup::getGroupId, localGroupIds);
            remove(queryWrapper);
            //删除已经解散的群组成员
            LambdaQueryWrapper<TimGroupMember> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(TimGroupMember::getGroupId, localGroupIds);
            timGroupMemberService.remove(wrapper);
        }

        //由于群组有部分信息无法通过回调获取（比如群组成员更新自己在群内的昵称、设置群组成员身份等），需要定时通过rest api接口同步群组的数据
        //单次获取不能超过50个群组
        int batchSize = 50;
        if (groupIds.size() <= batchSize) {
            sync(groupIds);

            // 刷新缓存
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_GROUP);

            return;
        }

        for (int fromIdx = 0, endIdx = batchSize; ; fromIdx += batchSize, endIdx += batchSize) {
            if (endIdx > groupIds.size()) {
                endIdx = groupIds.size();
            }
            sync(groupIds.subList(fromIdx, endIdx));
            if (endIdx == groupIds.size()) {

                // 刷新缓存
                cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_GROUP);

                return;
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync(String groupId) {
        TimGroup timGroup = getByGroupId(groupId);
        if (timGroup != null) {
            return;
        }

        RestResponse<TiGroupResult> restResponse = tiGroupService.getGroupInfo(groupId);
        if (!restResponse.getSuccess() || CollectionUtil.isEmpty(restResponse.getData().getGroups())) {
            return;
        }
        TiGroup tiGroup = restResponse.getData().getGroups().get(0);
        //保存群组信息
        save(new TimGroup(tiGroup, portalUserCache));
        //保存成员信息
        List<TiGroupMember> members = tiGroup.getMembers();
        List<TimGroupMember> groupMembers = members.stream().map(p -> new TimGroupMember(p, groupId, portalUserCache)).collect(Collectors.toList());
        timGroupMemberService.saveBatch(groupMembers);

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_GROUP);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse copyCreateNewGroup(String groupId, String groupName) {
        TimGroup timGroup = getByGroupId(groupId);
        if (timGroup == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }
        //先同步，防止本地数据不完整
        sync(ListUtil.of(groupId));


        String ownerAccount = portalUserCache.getUsernameByIdFromLocal(timGroup.getOwnerUserId());
        //少于100人的群，可以直接携带成员一次创建
        String newGroupId = IdUtil.fastUUID();
        System.out.println(newGroupId);
        TiGroupCreateParam tiGroupCreateParam = new TiGroupCreateParam(newGroupId, timGroup, ownerAccount, groupName);
        tiGroupCreateParam.setIntroduction(timGroup.getIntroduction());
        tiGroupCreateParam.setNotification(timGroup.getNotification());
        tiGroupCreateParam.setFaceUrl(timGroup.getFaceUrl());
        RestResponse response = tiGroupService.createGroup(tiGroupCreateParam);
        //处理失败情况
        if (!response.isOkRsp()) {
            log.error("导入群组失败，数据：{},请检查", JSON.toJSONString(tiGroupCreateParam));
            return response;
        }
        //保存本地数据
        timGroup.setId(null);
        timGroup.setGroupId(newGroupId);
        timGroup.setGroupName(groupName);
        timGroup.setCreateTime(LocalDateTime.now());
        save(timGroup);


        //导入群组成员
        int current = 1;
        int pageSize = 100;
        while (true) {
            LambdaQueryWrapper<TimGroupMember> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TimGroupMember::getGroupId, groupId);
            Page<TimGroupMember> page = timGroupMemberService.page(new Page<>(current, pageSize), queryWrapper);
            List<TimGroupMember> records = page.getRecords();
            if (CollectionUtil.isNotEmpty(records)) {
                TiGroupMemberImportParam item = new TiGroupMemberImportParam();
                item.setGroupId(newGroupId);
                List<TiGroupMemberImportParam.Member> collect = records.stream().map(p -> {
                    TiGroupMemberImportParam.Member member = new TiGroupMemberImportParam.Member();
                    member.setMemberAccount(portalUserCache.getUsernameByIdFromLocal(p.getUserId()));
                    member.setRole(p.getRole() == GroupMemberRoleEnum.Admin ? p.getRole() : null);//只有admin能设置
                    return member;
                }).collect(Collectors.toList());
                item.setMembers(collect);
                RestResponse<TiGroupMemberImportResult> restResponse = tiGroupService.importGroupMember(item);
                //处理失败情况
                if (!restResponse.isOkRsp()) {
                    log.error("导入群组成员失败，数据：{},请检查", JSON.toJSONString(item));
                    return restResponse;
                }
                //保存成员信息
                for (TimGroupMember member : records) {
                    member.setId(null);
                    member.setGroupId(newGroupId);
                    member.setCreateTime(LocalDateTime.now());
                }
                timGroupMemberService.saveBatch(records);

            }
            if (!page.hasNext()) {
                break;
            }
            current++;
        }
        //如果是全局禁言状态，同步禁言状态
        if (timGroup.getShutUpState().equals(OnOrOffEnum.ON)) {
            shutUpAllMember(newGroupId, true);
        }

        List<TimGroupMember> members = timGroupMemberService.lambdaQuery().eq(TimGroupMember::getGroupId, groupId).list();
        //如果有成员是禁言状态，设置禁言时间
        for (TimGroupMember member : members) {
            if (member.getShutUpEndTime() != null && member.getShutUpEndTime().isAfter(LocalDateTime.now())) {
                TimGroupMemberShutUpParam param = new TimGroupMemberShutUpParam();
                param.setGroupId(newGroupId);
                param.setUserIds(ListUtil.of(member.getUserId()));
                long offsetTime = LocalDateTimeUtil.getTimestampOfDateTime(member.getShutUpEndTime()) - LocalDateTimeUtil.getTimestampOfDateTime(LocalDateTime.now());
                param.setShutUpTime(offsetTime / 1000);
                timGroupMemberService.shutUp(param);
            }
        }

        //发送群组创建自定义消息
        sendGroupCreateCustomMsgToTencent(newGroupId, ownerAccount);
        return RestResponse.ok(timGroup);
    }

    private TiGroupIdResult getGroupIdFromRestApi(Long next) {
        TiGroupIdQueryParam query = new TiGroupIdQueryParam();
        query.setNext(next);
        query.setGroupType(GroupTypeEnum.Public);
        RestResponse<TiGroupIdResult> restResponse = tiGroupService.getAllGroupIds(query);
        return restResponse.getSuccess() ? restResponse.getData() : null;
    }

    private void sync(List<String> groupIds) {
        //查询本地数据
        LambdaQueryWrapper<TimGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TimGroup::getGroupId, groupIds);
        List<TimGroup> timGroups = list(queryWrapper);

        RestResponse<TiGroupResult> restResponse = tiGroupService.getGroupInfo(groupIds);
        if (!restResponse.getSuccess()) {
            //获取失败
            return;
        }
        List<TiGroup> groups = restResponse.getData().getGroups();

        List<TimGroupMember> timGroupMembers = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(groups)) {
            for (TiGroup group : groups) {

                //群组信息
                TimGroup timGroup = new TimGroup(group, portalUserCache);

                Optional<TimGroup> any = timGroups.stream().filter(p -> p.getGroupId().equals(group.getGroupId())).findAny();
                //本地存在，更新
                if (any.isPresent()) {
                    lambdaUpdate().eq(TimGroup::getGroupId, group.getGroupId())
                            .set(TimGroup::getGroupName, timGroup.getGroupName())
                            .set(TimGroup::getOwnerUserId, timGroup.getOwnerUserId())
                            .set(TimGroup::getMaxMemberCount, timGroup.getMaxMemberCount())
                            .update();
                } else {
                    //本地不存在，新增
                    save(timGroup);
                }
                //成员信息
                List<TiGroupMember> members = group.getMembers();
                List<TimGroupMember> collect = members.stream().map(p -> new TimGroupMember(p, group.getGroupId(), portalUserCache)).collect(Collectors.toList());
                timGroupMembers.addAll(collect);
            }

            //先删除成员
            LambdaQueryWrapper<TimGroupMember> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(TimGroupMember::getGroupId, groupIds);
            timGroupMemberService.remove(wrapper);
            //保存成员信息
            timGroupMemberService.saveBatch(timGroupMembers);
        }
    }

    /**
     * 向腾讯IM发送群组配置变更的消息
     *
     * @param groupId 群组ID
     */
    private void sendGroupSettingCustomMsgToTencent(String groupId, String opUsername) {
        TimGroup timGroup = getByGroupId(groupId);
        TiMsgCustomItem item = new TiMsgCustomItem(CustomMessageTypeEnum.GROUP_SETTING.getVal(), new TimGroupVO(timGroup), opUsername);
        TimGroupCustomMessageSendParam sendParam = new TimGroupCustomMessageSendParam(groupId, item);
        sendParam.setForbidCallbackControl(CommonConstant.forbidCallbackControl);
        sendParam.setSendMsgControl(CommonConstant.sendMsgControl);
        sendParam.setFromAccount(opUsername);
        tiGroupService.sendCustomMessage(sendParam);
    }

    /**
     * 向腾讯IM发送群组创建的消息
     *
     * @param groupId 群组ID
     */
    private void sendGroupCreateCustomMsgToTencent(String groupId, String username) {
        TiMsgCustomItem item = new TiMsgCustomItem(CustomMessageTypeEnum.GROUP_CREATE.getVal(), CustomMessageTypeEnum.GROUP_CREATE.getStr(), username);
        TimGroupCustomMessageSendParam param = new TimGroupCustomMessageSendParam(groupId, item);
        param.setSendMsgControl(null);
        param.setFromAccount(username);
        tiGroupService.sendCustomMessage(param);
    }

}
