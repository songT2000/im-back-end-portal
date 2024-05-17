package com.im.common.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.PortalUser;
import com.im.common.entity.PortalUserProfile;
import com.im.common.entity.enums.ActionEnum;
import com.im.common.entity.enums.AddSourceTypeEnum;
import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.entity.tim.*;
import com.im.common.response.RestResponse;
import com.im.common.service.*;
import com.im.common.util.CollectionUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.TimMessageUtil;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendImportParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupImportParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMemberImportParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMessageImportParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMessageQueryParam;
import com.im.common.util.api.im.tencent.entity.param.message.TiMessageImportParam;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitParam;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendImportResult;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMemberImportResult;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMessageHistoryResult;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMessageImportResult;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMessageResult;
import com.im.common.util.api.im.tencent.error.ApiErrorMsgEnum;
import com.im.common.util.api.im.tencent.service.rest.*;
import com.im.common.vo.SwitchAppResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <pre>
 * 切换腾讯IM账号
 * 导入历史数据至即时通信 IM，包括：
 * <li>导入帐号</li>
 * <li>导入用户资料</li>
 * <li>导入用户关系链</li>
 * <li>导入单聊历史消息</li>
 * <li>导入群组数据</li>
 * <li>导入群聊历史消息</li>
 * </pre>
 */
@Slf4j
@Service
public class SwitchAppServiceImpl implements SwitchAppService {

    private PortalUserService portalUserService;
    private TimFriendService timFriendService;
    private TimMessageC2cService timMessageC2cService;
    private TimGroupService timGroupService;
    private TimGroupMemberService timGroupMemberService;
    private TimMessageGroupService timMessageGroupService;
    private PortalUserProfileService portalUserProfileService;
    private PortalUserCache portalUserCache;

    private TiAccountService tiAccountService;
    private TiAccountPortraitService tiAccountPortraitService;
    private TiFriendService tiFriendService;
    private TiSingleChatService tiSingleChatService;
    private TiGroupService tiGroupService;

    private TimUserDeviceStateService timUserDeviceStateService;

    private ValueOperations<String, String> valueOperations;
    private SetOperations<String, String> setOperations;
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    public void setTimUserDeviceStateService(TimUserDeviceStateService timUserDeviceStateService) {
        this.timUserDeviceStateService = timUserDeviceStateService;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setTimFriendService(TimFriendService timFriendService) {
        this.timFriendService = timFriendService;
    }

    @Autowired
    public void setTimMessageC2cService(TimMessageC2cService timMessageC2cService) {
        this.timMessageC2cService = timMessageC2cService;
    }

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }

    @Autowired
    public void setTiAccountService(TiAccountService tiAccountService) {
        this.tiAccountService = tiAccountService;
    }

    @Autowired
    public void setTiAccountPortraitService(TiAccountPortraitService tiAccountPortraitService) {
        this.tiAccountPortraitService = tiAccountPortraitService;
    }

    @Autowired
    public void setTiFriendService(TiFriendService tiFriendService) {
        this.tiFriendService = tiFriendService;
    }

    @Autowired
    public void setTiSingleChatService(TiSingleChatService tiSingleChatService) {
        this.tiSingleChatService = tiSingleChatService;
    }

    @Autowired
    public void setTiGroupService(TiGroupService tiGroupService) {
        this.tiGroupService = tiGroupService;
    }

