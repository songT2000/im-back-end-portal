package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.PortalNavigator;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.PortalNavigatorMapper;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdParam;
import com.im.common.param.PortalNavigatorAddAdminParam;
import com.im.common.param.PortalNavigatorEditAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalNavigatorService;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 前台导航 服务实现类
 *
 * @author Barry
 * @date 2022-03-25
 */
@Service
public class PortalNavigatorServiceImpl
        extends MyBatisPlusServiceImpl<PortalNavigatorMapper, PortalNavigator>
        implements PortalNavigatorService {
    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(PortalNavigatorAddAdminParam param) {
        PortalNavigator navigator = new PortalNavigator();
        navigator.setName(StrUtil.trim(param.getName()));
        navigator.setUrl(StrUtil.trim(param.getUrl()));
        navigator.setSort(param.getSort());
        navigator.setEnabled(param.getEnabled());

        boolean saved = save(navigator);
        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_NAVIGATOR);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(PortalNavigatorEditAdminParam param) {
        boolean updated = lambdaUpdate()
                .eq(PortalNavigator::getId, param.getId())
                .set(PortalNavigator::getName, StrUtil.trim(param.getName()))
                .set(PortalNavigator::getUrl, StrUtil.trim(param.getUrl()))
                .set(PortalNavigator::getSort, param.getSort())
                .set(PortalNavigator::getEnabled, param.getEnabled())
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_NAVIGATOR);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(IdEnableDisableParam param) {
        boolean updated = lambdaUpdate()
                .eq(PortalNavigator::getId, param.getId())
                .set(PortalNavigator::getEnabled, param.getEnable())
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_NAVIGATOR);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        boolean deleted = removeById(param.getId());
        if (!deleted) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_NAVIGATOR);

        return RestResponse.OK;
    }
}
