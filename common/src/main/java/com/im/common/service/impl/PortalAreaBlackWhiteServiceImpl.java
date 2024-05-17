package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.PortalAreaBlackWhite;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.PortalAreaBlackWhiteMapper;
import com.im.common.param.IdParam;
import com.im.common.param.PortalAreaBlackWhiteAddAdminParam;
import com.im.common.param.PortalAreaBlackWhiteEditAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalAreaBlackWhiteService;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 前台区域黑名单 服务实现类
 *
 * @author Barry
 * @date 2021-02-27
 */
@Service
public class PortalAreaBlackWhiteServiceImpl
        extends MyBatisPlusServiceImpl<PortalAreaBlackWhiteMapper, PortalAreaBlackWhite>
        implements PortalAreaBlackWhiteService {
    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(AdminSessionUser sessionUser, PortalAreaBlackWhiteAddAdminParam param) {
        String area = StrUtil.trim(param.getArea());
        Integer count = lambdaQuery().eq(PortalAreaBlackWhite::getArea, area).count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        PortalAreaBlackWhite areaBlack = new PortalAreaBlackWhite();
        areaBlack.setArea(area);
        areaBlack.setBlackWhite(param.getBlackWhite());
        areaBlack.setCreateAdminId(sessionUser.getId());
        areaBlack.setUpdateAdminId(sessionUser.getId());
        areaBlack.setRemark(param.getRemark());

        boolean saved = save(areaBlack);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_AREA_BLACK_WHITE);

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, PortalAreaBlackWhiteEditAdminParam param) {

        String area = StrUtil.trim(param.getArea());
        // 检查区域是否存在
        Integer count = lambdaQuery()
                .eq(PortalAreaBlackWhite::getArea, area)
                .ne(PortalAreaBlackWhite::getId, param.getId()).count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        boolean updated = lambdaUpdate()
                .eq(PortalAreaBlackWhite::getId, param.getId())
                .set(PortalAreaBlackWhite::getArea, area)
                .set(PortalAreaBlackWhite::getBlackWhite, param.getBlackWhite())
                .set(PortalAreaBlackWhite::getUpdateAdminId, sessionUser.getId())
                .set(PortalAreaBlackWhite::getUpdateTime, LocalDateTime.now())
                .set(PortalAreaBlackWhite::getRemark, param.getRemark())
                .update();

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_AREA_BLACK_WHITE);

        return updated ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        boolean removed = lambdaUpdate().eq(PortalAreaBlackWhite::getId, param.getId()).remove();

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_AREA_BLACK_WHITE);

        return removed ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }
}