    @Autowired
    public void setTimGroupMemberService(TimGroupMemberService timGroupMemberService) {
        this.timGroupMemberService = timGroupMemberService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setPortalUserProfileService(PortalUserProfileService portalUserProfileService) {
        this.portalUserProfileService = portalUserProfileService;
    }

    @Autowired
    public void setValueOperations(ValueOperations<String, String> valueOperations) {
        this.valueOperations = valueOperations;
    }

    @Autowired
    public void setSetOperations(SetOperations<String, String> setOperations) {
        this.setOperations = setOperations;
    }

    @Autowired
    public void setThreadPoolTaskScheduler(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @Override
    public void kickOutAll() {
        LambdaQueryWrapper<TimUserDeviceState> wrapper = new LambdaQueryWrapper<>();
        // 只查在线用户
        wrapper.eq(TimUserDeviceState::getAction, ActionEnum.Login);
        wrapper.orderByDesc(TimUserDeviceState::getCreateTime);
        List<TimUserDeviceState> list = timUserDeviceStateService.list(wrapper);
        for (TimUserDeviceState timUser : list) {
            String username = portalUserCache.getUsernameByIdFromLocal(timUser.getUserId());
            tiAccountService.kick(username);
        }
    }

    /**
     * 导入已有的账号
     */
    @Async
    @Override
    public void importAccount() {

        SwitchAppResultVO result = new SwitchAppResultVO();
        int success = 0;
        result.setTotal(portalUserService.count());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT.getVal(), result);

        int current = 1;
        int pageSize = 100;
        while (true) {
            Page<PortalUser> page = portalUserService.page(new Page<>(current, pageSize));
            List<PortalUser> records = page.getRecords();
            if (CollectionUtil.isNotEmpty(records)) {
                List<String> accounts = records.stream().map(PortalUser::getUsername).collect(Collectors.toList());
                RestResponse<Map<String, Boolean>> restResponse = tiAccountService.multiAccountImport(accounts);
                if (!restResponse.isOkRsp()) {
                    log.error("用户导入失败，返回结果：" + restResponse.getMessage());
                    result.setFail(true);
                    result.setMessage(restResponse.getMessage());
                    setValue(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT.getVal(), result);
                    break;
                }
                //处理失败情况
                Map<String, Boolean> data = restResponse.getData();
                for (String key : data.keySet()) {
                    if (data.get(key)) {
                        success++;
                    } else {
                        log.error("用户{}导入失败，请检查！！！", key);
                    }
                }
                success += accounts.size();
                result.setCurrent(success);
                setValue(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT.getVal(), result);
            }
            if (!page.hasNext()) {
                break;
            }
            current++;
        }
        result.setCurrent(result.getTotal());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT.getVal(), result);
    }

    /**
     * 设置账号信息
     */
    @Async
    @Override
    public void setAccountPortrait() {
        SwitchAppResultVO result = new SwitchAppResultVO();
        List<PortalUser> all = portalUserService.list();
        result.setTotal(all.size());
        Long size = setOperations.size(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT_SUCCESS.getVal());
        result.setCurrent(size == null ? 0 : size.intValue());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT.getVal(), result);

