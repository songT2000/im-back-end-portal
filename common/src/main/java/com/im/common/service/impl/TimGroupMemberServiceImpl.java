package com.im.common.service.impl;

import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.mapper.TimGroupMemberMapper;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupMemberService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupAddMemberParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupDeleteMemberParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMemberSetRoleParam;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupShutUpParam;
import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.TimGroupMemberAddVO;
import com.im.common.vo.TimGroupMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TimGroupMemberServiceImpl extends MyBatisPlusServiceImpl<TimGroupMemberMapper, TimGroupMember> implements TimGroupMemberService {

    private TiGroupService tiGroupService;
    private PortalUserCache portalUserCache;

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
    public TimGroupMember getByGroupIdAndUserId(String groupId, Long userId) {
        return lambdaQuery().eq(TimGroupMember::getGroupId, groupId).eq(TimGroupMember::getUserId, userId).one();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void exit(String groupId, List<Long> memberUserIds) {
        LambdaUpdateWrapper<TimGroupMember> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TimGroupMember::getGroupId, groupId)
                .in(TimGroupMember::getUserId, memberUserIds);
        remove(updateWrapper);
    }

    @Lock4j(keys = {"#groupId"})
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void join(String groupId, List<Long> memberUserIds) {
        //先删再增，防止重复
        exit(groupId, memberUserIds);
        List<TimGroupMember> groupMembers = new ArrayList<>();
        for (Long memberUserId : memberUserIds) {
            groupMembers.add(new TimGroupMember(memberUserId, groupId));
        }
        saveBatch(groupMembers);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByGroupId(String groupId) {
        LambdaUpdateWrapper<TimGroupMember> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TimGroupMember::getGroupId, groupId);
        remove(updateWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void replace(String groupId, List<TimGroupMember> members) {
        removeByGroupId(groupId);
        saveBatch(members);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PageVO<TimGroupMemberVO> pageVOForAdmin(TimGroupMemberPageParam param) {
        // 查询
        Page<TimGroupMember> page = param.toPage();
        List<TimGroupMemberVO> list = getBaseMapper().pageVOForAdmin(page, param.getGroupId(), param.getUsername());

        if (CollectionUtil.isEmpty(list)) {
            return new PageVO<>(param);
        }
        for (TimGroupMemberVO vo : list) {
            vo.setNickname(portalUserCache.getNicknameByIdFromLocal(vo.getUserId()));
            vo.setUsername(portalUserCache.getUsernameByIdFromLocal(vo.getUserId()));
            vo.setAvatar(portalUserCache.getAvatarByIdFromLocal(vo.getUserId()));
        }

        // 手动封装分页
        return new PageVO<>(page, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse shutUp(TimGroupMemberShutUpParam param) {
        List<String> accounts = param.getUserIds().stream().map(id -> portalUserCache.getUsernameByIdFromLocal(id)).collect(Collectors.toList());
        TiGroupShutUpParam p = new TiGroupShutUpParam(param.getGroupId(), accounts, param.getShutUpTime());
        RestResponse restResponse = tiGroupService.shutUp(p);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        List<TimGroupMember> list = lambdaQuery().eq(TimGroupMember::getGroupId, param.getGroupId())
                .in(TimGroupMember::getUserId, param.getUserIds()).list();
        for (TimGroupMember groupMember : list) {
            groupMember.setShutUpEndTime(LocalDateTimeUtil.offset(LocalDateTime.now(), param.getShutUpTime(), ChronoUnit.SECONDS));
        }
        updateBatchById(list);
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse deleteMember(TimGroupMemberDeleteParam param) {
        String groupId = param.getGroupId();
        List<String> usernameList = param.getUsernameList();
        TiGroupDeleteMemberParam p = new TiGroupDeleteMemberParam(groupId, usernameList, param.getReason());
        RestResponse restResponse = tiGroupService.deleteMember(p);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        List<Long> userIds = usernameList.stream().map(username -> portalUserCache.getIdByUsernameFromLocal(username)).collect(Collectors.toList());
        exit(groupId, userIds);
        if (param.getRemoveMsgRecord()) {
            //清空用户的聊天记录
            for (String fromAccount : usernameList) {
                tiGroupService.deleteGroupMsgBySender(groupId, fromAccount);
            }
        }
        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addMemberForAdmin(TimGroupMemberAddParam param) {
        return addMemberBatch(param.getGroupId(), CollectionUtil.toSet(param.getUsername()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addMemberBatch(String groupId, Set<String> usernameList) {
        List<TimGroupMemberAddVO> result = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (String username : usernameList) {
            Long userId = portalUserCache.getIdByUsernameFromLocal(username);
            if (userId == null) {
                result.add(new TimGroupMemberAddVO(userId, username, false, ResponseCode.USER_NOT_FOUND));
            }

            boolean exists = lambdaQuery().eq(TimGroupMember::getGroupId, groupId)
                    .eq(TimGroupMember::getUserId, userId)
                    .exists();
            if (exists) {
                result.add(new TimGroupMemberAddVO(userId, username, false, ResponseCode.USER_IN_GROUP_CONFLICT));
            }
            userIds.add(userId);
            result.add(new TimGroupMemberAddVO(userId, username, true));
        }
        List<TiGroupAddMemberParam.Member> accounts = usernameList.stream().map(TiGroupAddMemberParam.Member::new).collect(Collectors.toList());
        TiGroupAddMemberParam p = new TiGroupAddMemberParam(groupId, accounts);
        RestResponse restResponse = tiGroupService.addMember(p);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }

        join(groupId, userIds);
        return RestResponse.ok(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse setManager(TimGroupManagerParam param) {
        GroupMemberRoleEnum role = param.getEnable() ? GroupMemberRoleEnum.Admin : GroupMemberRoleEnum.Member;
        String account = portalUserCache.getUsernameByIdFromLocal(param.getUserId());
        TiGroupMemberSetRoleParam p = new TiGroupMemberSetRoleParam(param.getGroupId(), account, role);
        RestResponse restResponse = tiGroupService.setMemberRole(p);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        TimGroupMember one = lambdaQuery().eq(TimGroupMember::getGroupId, param.getGroupId())
                .eq(TimGroupMember::getUserId, param.getUserId()).one();
        one.setRole(role);
        updateById(one);
        return RestResponse.OK;
    }
}
