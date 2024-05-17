package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.AdminMenu;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.AdminMenuMapper;
import com.im.common.param.AdminMenuEditParam;
import com.im.common.param.IdSortParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminMenuService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统菜单和权限表 服务实现类
 *
 * @author Barry
 * @date 2019-11-06
 */
@Service
public class AdminMenuServiceImpl
        extends MyBatisPlusServiceImpl<AdminMenuMapper, AdminMenu>
        implements AdminMenuService {
    private AdminMenuMapper menuMapper;
    private CacheProxy cacheProxy;

    @Autowired
    public void setMenuMapper(AdminMenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<AdminMenu> listUserMenus(long adminId, boolean hasAdminRole) {
        if (hasAdminRole) {
            return lambdaQuery().eq(AdminMenu::getEnabled, true).list();
        }

        return menuMapper.listUserMenus(adminId);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<AdminMenu> listRoleMenus(long roleId, boolean isAdminRole) {
        if (isAdminRole) {
            return lambdaQuery().eq(AdminMenu::getEnabled, true).list();
        }

        return menuMapper.listRoleMenus(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse edit(AdminMenuEditParam param) {
        boolean updated = lambdaUpdate()
                .eq(AdminMenu::getId, param.getId())
                .set(AdminMenu::getName, param.getName())
                .set(AdminMenu::getIcon, param.getIcon())
                .update();

        if (updated) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_MENU);
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_ROLE_MENU);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisable(IdEnableDisableParam param) {
        // 修改
        boolean updated = lambdaUpdate()
                .eq(AdminMenu::getId, param.getId())
                .set(AdminMenu::getEnabled, param.getEnable())
                .update();

        if (updated) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_MENU);
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_ROLE_MENU);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editSort(IdSortParam param) {
        // 获取当前数据排序号，确定是向前还是向后
        AdminMenu dbMenu = getById(param.getId());
        if (dbMenu == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }
        if (dbMenu.getSort().equals(param.getSort())) {
            return RestResponse.failed(ResponseCode.SYS_DATA_SAME_DATABASE);
        }

        // true向上，false向后
        boolean isUpward = param.getSort() > dbMenu.getSort() ? false : true;

        if (isUpward) {
            // 看前面还有没有数据
            Integer count = lambdaQuery()
                    .lt(AdminMenu::getSort, dbMenu.getSort())
                    .eq(AdminMenu::getParentId, dbMenu.getParentId())
                    .count();
            if (count == null || count <= 0) {
                return RestResponse.failed(ResponseCode.SYS_DATA_ALREADY_FIRST);
            }

            // 向上，比如要调到第1位，目前是第5位，则把第1位到第4位的位置统一加1
            menuMapper.updateSort(dbMenu.getParentId(), param.getSort(), dbMenu.getSort() - 1, 1);
        } else {
            // 看后面还有没有数据
            Integer count = lambdaQuery()
                    .gt(AdminMenu::getSort, dbMenu.getSort())
                    .eq(AdminMenu::getParentId, dbMenu.getParentId())
                    .count();
            if (count == null || count <= 0) {
                return RestResponse.failed(ResponseCode.SYS_DATA_ALREADY_LAST);
            }

            // 向下，比如要调到第9位，目前是第5位，则把第6位到第9位的位置统一减1
            menuMapper.updateSort(dbMenu.getParentId(), dbMenu.getSort() + 1, param.getSort(), -1);
        }

        // 修改
        boolean updated = lambdaUpdate()
                .eq(AdminMenu::getId, param.getId())
                .set(AdminMenu::getSort, param.getSort())
                .update();

        if (updated) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_MENU);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }
}
