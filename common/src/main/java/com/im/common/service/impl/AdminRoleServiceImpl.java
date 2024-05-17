package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.AdminMenu;
import com.im.common.entity.AdminRole;
import com.im.common.entity.AdminUserRole;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.AdminRoleMapper;
import com.im.common.param.AdminRoleAddParam;
import com.im.common.param.AdminRoleEditMenuParam;
import com.im.common.param.AdminRoleEditParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminMenuService;
import com.im.common.service.AdminRoleMenuService;
import com.im.common.service.AdminRoleService;
import com.im.common.service.AdminUserRoleService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminMenuVO;
import com.im.common.vo.AdminSessionUser;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统角色表 服务实现类
 *
 * @author Barry
 * @date 2019-11-06
 */
@Service
public class AdminRoleServiceImpl
        extends MyBatisPlusServiceImpl<AdminRoleMapper, AdminRole>
        implements AdminRoleService {
    private AdminRoleMapper roleMapper;
    private AdminUserRoleService adminUserRoleService;
    private CacheProxy cacheProxy;
    private AdminMenuService menuService;
    private AdminRoleMenuService roleMenuService;

    @Autowired
    public void setRoleMapper(AdminRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Autowired
    public void setAdminUserRoleService(AdminUserRoleService adminUserRoleService) {
        this.adminUserRoleService = adminUserRoleService;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Autowired
    public void setMenuService(AdminMenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setRoleMenuService(AdminRoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    @Override
    public boolean isAdminRole(AdminRole adminRole) {
        return adminRole != null && adminRole.getParentId() == CommonConstant.LONG_0;
    }

    @Override
    public boolean hasAdminRole(List<AdminRole> adminRoles) {
        boolean hasAdminRole = CollectionUtil.anyMatch(adminRoles, this::isAdminRole);
        return hasAdminRole;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Collection<AdminRole> listUserRolesAndLowerRoles(long adminId) {
        List<AdminRole> adminRoles = listUserRoles(adminId);
        List<Long> adminRoleIds = CollectionUtil.toList(adminRoles, AdminRole::getId);
        List<AdminRole> lowerRoles = listLowerRoles(adminRoleIds);
        return CollectionUtil.distinctBy(CollectionUtil.addAll(adminRoles, lowerRoles), AdminRole::getId);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<AdminRole> listUserRoles(long adminId, Boolean enabled) {
        return roleMapper.listUserRoles(adminId, enabled);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Map<Long, List<AdminRole>> listUserRoles(List<Long> adminIds) {
        // 查询所有用户的角色ID列表
        List<AdminUserRole> userRoles = adminUserRoleService.listUserRoles(adminIds);
        if (CollectionUtil.isEmpty(userRoles)) {
            return new HashMap<>(0);
        }

        // 查询角色
        Set<Long> roleIds = CollectionUtil.toSet(userRoles, AdminUserRole::getRoleId);
        Collection<AdminRole> roles = listByIds(roleIds);

        if (CollectionUtil.isEmpty(roles)) {
            return new HashMap<>(0);
        }

        // 角色转成Map列表
        Map<Long, AdminRole> rolesMap = CollectionUtil.toMapWithKey(roles, AdminRole::getId);

        Map<Long, List<AdminRole>> adminRoleVOMap = new HashMap<>();

        // map用户对应的角色
        for (AdminUserRole userRole : userRoles) {
            AdminRole adminRole = rolesMap.get(userRole.getRoleId());

            if (!adminRoleVOMap.containsKey(userRole.getAdminId())) {
                adminRoleVOMap.put(userRole.getAdminId(), new ArrayList<>());
            }

            adminRoleVOMap.get(userRole.getAdminId()).add(adminRole);
        }

        // 将角色排序
        adminRoleVOMap.forEach((adminId, adminRoleList) -> Collections.sort(adminRoleList));

        return adminRoleVOMap;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<AdminRole> listLowerRoles(List<Long> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }

        // 查询所有角色
        List<AdminRole> adminRoles = list();

        if (CollectionUtil.isEmpty(adminRoles)) {
            return new ArrayList<>();
        }

        List<AdminRole> lowerRoles = new ArrayList<>();

        // 查找每个角色的下级列表
        for (Long roleId : roleIds) {
            List<AdminRole> lowers = loopLowers(roleId, adminRoles);

            if (CollectionUtil.isNotEmpty(lowers)) {
                lowerRoles.addAll(lowers);
            }
        }

        // 去重
        return CollectionUtil.distinctBy(lowerRoles, AdminRole::getId);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public RestResponse isLowerRolesById(long adminId, Collection<Long> checkRoleIds) {
        if (CollectionUtil.isEmpty(checkRoleIds)) {
            return RestResponse.failed(ResponseCode.USER_ROLE_ILLEGAL);
        }

        // 检查输入的角色是否都是存在的
        Collection<AdminRole> checkRoles = listByIds(checkRoleIds);
        if (checkRoleIds.size() != checkRoles.size()) {
            return RestResponse.failed(ResponseCode.USER_ROLE_ILLEGAL);
        }

        return isLowerRoles(adminId, checkRoles);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public RestResponse isLowerRoles(long adminId, Collection<AdminRole> checkRoles) {
        List<AdminRole> adminRoles = listUserRoles(adminId);

        if (CollectionUtil.isEmpty(adminRoles)) {
            return RestResponse.failed(ResponseCode.USER_ROLE_DISABLED);
        }

        // 当前登录用户的下级角色
        List<Long> adminUserRoleIds = CollectionUtil.toList(adminRoles, AdminRole::getId);
        List<AdminRole> legalLowerRoles = listLowerRoles(adminUserRoleIds);

        // 当前登录用户的所有下级角色ID
        Set<Long> availableRoleIdSet = CollectionUtil.toSet(legalLowerRoles, AdminRole::getId);

        // 查看是否有不在下级角色ID列表中的
        AdminRole notLower = CollectionUtil.findFirst(checkRoles, checkRole -> !availableRoleIdSet.contains(checkRole.getId()));

        if (notLower != null) {
            return RestResponse.failed(ResponseCode.ADMIN_ROLE_NOT_LOWER, notLower.getName());
        } else {
            return RestResponse.OK;
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public RestResponse isLowerUser(long upperAdminId, long lowerAdminId) {
        // 只能操作原来是下级的账号
        List<AdminRole> checkRoles = listUserRoles(lowerAdminId);
        if (CollectionUtil.isNotEmpty(checkRoles)) {
            RestResponse isLowerRolesRsp = isLowerRoles(upperAdminId, checkRoles);
            if (!isLowerRolesRsp.isOkRsp()) {
                return isLowerRolesRsp;
            }
        }
        return RestResponse.OK;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean isSelfRoleOrLowerRole(long adminId, long roleId) {
        AdminRole role = getById(roleId);
        return isSelfRoleOrLowerRole(adminId, role);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean isSelfRoleOrLowerRole(long adminId, AdminRole role) {
        if (role == null) {
            return false;
        }

        // 看自己有没有
        List<AdminRole> adminRoles = listUserRoles(adminId);
        boolean selfHas = CollectionUtil.anyMatch(adminRoles, adminRole -> adminRole.getId().equals(role.getId()));
        if (selfHas) {
            return true;
        }

        // 看所有下级
        List<Long> adminRoleIds = CollectionUtil.toList(adminRoles, AdminRole::getId);
        List<AdminRole> lowerRoles = listLowerRoles(adminRoleIds);
        boolean lowerHas = CollectionUtil.anyMatch(lowerRoles, lowerRole -> lowerRole.getId().equals(role.getId()));
        return lowerHas;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean isExists(String name, Long notId) {
        return lambdaQuery()
                .eq(AdminRole::getName, name)
                .ne(notId != null, AdminRole::getId, notId)
                .one() != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse add(AdminSessionUser sessionUser, AdminRoleAddParam param) {
        // 检查角色名是否已经存在
        if (isExists(param.getName(), null)) {
            return RestResponse.failed(ResponseCode.ADMIN_ROLE_NAME_EXISTED);
        }

        // 父ID是否是自己的，或者下级
        boolean hasRole = isSelfRoleOrLowerRole(sessionUser.getId(), param.getParentId());
        if (!hasRole) {
            return RestResponse.failed(ResponseCode.ADMIN_ROLE_NOT_SELF_OR_LOWER_ROLE);
        }

        // 新增角色
        AdminRole role = new AdminRole();
        // 这个名称，添加的时候要求管理员输入I18N的ID，程序不要刻意处理
        role.setName(param.getName());
        role.setParentId(param.getParentId());
        role.setEnabled(true);
        role.setSort(param.getSort());
        role.setRemark(param.getRemark());
        boolean saved = save(role);

        if (saved) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_ROLE);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse edit(AdminSessionUser sessionUser, AdminRoleEditParam param) {
        // 父ID不能是自己
        if (param.getParentId().equals(param.getId())) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        // 只能编辑下级
        RestResponse isLowerRoleRsp = isLowerRoleById(sessionUser.getId(), param.getId());
        if (!isLowerRoleRsp.isOkRsp()) {
            return RestResponse.failed(ResponseCode.ADMIN_ROLE_NOT_LOWER);
        }

        // 父ID是否是自己的，或者下级
        boolean hasRole = isSelfRoleOrLowerRole(sessionUser.getId(), param.getParentId());
        if (!hasRole) {
            return RestResponse.failed(ResponseCode.ADMIN_ROLE_NOT_SELF_OR_LOWER_ROLE);
        }

        // 修改角色
        boolean updated = lambdaUpdate()
                .eq(AdminRole::getId, param.getId())
                .set(AdminRole::getParentId, param.getParentId())
                .set(AdminRole::getName, StrUtil.trim(param.getName()))
                .set(AdminRole::getSort, param.getSort())
                .set(AdminRole::getRemark, StrUtil.trim(param.getRemark()))
                .update();

        if (updated) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_ROLE);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisable(AdminSessionUser sessionUser,
                                      IdEnableDisableParam param) {
        // 只能编辑下级
        RestResponse isLowerRoleRsp = isLowerRoleById(sessionUser.getId(), param.getId());
        if (!isLowerRoleRsp.isOkRsp()) {
            return RestResponse.failed(ResponseCode.ADMIN_ROLE_NOT_LOWER);
        }

        // 修改角色
        boolean updated = lambdaUpdate()
                .eq(AdminRole::getId, param.getId())
                .set(AdminRole::getEnabled, param.getEnable())
                .update();

        if (updated) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_ROLE);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editMenu(AdminSessionUser sessionUser,
                                 AdminRoleEditMenuParam param) {
        // 只能编辑下级
        RestResponse isLowerRoleRsp = isLowerRoleById(sessionUser.getId(), param.getId());
        if (!isLowerRoleRsp.isOkRsp()) {
            return RestResponse.failed(ResponseCode.ADMIN_ROLE_NOT_LOWER);
        }

        // 修改角色菜单
        boolean saved = roleMenuService.adjustRoleMenus(param.getId(), param.getMenus());

        if (saved) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_ROLE);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<AdminMenuVO> listSelfOrLowerMenu(AdminSessionUser sessionUser, long roleId) {
        boolean selfRoleOrLowerRole = isSelfRoleOrLowerRole(sessionUser.getId(), roleId);
        if (!selfRoleOrLowerRole) {
            return new ArrayList<>();
        }

        AdminRole role = getById(roleId);
        if (role == null) {
            return new ArrayList<>();
        }

        boolean isAdminRole = role.getParentId() == CommonConstant.LONG_0;

        List<AdminMenu> adminMenus = menuService.listRoleMenus(roleId, isAdminRole);
        return CollectionUtil.toList(adminMenus, AdminMenuVO::new);
    }

    /**
     * 查找父角色ID对应的所有直接或间接下级
     *
     * @param adminRoles    所有角色
     * @param startParentId 从哪个父ID开始找起
     * @return 直接或间接下级列表
     */
    private List<AdminRole> loopLowers(long startParentId, List<AdminRole> adminRoles) {
        List<AdminRole> allLowers = new ArrayList<>();

        // 直接下级
        List<AdminRole> directLowers = adminRoles
                .stream()
                .filter(adminRoleVO -> adminRoleVO.getParentId() == startParentId)
                .sorted()
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(directLowers)) {
            return allLowers;
        }

        allLowers.addAll(directLowers);

        // 间接下级
        directLowers.forEach(directLower -> {
            List<AdminRole> indirectLowers = loopLowers(directLower.getId(), adminRoles);
            allLowers.addAll(indirectLowers);
        });

        return allLowers;
    }
}
