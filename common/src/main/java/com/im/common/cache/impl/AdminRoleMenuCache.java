package com.im.common.cache.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.CommonConstant;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.AdminMenu;
import com.im.common.entity.AdminRole;
import com.im.common.entity.AdminRoleMenu;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.AdminMenuService;
import com.im.common.service.AdminRoleMenuService;
import com.im.common.service.AdminRoleService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

/**
 * <p>角色菜单缓存</p>
 *
 * @author Barry
 * @date 2019-11-20
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.ADMIN_ROLE_MENU, redis = true, local = false)
@Component
public class AdminRoleMenuCache implements BaseCacheHandler {
    private AdminRoleService adminRoleService;
    private AdminMenuService adminMenuService;
    private AdminRoleMenuService adminRoleMenuService;
    private SetOperations<String, String> redisSet;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    @Autowired
    public void setAdminRoleService(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @Autowired
    public void setAdminMenuService(AdminMenuService adminMenuService) {
        this.adminMenuService = adminMenuService;
    }

    @Autowired
    public void setAdminRoleMenuService(AdminRoleMenuService adminRoleMenuService) {
        this.adminRoleMenuService = adminRoleMenuService;
    }

    @Autowired
    public void setRedisSet(SetOperations<String, String> redisSet) {
        this.redisSet = redisSet;
    }

    @Autowired
    public void setKeySerializer(RedisKeySerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    @Autowired
    public void setValueSerializer(RedisValueSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void reloadRedis() {
        loadAndCache();
    }

    public boolean hasPermissionByMenuId(List<AdminRole> adminRoles, long menuId) {
        return hasPermission(adminRoles, this::formatIdKey, menuId);
    }

    public boolean hasPermissionByMenuCode(List<AdminRole> adminRoles, String menuCode) {
        return hasPermission(adminRoles, this::formatCodeKey, menuCode);
    }

    public boolean hasPermissionByMenuUrl(List<AdminRole> adminRoles, String menuUrl) {
        return hasPermission(adminRoles, this::formatUrlKey, menuUrl);
    }

    private boolean hasPermission(List<AdminRole> adminRoles, Function<Long, String> key, Object value) {
        if (CollectionUtil.isEmpty(adminRoles)) {
            return false;
        }

        boolean hasAdminRole = hasAdminRole(adminRoles);
        if (hasAdminRole) {
            return true;
        }

        if (adminRoles.size() == 1) {
            String keyStr = key.apply(adminRoles.get(0).getId());
            return redisSet.isMember(keyStr, value);
        }

        List<Object> callbacks = redisSet.getOperations().executePipelined((RedisCallback<Boolean>) connection -> {
            byte[] valueByte = valueSerializer.serialize(value);

            for (AdminRole adminRole : adminRoles) {
                byte[] keyByte = keySerializer.serialize(key.apply(adminRole.getId()));

                connection.sIsMember(keyByte, valueByte);
            }

            return null;
        });

        return CollectionUtil.anyMatch(callbacks, Boolean.TRUE::equals);
    }

    private List<AdminMenu> findRoleMenuList(AdminRole adminRole,
                                             List<AdminRoleMenu> roleMenuList,
                                             List<AdminMenu> menuList) {
        Set<Long> menuIds = CollectionUtil
                .toSet(roleMenuList,
                        roleMenu -> roleMenu.getRoleId().equals(adminRole.getId()),
                        AdminRoleMenu::getMenuId);

        List<AdminMenu> foundMenuList = CollectionUtil.filterList(menuList, adminMenu -> menuIds.contains(adminMenu.getId()));

        return foundMenuList;
    }

    private boolean hasAdminRole(List<AdminRole> adminRoles) {
        return CollectionUtil.anyMatch(adminRoles, this::isAdminRole);
    }

    private boolean isAdminRole(AdminRole role) {
        return role.getParentId() == CommonConstant.LONG_0;
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<AdminRole> allRoleList = adminRoleService.list();
        List<AdminMenu> allMenuList = adminMenuService.listByEnabled();
        List<AdminRoleMenu> allRoleMenuList = adminRoleMenuService.list();

        // 每个角色对应的菜单列表
        Map<AdminRole, List<AdminMenu>> roleMenuListMap = new HashMap<>(16);

        for (AdminRole role : allRoleList) {
            boolean isAdminRole = isAdminRole(role);
            if (isAdminRole) {
                // 管理员具有所有角色
                roleMenuListMap.put(role, allMenuList);
            } else {
                // 找到角色对应的菜单列表
                List<AdminMenu> roleMenuList = findRoleMenuList(role, allRoleMenuList, allMenuList);
                roleMenuListMap.put(role, roleMenuList);
            }
        }

        Optional.ofNullable(roleMenuListMap).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param roleMenuListMap Map
     */
    private void resolveList(Map<AdminRole, List<AdminMenu>> roleMenuListMap) {
        // 设置到redis，使用pipeline减少删增之间的真空时间
        redisSet.getOperations().executePipelined((RedisCallback<Object>) connection -> {
            roleMenuListMap.forEach((adminRole, roleMenuList) -> {
                byte[] idKey = keySerializer.serialize(formatIdKey(adminRole.getId()));
                List<byte[]> idValuesList = CollectionUtil.toList(roleMenuList, roleMenu -> valueSerializer.serialize(roleMenu.getId()));
                byte[][] idValues = ArrayUtil.toArray(idValuesList, byte[].class);

                byte[] codeKey = keySerializer.serialize(formatCodeKey(adminRole.getId()));
                List<byte[]> codeValuesList = CollectionUtil.toList(roleMenuList, roleMenu -> valueSerializer.serialize(roleMenu.getCode()));
                byte[][] codeValues = ArrayUtil.toArray(codeValuesList, byte[].class);

                byte[] urlKey = keySerializer.serialize(formatUrlKey(adminRole.getId()));
                List<byte[]> urlValuesList = CollectionUtil.toListFilterByOriginal(roleMenuList,
                        roleMenu -> StrUtil.isNotBlank(roleMenu.getUrl()),
                        roleMenu -> valueSerializer.serialize(roleMenu.getUrl()));
                byte[][] urlValues = ArrayUtil.toArray(urlValuesList, byte[].class);

                connection.del(idKey);
                if (CollectionUtil.isNotEmpty(idValuesList)) {
                    connection.sAdd(idKey, idValues);
                }

                connection.del(codeKey);
                if (CollectionUtil.isNotEmpty(codeValuesList)) {
                    connection.sAdd(codeKey, codeValues);
                }

                connection.del(urlKey);
                if (CollectionUtil.isNotEmpty(urlValuesList)) {
                    connection.sAdd(urlKey, urlValues);
                }
            });
            return null;
        });
    }

    private String formatIdKey(long roleId) {
        return StrUtil.format(RedisKeyEnum.ADMIN_ROLE_MENU_ID.getVal(), roleId);
    }

    private String formatCodeKey(long roleId) {
        return StrUtil.format(RedisKeyEnum.ADMIN_ROLE_MENU_CODE.getVal(), roleId);
    }

    private String formatUrlKey(long roleId) {
        return StrUtil.format(RedisKeyEnum.ADMIN_ROLE_MENU_URL.getVal(), roleId);
    }
}
