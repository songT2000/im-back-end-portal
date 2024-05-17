package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserService;
import com.im.common.service.UserBalanceSnapshotService;
import com.im.common.util.StrUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.PortalUserAdminVO;
import com.im.common.vo.PortalUserSimpleAdminVO;
import com.im.common.vo.UserBalanceSnapshotVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 前台用户controller
 *
 * @author Barry
 * @date 2021-06-08
 */
@RestController
@Api(tags = "前台用户相关接口")
public class PortalUserController extends BaseController {
    private PortalUserService portalUserService;
    private PortalUserCache portalUserCache;
    private UserBalanceSnapshotService userBalanceSnapshotService;

    @Autowired
    public void setUserBalanceSnapshotService(UserBalanceSnapshotService userBalanceSnapshotService) {
        this.userBalanceSnapshotService = userBalanceSnapshotService;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("简单列表")
    public RestResponse<List<PortalUserSimpleAdminVO>> portalUserSimpleList() {
        List<PortalUserSimpleAdminVO> list = portalUserService.listVO(PortalUserSimpleAdminVO::new);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_PAGE, method = RequestMethod.POST)
    // 因为后台页面ApiUrl.TIM_GLOBAL_SHUT_UP_GET用到了该接口做组合数据，所以当拥有TIM_GLOBAL_SHUT_UP_GET权限时，自动拥有该权限
    @CheckPermission(url = {ApiUrl.PORTAL_USER_PAGE, ApiUrl.TIM_GLOBAL_SHUT_UP_GET})
    @ApiOperation("分页")
    public RestResponse<PageVO<PortalUserAdminVO>> portalUserPage(@RequestBody @Valid PortalUserPageAdminParam param) {
        PageVO<PortalUserAdminVO> pageVO = portalUserService.pageVOForAdmin(param);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_DETAIL, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.PORTAL_USER_PAGE)
    @ApiOperation("详情")
    public RestResponse<PortalUserAdminVO> portalUserDetail(@RequestBody @Valid UsernameParam param) {
        PortalUserAdminVO detail = portalUserService.getVOForAdmin(param);
        return ok(detail);
    }

    // @RequestMapping(value = ApiUrl.PORTAL_USER_KICK_OUT, method = RequestMethod.POST)
    // @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_KICK_OUT)
    // @CheckPermission
    // @ApiOperation("踢下线")
    // public RestResponse portalUserKickOut(@RequestBody @Valid UsernameParam param) {
    //     Long userId = portalUserCache.getIdByUsernameFromLocal(param.getUsername());
    //     if (userId == null) {
    //         return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
    //     }
    //     portalUserService.kickOutAllLoginClient(userId, ResponseCode.USER_SESSION_INFO_CHANGED);
    //     return RestResponse.OK;
    // }

    @RequestMapping(value = ApiUrl.PORTAL_USER_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_ADD, hideParam = {"loginPwd", "fundPwd"})
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse portalUserAdd(@RequestBody @Valid PortalUserAddAdminParam param) {
        return portalUserService.addForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_EDIT, hideParam = {"loginPwd", "fundPwd"})
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse portalUserEdit(@RequestBody @Valid PortalUserEditAdminParam param) {
        return portalUserService.editForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_ENABLE_DISABLE_LOGIN, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_ENABLE_DISABLE_LOGIN)
    @CheckPermission
    @ApiOperation("启/禁登录")
    public RestResponse portalUserEnableDisableLogin(@RequestBody @Valid UsernameEnableDisableParam param) {
        return portalUserService.enableDisableLoginForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_ENABLE_DISABLE_ADD_FRIEND, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_ENABLE_DISABLE_ADD_FRIEND)
    @CheckPermission
    @ApiOperation("启/禁加好友")
    public RestResponse portalUserEnableDisableAddFriend(@RequestBody @Valid UsernameEnableDisableParam param) {
        return portalUserService.enableDisableAddFriendForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_ENABLE_DISABLE_RECHARGE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_ENABLE_DISABLE_RECHARGE)
    @CheckPermission
    @ApiOperation("启/禁充值")
    public RestResponse portalUserEnableDisableRecharge(@RequestBody @Valid UsernameEnableDisableParam param) {
        return portalUserService.enableDisableRechargeForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_ENABLE_DISABLE_WITHDRAW, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_ENABLE_DISABLE_WITHDRAW)
    @CheckPermission
    @ApiOperation("启/禁提现")
    public RestResponse portalUserEnableDisableWithdraw(@RequestBody @Valid UsernameEnableDisableParam param) {
        return portalUserService.enableDisableWithdrawForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_ENABLE_DISABLE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_ENABLE_DISABLE)
    @CheckPermission
    @ApiOperation("启/禁用户")
    public RestResponse portalUserEnableDisable(@RequestBody @Valid UsernameEnableDisableParam param) {
        return portalUserService.enableDisableForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_USERNAME_EXIST, method = RequestMethod.POST)
    @ApiOperation("检查用户名是否存在")
    public RestResponse<Boolean> portalUserUsernameExist(@RequestBody @Valid UsernameParam param) {
        boolean exists = portalUserService.isExists(StrUtil.cleanBlank(param.getUsername()));
        return ok(exists);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_RESET_FUND_PWD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_RESET_FUND_PWD)
    @CheckPermission
    @ApiOperation("重置资金密码")
    public RestResponse portalUserResetFundPwd(HttpServletRequest request, @RequestBody @Valid UsernameGoogleCodeParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return portalUserService.resetFundPwdForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_EDIT_WITHDRAW_NAME, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_EDIT_WITHDRAW_NAME)
    @CheckPermission
    @ApiOperation("编辑提现姓名")
    public RestResponse portalUserEditWithdrawName(HttpServletRequest request, @RequestBody @Valid PortalUserEditWithdrawNameAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return portalUserService.editWithdrawNameForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_EDIT_MY_INVITE_CODE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_EDIT_MY_INVITE_CODE)
    @CheckPermission
    @ApiOperation("编辑我的邀请码")
    public RestResponse portalUserEditMyInviteCode(HttpServletRequest request, @RequestBody @Valid PortalUserEditMyInviteCodeAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return portalUserService.editMyInviteCodeForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_ADD_BALANCE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_ADD_BALANCE)
    @CheckPermission
    @ApiOperation("增减余额")
    public RestResponse userAddBalance(HttpServletRequest request, @RequestBody @Valid UserAddBalanceAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return portalUserService.adminAddBalance(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_BALANCE_SNAPSHOT_PAGE, method = RequestMethod.POST)
    @ApiOperation("用户余额快照")
    public RestResponse userBalanceSnapshot(@RequestBody @Valid UserBalanceSnapshotPageParam param) {
        PageVO<UserBalanceSnapshotVO> vo = userBalanceSnapshotService.pageVO(param, e -> new UserBalanceSnapshotVO(e, portalUserCache));
        return ok(vo);
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_EDIT_INTERNAL_USER, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_USER_EDIT_INTERNAL_USER)
    @CheckPermission
    @ApiOperation("编辑内部用户")
    public RestResponse editInternalUser(@RequestBody @Valid UsernameEnableDisableParam param) {
        return portalUserService.editInternalUserForAdmin(param);
    }

}
