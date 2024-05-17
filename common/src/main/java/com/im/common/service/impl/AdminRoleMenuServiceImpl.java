package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.AdminRoleMenu;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.AdminRoleMenuMapper;
import com.im.common.service.AdminRoleMenuService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 角色菜单表 服务实现类
 *
 * @author Barry
 * @date 2019-11-06
 */
@Service
public class AdminRoleMenuServiceImpl
        extends MyBatisPlusServiceImpl<AdminRoleMenuMapper, AdminRoleMenu>
        implements AdminRoleMenuService {

    @Autowired
    private CacheProxy cacheProxy;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adjustRoleMenus(long roleId, Collection<Long> menuIds) {
        // 先删除角色的所有菜单
        lambdaUpdate().eq(AdminRoleMenu::getRoleId, roleId).remove();

        if (CollectionUtil.isEmpty(menuIds)) {
            return true;
        }

        // 去重
        List<Long> distinctMenuIds = CollectionUtil.distinct(menuIds);

        // 再添加菜单
        List<AdminRoleMenu> roleMenus = CollectionUtil.toList(distinctMenuIds, distinctMenuId -> new AdminRoleMenu(roleId, distinctMenuId));

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_ROLE_MENU);

        return saveBatch(roleMenus);
    }
}
