package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.AdminMenu;
import com.im.common.entity.AdminUser;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminMenuService;
import com.im.common.service.AdminRoleService;
import com.im.common.service.AdminUserService;
import com.im.common.service.UserAuthTokenService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.jwt.JwtUtil;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.url.RequestWebsite;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.AdminUserLoginVO;
import com.im.common.vo.AdminUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 后台用户Controller
 *
 * @author Barry
 * @date 2019-11-08
 */
@RestController
@Api(tags = "管理员相关接口")
public class AdminUserController extends BaseController {
    private AdminUserService adminUserService;
    private AdminRoleService roleService;
    private AdminMenuService menuService;
    private UserAuthTokenService userAuthTokenService;

    @Autowired
    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Autowired
    public void setRoleService(AdminRoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setMenuService(AdminMenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setUserAuthTokenService(UserAuthTokenService userAuthTokenService) {
        this.userAuthTokenService = userAuthTokenService;
    }

    @RequestMapping(value = ApiUrl.LOGIN, method = RequestMethod.POST)
    @ApiOperation(value = "登录", notes = "登录成功后，把返回数据里面的token字段，保存在客户端里(LocalStorage)，以后所有以/api/*/auth开头的接口，" +
            "都需要携带在header里，并取名" + JwtUtil.AUTH_HEADER +
            "如果RestResponse.code=GOOGLE_NOT_YET_BIND，则要弹窗提示用户绑定谷歌，谷歌相应的key和qrCode会在data里面")
    public RestResponse<AdminUserLoginVO> login(@RequestBody @Valid ManualLoginParam param,
                                                @RequestIp String requestIp,
                                                @RequestWebsite String requestWebsite,
                                                HttpServletRequest request) {
        param.setIp(requestIp);
        param.setUrl(requestWebsite);
        return adminUserService.manualLogin(request, param);
    }

    @RequestMapping(value = ApiUrl.LOGOUT, method = RequestMethod.POST)
    @ApiOperation("登出")
    public RestResponse logout(HttpServletRequest request) {
        adminUserService.logout(request);
        return ok();
    }

    @RequestMapping(value = ApiUrl.GET_LOGIN_INFO, method = RequestMethod.POST)
    @ApiOperation("获取当前登录用户个人信息")
    public RestResponse<AdminUserLoginVO> getLoginInfo(HttpServletRequest request, HttpServletResponse response) {
        String token = JwtUtil.getTokenFromHeader(request);
        if (token == null) {
            return failed(ResponseCode.USER_SESSION_PLEASE_LOGIN_FIRST);
        }

        AdminSessionUser sessionUser = userAuthTokenService.checkToken(request, response, PortalTypeEnum.ADMIN);
        if (sessionUser == null) {
            adminUserService.logout(request);
            return null;
        }

        AdminUser user = adminUserService.getByUsernameNotDeleted(sessionUser.getUsername());
        if (user == null || !Boolean.TRUE.equals(user.getEnabled())) {
            adminUserService.logout(request);
            return RestResponse.logout(ResponseCode.USER_SESSION_INFO_CHANGED);
        }

        boolean hasAdminRole = roleService.hasAdminRole(sessionUser.getRoles());

        // 获取权限列表
        List<AdminMenu> adminMenus = menuService.listUserMenus(sessionUser.getId(), hasAdminRole);

        // 返回信息
        AdminUserLoginVO loginUserVo = new AdminUserLoginVO(sessionUser, user, adminMenus);

        return ok(loginUserVo);
    }

    @RequestMapping(value = ApiUrl.HAS_BOUND_GOOGLE, method = RequestMethod.POST)
    @ApiOperation("获取用户是否绑定谷歌")
    public RestResponse<Boolean> hasBoundGoogle(@RequestBody @Valid HasBoundGoogleParam param) {
        boolean hasBoundGoogle = adminUserService.hasBoundGoogle(param.getUsername());
        return ok(hasBoundGoogle);
    }

    @RequestMapping(value = ApiUrl.BIND_GOOGLE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.GOOGLE_BIND, hideParam = "password")
    @ApiOperation(value = "绑定谷歌", notes = "如果登录时RestResponse.code=GOOGLE_NOT_YET_BIND，则要弹窗提示用户绑定谷歌")
    public RestResponse bindGoogle(@RequestBody @Valid BindGoogleParam param, @RequestIp String requestIp) {
        param.setIp(requestIp);
        return adminUserService.bindGoogle(param);
    }

    @RequestMapping(value = ApiUrl.ADMIN_USER_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("列表")
    public RestResponse<PageVO<AdminUserVO>> adminUserPage(HttpServletRequest request, @RequestBody @Valid AdminUserPageParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        PageVO<AdminUserVO> page = adminUserService.pageLowerUsers(param, sessionUser);
        return ok(page);
    }

    @RequestMapping(value = ApiUrl.ADMIN_USER_USERNAME_EXIST, method = RequestMethod.POST)
    @ApiOperation("检查用户名是否存在")
    public RestResponse<Boolean> adminUserUsernameExist(@RequestBody @Valid UsernameParam param) {
        boolean exists = adminUserService.isExists(param.getUsername());
        return ok(exists);
    }

    @RequestMapping(value = ApiUrl.ADMIN_USER_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.ADMIN_USER_ADD, hideParam = "loginPwd")
    @ApiOperation("新增")
    public RestResponse adminUserAdd(HttpServletRequest request, @RequestBody @Valid AdminUserAddParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminUserService.add(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.ADMIN_USER_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.ADMIN_USER_EDIT, hideParam = "loginPwd")
    @ApiOperation("编辑")
    public RestResponse adminUserEdit(HttpServletRequest request, @RequestBody @Valid AdminUserEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminUserService.edit(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.EDIT_LOGIN_PWD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.LOGIN_PWD_EDIT, hideParam = {"oldPwd", "newPwd"})
    @ApiOperation("修改当前登录用户登录密码")
    public RestResponse editLoginPwd(HttpServletRequest request, @RequestBody @Valid UserEditPwdParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminUserService.editLoginPwd(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.ADMIN_USER_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.ADMIN_USER_DELETE)
    @ApiOperation("删除")
    public RestResponse adminUserDelete(HttpServletRequest request, @RequestBody @Valid UsernameGoogleCodeParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminUserService.delete(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.ADMIN_USER_ENABLE_DISABLE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.ADMIN_USER_ENABLE_DISABLE)
    @ApiOperation("启/禁")
    public RestResponse adminUserEnableDisable(HttpServletRequest request, @RequestBody @Valid UsernameEnableDisableParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminUserService.enableDisable(sessionUser, param.getUsername(), param.getEnable());
    }

    @RequestMapping(value = ApiUrl.ADMIN_USER_UNBIND_GOOGLE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.UNBIND_GOOGLE)
    @ApiOperation("解绑谷歌")
    public RestResponse adminUserUnbindGoogle(HttpServletRequest request, @RequestBody @Valid UsernameParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminUserService.unbindGoogle(sessionUser, param.getUsername());
    }

    @RequestMapping(value = ApiUrl.ADMIN_USER_RESET_LOGIN_PWD_ERROR_NUM, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.ADMIN_USER_RESET_LOGIN_PWD_ERROR_NUM)
    @ApiOperation("重置密码错误次数")
    public RestResponse resetLoginPwdErrorNum(HttpServletRequest request, @RequestBody @Valid UsernameParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminUserService.resetLoginPwdErrorNum(sessionUser, param.getUsername());
    }
}
