package com.im.portal.controller;

import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.TimGroupMessagePortalPageParam;
import com.im.common.param.TimMessageGroupWithdrawParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.TimMessageGroupService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.TimMessageGroupVO;
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
 * 群组消息相关接口
 */
@RestController
@Api(tags = "群组消息相关接口")
public class TimGroupMessageController extends BaseController {

    private TimMessageGroupService timMessageGroupService;

    private TiGroupService tiGroupService;

    @Autowired
    public void setTiGroupService(TiGroupService tiGroupService) {
        this.tiGroupService = tiGroupService;
    }

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }


    @RequestMapping(value = ApiUrl.TIM_GROUP_MESSAGE_WITHDRAW, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_MESSAGE_WITHDRAW)
    @ApiOperation("管理员撤回群组消息")
    public RestResponse withdraw(@RequestBody @Valid TimMessageGroupWithdrawParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        if (!isManagerRole(portalUser.getUsername(), param.getGroupId())) {
            return failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return timMessageGroupService.withdraw(param);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MESSAGE_LIST, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_MESSAGE_LIST)
    @ApiOperation("查询群聊消息")
    public RestResponse<PageVO<TimMessageGroupVO>> getTimFaceList(@RequestBody @Valid TimGroupMessagePortalPageParam param, HttpServletRequest request) {
        return ok(timMessageGroupService.pageFormPortal(param));
    }

    /**
     * 判断当前用户是否群组或者管理员
     * 群组管理员无法实时更新，需要通过接口去获取
     */
    private boolean isManagerRole(String username, String groupId) {

        RestResponse restResponse = tiGroupService.getMemberRole(groupId, username);
        if (restResponse.isOkRsp()) {
            GroupMemberRoleEnum role = (GroupMemberRoleEnum) restResponse.getData();
            return role.equals(GroupMemberRoleEnum.Admin) || role.equals(GroupMemberRoleEnum.Owner);
        }
        return false;
    }
}
