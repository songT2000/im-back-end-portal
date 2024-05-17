package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.AdminRole;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminRoleService;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.AdminMenuVO;
import com.im.common.vo.AdminRoleVO;
import com.im.common.vo.AdminSessionUser;
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
import java.util.Collection;
import java.util.List;

/**
 * 后台角色Controller
 *
 * @author Barry
 * @date 2019-11-08
 */
@RestController
@Api(tags = "后台角色相关接口")
public class AdminRoleController extends BaseController {
    private AdminRoleService roleService;

    @Autowired
    public void setRoleService(AdminRoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 当前登录用户的下级角色列表
     */
    @RequestMapping(value = ApiUrl.ADMIN_ROLE_CURRENT_LOWER_LIST, method = RequestMethod.POST)
    @ApiOperation("当前登录用户的下级角色列表")
    public RestResponse<List<AdminRoleVO>> adminRoleCurrentLowerList(HttpServletRequest request) {
        AdminSessionUser sessionUser = getSessionUser(request);
        List<AdminRole> userRoles = roleService.listUserRoles(sessionUser.getId());

        if (CollectionUtil.isEmpty(userRoles)) {
            return ok(new ArrayList<>());
        }

        List<Long> roleIds = CollectionUtil.toList(userRoles, AdminRole::getId);

        List<AdminRole> lowerRoles = roleService.listLowerRoles(roleIds);

        return ok(CollectionUtil.toList(lowerRoles, AdminRoleVO::new));
    }

    /**
     * 当前登录用户的角色列表和下级角色列表
     */
    @RequestMapping(value = ApiUrl.ADMIN_ROLE_CURRENT_SELF_LOWER_LIST, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("当前登录用户的角色列表和下级角色列表")
    public RestResponse<List<AdminRoleVO>> adminRoleCurrentSelfLowerList(HttpServletRequest request) {
        AdminSessionUser sessionUser = getSessionUser(request);

        Collection<AdminRole> lowerRoles = roleService.listUserRolesAndLowerRoles(sessionUser.getId());

        return ok(CollectionUtil.toList(lowerRoles, AdminRoleVO::new));
    }

    /**
     * 检查角色名是否存在
     */
    @RequestMapping(value = ApiUrl.ADMIN_ROLE_USERNAME_EXIST, method = RequestMethod.POST)
    @ApiOperation("检查角色名是否存在")
    public RestResponse<Boolean> adminRoleUsernameExist(@RequestBody @Valid AdminRoleNameCheckParam param) {
        boolean exists = roleService.isExists(param.getName(), param.getNotId());
        return ok(exists);
    }

    /**
     * 添加下级角色
     */
    @RequestMapping(value = ApiUrl.ADMIN_ROLE_ADD, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("添加下级角色")
    public RestResponse adminRoleAdd(HttpServletRequest request, @RequestBody @Valid AdminRoleAddParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return roleService.add(sessionUser, param);
    }

    /**
     * 编辑下级角色
     */
    @RequestMapping(value = ApiUrl.ADMIN_ROLE_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("编辑下级角色")
    public RestResponse adminRoleEdit(HttpServletRequest request, @RequestBody @Valid AdminRoleEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return roleService.edit(sessionUser, param);
    }

    /**
     * 启用/禁用下级角色
     */
    @RequestMapping(value = ApiUrl.ADMIN_ROLE_ENABLE_DISABLE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("启用/禁用下级角色")
    public RestResponse adminRoleEnableDisable(HttpServletRequest request, @RequestBody @Valid IdEnableDisableParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return roleService.enableDisable(sessionUser, param);
    }

    /**
     * 获取下级角色菜单权限列表
     */
    @RequestMapping(value = ApiUrl.ADMIN_ROLE_MENU_LIST, method = RequestMethod.POST)
    @ApiOperation("获取下级角色菜单权限列表")
    public RestResponse<List<AdminMenuVO>> adminRoleMenuList(HttpServletRequest request, @RequestBody @Valid AdminRoleMenuListParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        List<AdminMenuVO> adminMenuVOS = roleService.listSelfOrLowerMenu(sessionUser, param.getId());
        return ok(adminMenuVOS);
    }

    /**
     * 编辑下级角色权限
     */
    @RequestMapping(value = ApiUrl.ADMIN_ROLE_EDIT_MENU, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("编辑下级角色权限")
    public RestResponse adminRoleEditMenu(HttpServletRequest request, @RequestBody @Valid AdminRoleEditMenuParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return roleService.editMenu(sessionUser, param);
    }
}
