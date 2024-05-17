package com.im.portal.controller;

import cn.hutool.core.collection.ListUtil;
import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupMemberService;
import com.im.common.service.TimGroupService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.api.im.tencent.entity.param.group.TiGroupQueryParam;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroup;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMember;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupResult;
import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.TimGroupMemberSimpleVO;
import com.im.common.vo.TimGroupMemberVO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群组成员相关接口
 */
@RestController
@Api(tags = "群组成员相关接口")
public class TimGroupMemberController extends BaseController {

    private TimGroupMemberService timGroupMemberService;
    private TiGroupService tiGroupService;
    private TimGroupService timGroupService;

    @Autowired
    public void setTimGroupMemberService(TimGroupMemberService timGroupMemberService) {
        this.timGroupMemberService = timGroupMemberService;
    }

    @Autowired
    public void setTiGroupService(TiGroupService tiGroupService) {
        this.tiGroupService = tiGroupService;
    }

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_MEMBER_DELETE)
    @ApiOperation("删除群组成员")
    public RestResponse deleteMember(@RequestBody @Valid TimGroupMemberDeleteParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupMemberService.deleteMember(param);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_MEMBER_ADD)
    @ApiOperation("添加群组成员")
    public RestResponse addMember(@RequestBody @Valid TimGroupMemberAddForPortalParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!addMemberPermission(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupMemberService.addMemberBatch(param.getGroupId(), param.getUsernameList());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_QRCODE_MEMBER_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_QRCODE_MEMBER_ADD)
    @ApiOperation("扫描二维码加群")
    public RestResponse addMemberByQrcode(@RequestBody @Valid TimGroupMemberJoinByQrcodeParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!addMemberPermission(param.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timGroupMemberService.addMemberBatch(param.getGroupId(), CollectionUtil.toSet(portalUser.getUsername()));
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_COUNT, method = RequestMethod.POST)
    @ApiOperation("获取群组成员数量")
    public RestResponse getMemberCount(@RequestBody @Valid TimGroupIdParam param) {
        Integer count = timGroupMemberService.lambdaQuery().eq(TimGroupMember::getGroupId, param.getGroupId()).count();
        return RestResponse.ok(count == null ? 0 : count);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_PAGE, method = RequestMethod.POST)
    @ApiOperation("搜索群组成员")
    public RestResponse<PageVO<TimGroupMemberVO>> groupMemberPage(@RequestBody @Valid TimGroupMemberPageParam param) {
        PageVO<TimGroupMemberVO> pageVO = timGroupMemberService.pageVOForAdmin(param);
        return ok(pageVO);
    }


    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_ALL, method = RequestMethod.POST)
    @ApiOperation("查询所有群组成员")
    public RestResponse<List<TimGroupMemberSimpleVO>> groupMemberAll(@RequestBody @Valid TimGroupIdParam param) {
        TiGroupQueryParam groupQuery = new TiGroupQueryParam();
        groupQuery.setGroupIdList(ListUtil.of(param.getGroupId()));
        TiGroupQueryParam.ResponseFilterDTO filter = new TiGroupQueryParam.ResponseFilterDTO();
        filter.setMemberInfoFilter(ListUtil.of("Account", "Role"));
        groupQuery.setResponseFilter(filter);
        RestResponse<TiGroupResult> restResponse = tiGroupService.getGroupInfo(groupQuery);
        if (restResponse.isOkRsp()) {
            TiGroupResult data = restResponse.getData();
            TiGroup tiGroup = data.getGroups().get(0);
            List<TiGroupMember> members = tiGroup.getMembers();
            List<TimGroupMemberSimpleVO> collect = members.stream().map(p -> {
                Long userId = UserUtil.getUserIdByUsernameFromLocal(p.getMemberAccount(), PortalTypeEnum.PORTAL);
                return new TimGroupMemberSimpleVO(userId, p.getMemberAccount(), p.getRole());
            }).collect(Collectors.toList());
            return ok(collect);
        }

        return ok(new ArrayList<>());
    }

    /**
     * 判断当前用户是否群组或者管理员
     * 群组管理员无法实时更新，需要通过接口去获取
     */
    private boolean isManagerRole(String username, String groupId) {
        checkLocalGroupData(groupId);
        RestResponse restResponse = tiGroupService.getMemberRole(groupId, username);
        if (restResponse.isOkRsp()) {
            GroupMemberRoleEnum role = (GroupMemberRoleEnum) restResponse.getData();
            return role.equals(GroupMemberRoleEnum.Admin) || role.equals(GroupMemberRoleEnum.Owner);
        }
        return false;
    }


    /**
     * 判断当前用户是否可以拉人进群
     */
    private boolean addMemberPermission(String username, String groupId) {
        //如果开启了允许普通用户拉人进群，直接返回允许
        checkLocalGroupData(groupId);
        TimGroup timGroup = timGroupService.getByGroupId(groupId);
        if (timGroup != null && timGroup.getAddMemberEnabled()) {
            return true;
        }

        return isManagerRole(username, groupId);
    }

    private void checkLocalGroupData(String groupId) {
        TimGroup timGroup = timGroupService.getByGroupId(groupId);
        if (timGroup == null) {
            //本地数据不全，同步一次数据
            timGroupService.sync(groupId);
        }
    }

}
