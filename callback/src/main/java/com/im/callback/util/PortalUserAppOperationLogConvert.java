package com.im.callback.util;

import com.im.callback.entity.*;
import com.im.callback.service.TiCallbackCommandEnum;
import com.im.common.entity.PortalUserAppOperationLog;
import com.im.common.entity.enums.GroupMemberExitTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.PortalUserAppOperationLogContentEnum;
import com.im.common.entity.enums.PortalUserAppOperationLogTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.service.TimGroupService;
import com.im.common.util.StrUtil;
import com.im.common.util.spring.SpringContextUtil;
import com.im.common.util.user.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class PortalUserAppOperationLogConvert {

    /**
     * TiCallbackRequest 转换成PortalUserAppOperationLog
     */
    public static List<PortalUserAppOperationLog> convert(TiCallbackRequest callbackRequest) {
        List<PortalUserAppOperationLog> list = new ArrayList<>();
        String command = callbackRequest.getCallbackCommand();
        if (command.equals(TiCallbackCommandEnum.black_list_add.getCommand())) {
            TiCbPairListRequest request = (TiCbPairListRequest) callbackRequest;
            List<TiCbPairListRequest.PairListDTO> pairList = request.getPairList();
            for (TiCbPairListRequest.PairListDTO dto : pairList) {
                PortalUserAppOperationLog log = new PortalUserAppOperationLog();
                log.setOperationType(PortalUserAppOperationLogTypeEnum.BLACKLIST_ADD);
                log.setUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getFromAccount(), PortalTypeEnum.PORTAL));
                log.setToUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getToAccount(), PortalTypeEnum.PORTAL));
                log.setClientIp(callbackRequest.getClientIp());
                log.setOptPlatform(callbackRequest.getOptPlatform());
                log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.BLACKLIST_ADD.getStr(),
                        UserUtil.getUserNicknameByIdFromLocal(log.getUserId()),
                        UserUtil.getUserNicknameByIdFromLocal(log.getToUserId())));
                list.add(log);
            }
        }
        if (command.equals(TiCallbackCommandEnum.black_list_delete.getCommand())) {
            TiCbPairListRequest request = (TiCbPairListRequest) callbackRequest;
            List<TiCbPairListRequest.PairListDTO> pairList = request.getPairList();
            for (TiCbPairListRequest.PairListDTO dto : pairList) {
                PortalUserAppOperationLog log = new PortalUserAppOperationLog();
                log.setOperationType(PortalUserAppOperationLogTypeEnum.BLACKLIST_DELETE);
                log.setUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getFromAccount(), PortalTypeEnum.PORTAL));
                log.setToUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getToAccount(), PortalTypeEnum.PORTAL));
                log.setClientIp(callbackRequest.getClientIp());
                log.setOptPlatform(callbackRequest.getOptPlatform());
                log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.BLACKLIST_DELETE.getStr(),
                        UserUtil.getUserNicknameByIdFromLocal(log.getUserId()),
                        UserUtil.getUserNicknameByIdFromLocal(log.getToUserId())));
                list.add(log);
            }
        }
        if (command.equals(TiCallbackCommandEnum.after_friend_add.getCommand())) {
            TiCbFriendAddAfterRequest request = (TiCbFriendAddAfterRequest) callbackRequest;
            List<TiCbFriendAddAfterRequest.PairListDTO> pairList = request.getPairList();
            for (TiCbFriendAddAfterRequest.PairListDTO dto : pairList) {
                PortalUserAppOperationLog log = new PortalUserAppOperationLog();
                log.setOperationType(PortalUserAppOperationLogTypeEnum.FRIEND_ADD);
                log.setUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getFromAccount(), PortalTypeEnum.PORTAL));
                log.setToUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getToAccount(), PortalTypeEnum.PORTAL));
                log.setClientIp(callbackRequest.getClientIp());
                log.setOptPlatform(callbackRequest.getOptPlatform());
                log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.FRIEND_ADD.getStr(),
                        UserUtil.getUserNicknameByIdFromLocal(log.getUserId()),
                        UserUtil.getUserNicknameByIdFromLocal(log.getToUserId())));
                list.add(log);
            }
        }
        if (command.equals(TiCallbackCommandEnum.after_friend_delete.getCommand())) {
            TiCbPairListRequest request = (TiCbPairListRequest) callbackRequest;
            List<TiCbPairListRequest.PairListDTO> pairList = request.getPairList();
            for (TiCbPairListRequest.PairListDTO dto : pairList) {
                PortalUserAppOperationLog log = new PortalUserAppOperationLog();
                log.setOperationType(PortalUserAppOperationLogTypeEnum.FRIEND_DELETE);
                log.setUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getFromAccount(), PortalTypeEnum.PORTAL));
                log.setToUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getToAccount(), PortalTypeEnum.PORTAL));
                log.setClientIp(callbackRequest.getClientIp());
                log.setOptPlatform(callbackRequest.getOptPlatform());
                log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.FRIEND_DELETE.getStr(),
                        UserUtil.getUserNicknameByIdFromLocal(log.getUserId()),
                        UserUtil.getUserNicknameByIdFromLocal(log.getToUserId())));
                list.add(log);
            }
        }
        if (command.equals(TiCallbackCommandEnum.after_create_group.getCommand())) {
            TiCbGroupCreateRequest request = (TiCbGroupCreateRequest) callbackRequest;
            PortalUserAppOperationLog log = new PortalUserAppOperationLog();
            log.setOperationType(PortalUserAppOperationLogTypeEnum.GROUP_CREATE);
            log.setUserId(UserUtil.getUserIdByUsernameFromLocal(request.getOwnerAccount(), PortalTypeEnum.PORTAL));
            log.setGroupId(request.getGroupId());
            log.setClientIp(callbackRequest.getClientIp());
            log.setOptPlatform(callbackRequest.getOptPlatform());
            log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.GROUP_CREATE.getStr(),
                    UserUtil.getUserNicknameByIdFromLocal(log.getUserId()), request.getName()));
            list.add(log);
            //群组成员加群日志
            List<TiCbGroupCreateRequest.MemberListDTO> memberList = request.getMemberList();
            for (TiCbGroupCreateRequest.MemberListDTO member : memberList) {
                PortalUserAppOperationLog log2 = new PortalUserAppOperationLog();
                log2.setOperationType(PortalUserAppOperationLogTypeEnum.GROUP_MEMBER_JOIN);
                log2.setGroupId(request.getGroupId());
                log2.setUserId(UserUtil.getUserIdByUsernameFromLocal(request.getOwnerAccount(), PortalTypeEnum.PORTAL));
                log2.setToUserId(UserUtil.getUserIdByUsernameFromLocal(member.getMemberAccount(), PortalTypeEnum.PORTAL));
                log2.setClientIp(callbackRequest.getClientIp());
                log2.setOptPlatform(callbackRequest.getOptPlatform());
                log2.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.GROUP_MEMBER_JOIN.getStr(),
                        UserUtil.getUserNicknameByIdFromLocal(log2.getUserId()),
                        UserUtil.getUserNicknameByIdFromLocal(log2.getToUserId()),
                        request.getName()));
                list.add(log2);
            }
        }
        if (command.equals(TiCallbackCommandEnum.after_group_destroyed.getCommand())) {
            TiCbGroupDestroyedRequest request = (TiCbGroupDestroyedRequest) callbackRequest;
            PortalUserAppOperationLog log = new PortalUserAppOperationLog();
            log.setOperationType(PortalUserAppOperationLogTypeEnum.GROUP_DESTROY);
            log.setUserId(UserUtil.getUserIdByUsernameFromLocal(request.getOwnerAccount(), PortalTypeEnum.PORTAL));
            log.setGroupId(request.getGroupId());
            log.setClientIp(callbackRequest.getClientIp());
            log.setOptPlatform(callbackRequest.getOptPlatform());
            log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.GROUP_DESTROY.getStr(),
                    UserUtil.getUserNicknameByIdFromLocal(log.getUserId()), request.getName()));
            list.add(log);
            //群组成员退群日志
            List<TiCbGroupDestroyedRequest.MemberListDTO> memberList = request.getMemberList();
            for (TiCbGroupDestroyedRequest.MemberListDTO member : memberList) {
                PortalUserAppOperationLog log2 = new PortalUserAppOperationLog();
                log2.setOperationType(PortalUserAppOperationLogTypeEnum.GROUP_MEMBER_EXIT_KICK);
                log2.setUserId(UserUtil.getUserIdByUsernameFromLocal(request.getOwnerAccount(), PortalTypeEnum.PORTAL));
                log2.setGroupId(request.getGroupId());
                log2.setToUserId(UserUtil.getUserIdByUsernameFromLocal(member.getMemberAccount(), PortalTypeEnum.PORTAL));
                log2.setClientIp(callbackRequest.getClientIp());
                log2.setOptPlatform(callbackRequest.getOptPlatform());
                log2.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.GROUP_MEMBER_EXIT_KICK.getStr(),
                        UserUtil.getUserNicknameByIdFromLocal(log2.getUserId()),
                        UserUtil.getUserNicknameByIdFromLocal(log2.getToUserId()),
                        request.getName()));
                list.add(log2);
            }

        }
        if (command.equals(TiCallbackCommandEnum.after_member_join.getCommand())) {
            TiCbMemberJoinRequest request = (TiCbMemberJoinRequest) callbackRequest;
            List<TiCbMemberJoinRequest.MemberListDTO> pairList = request.getMemberList();
            TimGroupService timGroupService = SpringContextUtil.getBean(TimGroupService.class);
            TimGroup group = timGroupService.getByGroupId(request.getGroupId());

            if (group != null) {
                for (TiCbMemberJoinRequest.MemberListDTO dto : pairList) {
                    PortalUserAppOperationLog log = new PortalUserAppOperationLog();
                    log.setOperationType(PortalUserAppOperationLogTypeEnum.GROUP_MEMBER_JOIN);
                    log.setUserId(UserUtil.getUserIdByUsernameFromLocal(request.getOperatorAccount(), PortalTypeEnum.PORTAL));
                    log.setToUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getMemberAccount(), PortalTypeEnum.PORTAL));
                    log.setGroupId(request.getGroupId());
                    log.setClientIp(callbackRequest.getClientIp());
                    log.setOptPlatform(callbackRequest.getOptPlatform());
                    log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.GROUP_MEMBER_JOIN.getStr(),
                            UserUtil.getUserNicknameByIdFromLocal(log.getUserId()),
                            UserUtil.getUserNicknameByIdFromLocal(log.getToUserId()),
                            group.getGroupName()));
                    list.add(log);
                }
            }
        }
        if (command.equals(TiCallbackCommandEnum.after_member_exit.getCommand())) {
            TiCbMemberExitRequest request = (TiCbMemberExitRequest) callbackRequest;
            List<TiCbMemberExitRequest.MemberListDTO> pairList = request.getMemberList();
            TimGroupService timGroupService = SpringContextUtil.getBean(TimGroupService.class);
            TimGroup group = timGroupService.getByGroupId(request.getGroupId());

            if (group != null) {
                for (TiCbMemberExitRequest.MemberListDTO dto : pairList) {
                    PortalUserAppOperationLog log = new PortalUserAppOperationLog();
                    if (request.getExitType().equals(GroupMemberExitTypeEnum.Kicked.getVal())) {
                        log.setOperationType(PortalUserAppOperationLogTypeEnum.GROUP_MEMBER_EXIT_KICK);
                    } else {
                        log.setOperationType(PortalUserAppOperationLogTypeEnum.GROUP_MEMBER_EXIT);
                    }
                    log.setUserId(UserUtil.getUserIdByUsernameFromLocal(request.getOperatorAccount(), PortalTypeEnum.PORTAL));
                    log.setToUserId(UserUtil.getUserIdByUsernameFromLocal(dto.getMemberAccount(), PortalTypeEnum.PORTAL));
                    log.setGroupId(request.getGroupId());
                    log.setClientIp(callbackRequest.getClientIp());
                    log.setOptPlatform(callbackRequest.getOptPlatform());

                    if (request.getExitType().equals(GroupMemberExitTypeEnum.Kicked.getVal())) {
                        log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.GROUP_MEMBER_EXIT_KICK.getStr(),
                                UserUtil.getUserNicknameByIdFromLocal(log.getUserId()),
                                UserUtil.getUserNicknameByIdFromLocal(log.getToUserId()),
                                group.getGroupName()));
                    } else {
                        log.setContent(StrUtil.format(PortalUserAppOperationLogContentEnum.GROUP_MEMBER_EXIT.getStr(),
                                UserUtil.getUserNicknameByIdFromLocal(log.getToUserId()),
                                group.getGroupName()));
                    }
                    list.add(log);
                }
            }
        }
        return list;
    }

}