        List<PortalUserProfile> portalUserProfiles = portalUserProfileService.list();
        for (PortalUser user : all) {
            Boolean member = setOperations.isMember(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT_SUCCESS.getVal(), String.valueOf(user.getId()));
            if (Boolean.TRUE.equals(member)) {
                continue;
            }
            PortalUserProfile profile = portalUserProfiles.stream().filter(p -> p.getId().equals(user.getId())).findAny().orElse(null);
            TiAccountPortraitParam portrait = new TiAccountPortraitParam();
            portrait.setUsername(user.getUsername());
            portrait.setNickname(user.getNickname());
            portrait.setAvatar(user.getAvatar());
            if (profile != null) {
                portrait.setSex(profile.getSex());
                portrait.setBirthday(profile.getBirthday());
                portrait.setSelfSignature(profile.getSelfSignature());
            }
            portrait.setAddFriendEnabled(user.getAddFriendEnabled());
            portrait.setEnabled(user.getEnabled());
            //多线程执行
            threadPoolTaskScheduler.execute(() -> {
                RestResponse restResponse = tiAccountPortraitService.setPortrait(portrait);
                //处理失败情况
                if (!restResponse.isOkRsp()) {
                    log.error("用户{}资料导入失败，请检查！！！", user.getUsername());
                } else {
                    setOperations.add(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT_SUCCESS.getVal(), String.valueOf(user.getId()));
                    result.setCurrent(result.getCurrent() + 1);//不考虑线程安全，只是一个进度，无所谓
                    setValue(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT.getVal(), result);
                }

            });


        }
        size = setOperations.size(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT_SUCCESS.getVal());
        result.setCurrent(size == null ? 0 : size.intValue());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT.getVal(), result);
    }

    /**
     * 导入关系链数据
     */
    @Async
    @Override
    public void importFriendData() {

        SwitchAppResultVO result = new SwitchAppResultVO();
        List<TimFriend> list = timFriendService.list();
        Map<Long, List<TimFriend>> map = list.stream().collect(Collectors.groupingBy(TimFriend::getUserId));

        result.setTotal(map.size());
        Long size = setOperations.size(RedisKeyEnum.SWITCH_APP_IMPORT_FRIEND_SUCCESS.getVal());
        result.setCurrent(size == null ? 0 : size.intValue());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_FRIEND.getVal(), result);

        for (Long userId : map.keySet()) {

            Boolean member = setOperations.isMember(RedisKeyEnum.SWITCH_APP_IMPORT_FRIEND_SUCCESS.getVal(), String.valueOf(userId));
            if (Boolean.TRUE.equals(member)) {
                continue;
            }

            String account = portalUserCache.getUsernameByIdFromLocal(userId);
            TiFriendImportParam item = new TiFriendImportParam();
            item.setFromAccount(account);
            List<TimFriend> timFriends = map.get(userId);
            List<TiFriendImportParam.AddFriendItemDTO> items = timFriends.stream().map(p -> {
                TiFriendImportParam.AddFriendItemDTO dto = new TiFriendImportParam.AddFriendItemDTO();
                dto.setAddSource(p.getAddSource() == null ? AddSourceTypeEnum.Other : p.getAddSource());
                dto.setAddTime(LocalDateTimeUtil.getTimestampOfDateTime(p.getCreateTime()) / 1000L);
                dto.setRemark(StrUtil.isBlank(p.getAliasName()) ? null : p.getAliasName());
                dto.setGroupName(StrUtil.isBlank(p.getGroupName()) ? null : StrUtil.split(p.getGroupName(), StrUtil.COMMA));
                dto.setToAccount(portalUserCache.getUsernameByIdFromLocal(p.getFriendUserId()));
                return dto;
            }).collect(Collectors.toList());
            item.setAddFriendItem(items);
            threadPoolTaskScheduler.execute(() -> {
                RestResponse<TiFriendImportResult> restResponse = tiFriendService.importFriend(item);
                //处理失败情况
                if (!restResponse.isOkRsp()) {
                    log.error("导入关系链失败，数据：{}", JSON.toJSONString(item));
                } else {
                    setOperations.add(RedisKeyEnum.SWITCH_APP_IMPORT_FRIEND_SUCCESS.getVal(), String.valueOf(userId));
                    result.setCurrent(result.getCurrent() + 1);
                    setValue(RedisKeyEnum.SWITCH_APP_IMPORT_FRIEND.getVal(), result);
                }
            });
        }
        size = setOperations.size(RedisKeyEnum.SWITCH_APP_IMPORT_FRIEND_SUCCESS.getVal());
        result.setCurrent(size == null ? 0 : size.intValue());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_FRIEND.getVal(), result);
    }

    /**
     * 导入单聊消息
     */
    @Async
    @Override
    public void importC2cMessage() {

        SwitchAppResultVO result = new SwitchAppResultVO();
        result.setTotal(timMessageC2cService.count());
        Long size = setOperations.size(RedisKeyEnum.SWITCH_APP_IMPORT_C2C_MESSAGE_SUCCESS.getVal());
        result.setCurrent(size == null ? 0 : size.intValue());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_C2C_MESSAGE.getVal(), result);

        int current = 1;
        int pageSize = 100;
        while (true) {
            Page<TimMessageC2c> page = timMessageC2cService.getForImport(current, pageSize);
            List<TimMessageC2c> records = page.getRecords();
            if (CollectionUtil.isNotEmpty(records)) {
                for (TimMessageC2c messageC2c : records) {
                    if (CollectionUtil.isEmpty(messageC2c.getMsgBody())) {
                        result.setCurrent(result.getCurrent() + 1);
                        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_C2C_MESSAGE.getVal(), result);
                        continue;
                    }
                    Boolean member = setOperations.isMember(RedisKeyEnum.SWITCH_APP_IMPORT_C2C_MESSAGE_SUCCESS.getVal(), String.valueOf(messageC2c.getId()));
                    if (Boolean.TRUE.equals(member)) {
                        continue;
                    }
                    //采用多线程
                    threadPoolTaskScheduler.execute(() -> {
                        TiMessageImportParam messageImport = new TiMessageImportParam(messageC2c, portalUserCache);
                        RestResponse restResponse = tiSingleChatService.importMessage(messageImport);
                        //处理失败情况
                        if (!restResponse.isOkRsp()) {
                            log.error("导入单聊消息失败，数据：{},请检查", JSON.toJSONString(messageImport));
                        } else {
                            result.setCurrent(result.getCurrent() + 1);
                            setValue(RedisKeyEnum.SWITCH_APP_IMPORT_C2C_MESSAGE.getVal(), result);
                            setOperations.add(RedisKeyEnum.SWITCH_APP_IMPORT_C2C_MESSAGE_SUCCESS.getVal(), String.valueOf(messageC2c.getId()));
                        }

                    });
                }
            }

            if (!page.hasNext()) {
                break;
            }
            current++;
        }
    }

    /**
     * 同步群组信息
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importGroup() {

        SwitchAppResultVO result = new SwitchAppResultVO();
        result.setTotal(timGroupService.count());
        Long size = setOperations.size(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_SUCCESS.getVal());
        result.setCurrent(size == null ? 0 : size.intValue());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP.getVal(), result);

        int success = 0;
        int current = 1;
        int pageSize = 100;
        outer:
        while (true) {
            Page<TimGroup> page = timGroupService.page(new Page<>(current, pageSize));
            List<TimGroup> records = page.getRecords();
            if (CollectionUtil.isNotEmpty(records)) {
                for (TimGroup record : records) {

                    if (record.getGroupId().startsWith("@TGS#")) {
                        //腾讯IM系统生成的id，无法导入
                        success++;
                        result.setCurrent(success);
                        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP.getVal(), result);
                        continue;
                    }
                    Boolean member = setOperations.isMember(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_SUCCESS.getVal(), record.getGroupId());
                    if (Boolean.TRUE.equals(member)) {
                        continue;
                    }

                    TiGroupImportParam item = new TiGroupImportParam(record, portalUserCache);
                    RestResponse restResponse = tiGroupService.importGroup(item);
                    //处理失败情况
                    if (!restResponse.isOkRsp()) {
                        if (restResponse.getMessage().endsWith(ApiErrorMsgEnum.CODE_10021.getMsg())) {
                            //id重复，跳过
                            success++;
                            result.setCurrent(success);
                            setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP.getVal(), result);
                            setOperations.add(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_SUCCESS.getVal(), record.getGroupId());
                            continue;
                        }
                        log.error("导入群组失败，数据：{},请检查", JSON.toJSONString(item));
                        result.setFail(true);
                        result.setMessage(restResponse.getMessage());
                        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP.getVal(), result);
                        break outer;
                    }
                    success++;
                    result.setCurrent(success);
                    setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP.getVal(), result);
                    setOperations.add(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_SUCCESS.getVal(), record.getGroupId());
                }
            }
            if (!page.hasNext()) {
                break;
            }
            current++;
        }
        result.setCurrent(result.getTotal());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP.getVal(), result);
    }

    /**
     * 导入群组成员
     */
    @Async
    @Override
    public void importGroupMember() {

        SwitchAppResultVO result = new SwitchAppResultVO();
        result.setTotal(timGroupMemberService.count());
        Long size = setOperations.size(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER_SUCCESS.getVal());
        result.setCurrent(size == null ? 0 : size.intValue());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER.getVal(), result);

        int success = 0;
        List<TimGroup> list = timGroupService.lambdaQuery().select(TimGroup::getGroupId).list();
        outer:
        for (TimGroup timGroup : list) {
            if (timGroup.getGroupId().startsWith("@TGS#")) {
                //腾讯IM系统生成的id，无法导入
                LambdaQueryWrapper<TimGroupMember> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TimGroupMember::getGroupId, timGroup.getGroupId());
                int count = timGroupMemberService.count(queryWrapper);
                success += count;
                result.setCurrent(success);
                setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER.getVal(), result);
                continue;
            }
            Boolean isHave = setOperations.isMember(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER_SUCCESS.getVal(), timGroup.getGroupId());
            if (Boolean.TRUE.equals(isHave)) {
                continue;
            }
            //导入群组成员
            int current = 1;
            int pageSize = 100;
            while (true) {
                LambdaQueryWrapper<TimGroupMember> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TimGroupMember::getGroupId, timGroup.getGroupId());
                Page<TimGroupMember> page = timGroupMemberService.page(new Page<>(current, pageSize), queryWrapper);
                List<TimGroupMember> records = page.getRecords();
                if (CollectionUtil.isNotEmpty(records)) {
                    TiGroupMemberImportParam item = new TiGroupMemberImportParam();
                    item.setGroupId(timGroup.getGroupId());
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
                        result.setFail(true);
                        result.setMessage(restResponse.getMessage());
                        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER.getVal(), result);
                        break outer;
                    } else {
                        success += collect.size();
                        result.setCurrent(success);
                        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER.getVal(), result);
                        setOperations.isMember(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER_SUCCESS.getVal(), timGroup.getGroupId());
                    }

                }
                if (!page.hasNext()) {
                    break;
                }
                current++;
            }
            setOperations.add(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER_SUCCESS.getVal(), timGroup.getGroupId());
        }
        result.setCurrent(result.getTotal());
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER.getVal(), result);
    }

    /**
     * 导入群消息
     */
    @Async
    @Override
    public void importGroupMessage(int offsetDay) {
        SwitchAppResultVO result = new SwitchAppResultVO();
        LambdaQueryWrapper<TimMessageGroup> query = new LambdaQueryWrapper<TimMessageGroup>()
                .gt(TimMessageGroup::getSendTime, LocalDateTimeUtil.offset(LocalDateTime.now(), -offsetDay, ChronoUnit.DAYS));
        result.setTotal(timMessageGroupService.count(query));
        result.setCurrent(0);
        setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MESSAGE.getVal(), result);

        List<TimGroup> list = timGroupService.lambdaQuery().select(TimGroup::getGroupId).list();
        for (TimGroup timGroup : list) {
            threadPoolTaskScheduler.execute(() -> {
                if (!timGroup.getGroupId().startsWith("@TGS#")) {
                    LocalDateTime startTime = LocalDateTimeUtil.offset(LocalDateTime.now(), -offsetDay, ChronoUnit.DAYS);
                    //查询群组最后一条消息的时间，和startTime对比，如果没有，取startTime，如果有，取最后一条消息的时间，谁大取谁
                    RestResponse<TiGroupMessageHistoryResult> groupMessageHistory = tiGroupService.getGroupMessageHistory(new TiGroupMessageQueryParam(timGroup.getGroupId()));
                    List<TiGroupMessageResult> msgList = groupMessageHistory.getData().getMsgList();
                    if (CollectionUtil.isNotEmpty(msgList)) {
                        TiGroupMessageResult latestMsg = msgList.get(0);
                        if (LocalDateTimeUtil.getTimestampOfDateTime(startTime) < latestMsg.getMsgTimeStamp() * 1000) {
                            startTime = LocalDateTimeUtil.of(latestMsg.getMsgTimeStamp() * 1000);
                        }
                    }
                    int current = 1;
                    int pageSize = 7;
                    while (true) {

                        Page<TimMessageGroup> page = timMessageGroupService.getForImport(timGroup.getGroupId(), startTime, current, pageSize);
                        List<TimMessageGroup> records = page.getRecords();
                        if (CollectionUtil.isNotEmpty(records)) {
                            TiGroupMessageImportParam item = new TiGroupMessageImportParam();
                            item.setGroupId(timGroup.getGroupId());
                            List<TiGroupMessageImportParam.Message> messages = records.stream().map(p -> {
                                Boolean member = setOperations.isMember(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MESSAGE_SUCCESS.getVal(), String.valueOf(p.getId()));
                                if (Boolean.TRUE.equals(member)) {
                                    return null;
                                }

                                TiGroupMessageImportParam.Message message = new TiGroupMessageImportParam.Message();
                                message.setFromAccount(portalUserCache.getUsernameByIdFromLocal(p.getFromUserId()));
                                message.setSendTime(LocalDateTimeUtil.getTimestampOfDateTime(p.getSendTime()) / 1000L);
                                message.setRandom(p.getMsgRandom());
                                message.setMsgBody(p.getMsgBody().stream().map(q -> TimMessageUtil.convertTimElem(q.getMsgContent())).collect(Collectors.toList()));
                                return message;
                            }).filter(ObjectUtil::isNotNull).sorted(Comparator.comparing(TiGroupMessageImportParam.Message::getSendTime)).collect(Collectors.toList());
                            if (CollectionUtil.isNotEmpty(messages)) {
                                item.setMsgList(messages);
                                RestResponse<TiGroupMessageImportResult> restResponse = tiGroupService.importGroupMessage(item);
                                //处理失败情况
                                if (!restResponse.isOkRsp()) {
                                    log.error("导入群组消息失败，数据：{},请检查", JSON.toJSONString(item));
                                } else {
                                    List<String> messageIds = records.stream().map(TimMessageGroup::getId).map(String::valueOf).collect(Collectors.toList());
                                    for (String messageId : messageIds) {
                                        setOperations.add(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MESSAGE_SUCCESS.getVal(), messageId);
                                    }
                                }
                            }
                        }
                        if (!page.hasNext()) {
                            break;
                        }
                        current++;
                    }
                }
                LambdaQueryWrapper<TimMessageGroup> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TimMessageGroup::getGroupId, timGroup.getGroupId())
                        .gt(TimMessageGroup::getSendTime, LocalDateTimeUtil.offset(LocalDateTime.now(), -offsetDay, ChronoUnit.DAYS));
                int count = timMessageGroupService.count(queryWrapper);
                result.setCurrent(result.getCurrent() + count);
                setValue(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MESSAGE.getVal(), result);
            });
        }
    }

    @Override
    public RestResponse<SwitchAppResultVO> getProcess(String key) {
        return RestResponse.ok(getValue(key));
    }

    private void setValue(String key, SwitchAppResultVO bean) {
        valueOperations.set(key, JSON.toJSONString(bean), 1L, TimeUnit.DAYS);
    }

    private SwitchAppResultVO getValue(String key) {
        String s = valueOperations.get(key);
        if (StrUtil.isBlank(s)) {
            return null;
        }
        return JSON.parseObject(s, SwitchAppResultVO.class);
    }

}
