package com.im.portal.controller;

import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.TimGroupVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 群组相关接口
 */
@RestController
@Api(tags = "群组相关接口")
public class TimGroupController extends BaseController {

    private TimGroupService timGroupService;
    private TiGroupService tiGroupService;

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @Autowired
    public void setTiGroupService(TiGroupService tiGroupService) {
        this.tiGroupService = tiGroupService;
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_INFO, method = RequestMethod.POST)
    @ApiOperation("获取群组信息")
    public RestResponse<TimGroupVO> getGroupInfo(@RequestBody @Valid TimGroupIdParam param) {
        TimGroup timGroup = timGroupService.getByGroupId(param.getGroupId());
        if (timGroup == null) {
            //新创建的群服务器尚未同步，可能查不到，返回一个默认的群组
            TimGroupVO timGroupVO = new TimGroupVO();
            timGroupVO.setGroupId(param.getGroupId());
            return RestResponse.ok(timGroupVO);
        }
        return RestResponse.ok(new TimGroupVO(timGroup));
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_ENABLE_DISABLE_SHOW_MEMBER, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_ENABLE_DISABLE_SHOW_MEMBER)
    @ApiOperation("显示群内成员启/禁")
    public RestResponse showMemberEnabled(@RequestBody @Valid TimGroupSettingParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.updateShowMemberEnabled(param.getGroupId(), param.getEnable(), portalUser.getUsername());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_ENABLE_DISABLE_ANONYMOUS_CHAT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_ENABLE_DISABLE_ANONYMOUS_CHAT)
    @ApiOperation("允许成员私聊启/禁")
    public RestResponse anonymousChatEnabled(@RequestBody @Valid TimGroupSettingParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.updateAnonymousChatEnabled(param.getGroupId(), param.getEnable(), portalUser.getUsername());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_ENABLE_DISABLE_UPLOAD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_ENABLE_DISABLE_UPLOAD)
    @ApiOperation("普通成员上传文件权限启/禁")
    public RestResponse uploadEnabled(@RequestBody @Valid TimGroupSettingParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.updateUploadEnabled(param.getGroupId(), param.getEnable(), portalUser.getUsername());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_ENABLE_DISABLE_ADD_MEMBER, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_ENABLE_DISABLE_ADD_MEMBER)
    @ApiOperation("允许普通成员拉人进群权限启/禁")
    public RestResponse addMemberEnabled(@RequestBody @Valid TimGroupSettingParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.updateAddMemberEnabled(param.getGroupId(), param.getEnable(), portalUser.getUsername());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_ENABLE_DISABLE_ADD_FRIEND, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_ENABLE_DISABLE_ADD_FRIEND)
    @ApiOperation("群内私加好友权限启/禁")
    public RestResponse addFriendEnabled(@RequestBody @Valid TimGroupSettingParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.updateAddFriendEnabled(param.getGroupId(), param.getEnable(), portalUser.getUsername());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_ENABLE_DISABLE_EXIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_ENABLE_DISABLE_EXIT)
    @ApiOperation("允许普通成员退出群组权限启/禁")
    public RestResponse exitEnabled(@RequestBody @Valid TimGroupSettingParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.updateExitEnabled(param.getGroupId(), param.getEnable(), portalUser.getUsername());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_EDIT_NOTIFICATION, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_EDIT_NOTIFICATION)
    @ApiOperation("修改群组公告")
    public RestResponse editNotification(@RequestBody @Valid TimGroupEditNotificationParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.updateNotification(param.getGroupId(), param.getNotification(), portalUser.getUsername());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_EDIT_INTRODUCTION, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_EDIT_INTRODUCTION)
    @ApiOperation("修改群组简介")
    public RestResponse editIntroduction(@RequestBody @Valid TimGroupEditIntroductionParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.updateIntroduction(param.getGroupId(), param.getIntroduction(), portalUser.getUsername());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_COPY, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_COPY)
    @ApiOperation("一键复制创建新群")
    public RestResponse copyGroup(@RequestBody @Valid TimGroupCopyParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupService.copyCreateNewGroup(param.getGroupId(), param.getGroupName());
    }

    /**
     * 判断当前用户是否群组或者管理员
     * 群组管理员无法实时更新，需要通过接口去获取
     */
    private boolean isManagerRole(String username, String groupId) {
        TimGroup timGroup = timGroupService.getByGroupId(groupId);
        if (timGroup == null) {
            //本地没有群组，需要同步
            timGroupService.sync(groupId);
        }
        RestResponse restResponse = tiGroupService.getMemberRole(groupId, username);
        if (restResponse.isOkRsp()) {
            GroupMemberRoleEnum role = (GroupMemberRoleEnum) restResponse.getData();
            return role.equals(GroupMemberRoleEnum.Admin) || role.equals(GroupMemberRoleEnum.Owner);
        }
        return false;
    }

}
