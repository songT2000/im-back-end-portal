package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.PortalAreaBlackWhite;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalAreaBlackWhiteService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.ip.IpAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>用户区域黑白名单缓存</p>
 *
 * @author Barry
 * @date 2021-02-27
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.PORTAL_AREA_BLACK_WHITE, redis = false, local = true)
@Component
public class PortalAreaBlackWhiteCache implements BaseCacheHandler {
    private PortalAreaBlackWhiteService portalAreaBlackWhiteService;

    private Set<String> LOCAL_BLACK_AREA_CACHE = new HashSet<>();
    private Set<String> LOCAL_WHITE_AREA_CACHE = new HashSet<>();

    @Autowired
    public void setPortalAreaBlackWhiteService(PortalAreaBlackWhiteService portalAreaBlackWhiteService) {
        this.portalAreaBlackWhiteService = portalAreaBlackWhiteService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public RestResponse checkAreaBlackWhite(String ip) {
        if (LOCAL_BLACK_AREA_CACHE.isEmpty() && LOCAL_WHITE_AREA_CACHE.isEmpty()) {
            return RestResponse.OK;
        }

        String realIp = StrUtil.cleanBlank(ip);
        String area = IpAddressUtil.findLocation(realIp);

        // 如果列表中有黑名单，且区域在黑名单范围内，拒绝访问
        if (isBlackArea(area)) {
            return RestResponse.failed(ResponseCode.AREA_NOT_ALLOWED, area);
        }

        // 如果列表中有白名单，且区域不在白名单范围内，拒绝访问
        // 意思是，如果列表中有白名单，则走白名单模式
        if (!LOCAL_WHITE_AREA_CACHE.isEmpty() && !isWhiteArea(area)) {
            return RestResponse.failed(ResponseCode.AREA_NOT_ALLOWED, area);
        }

        return RestResponse.OK;
    }

    private boolean isBlackArea(String area) {
        if (LOCAL_BLACK_AREA_CACHE.isEmpty()) {
            return false;
        }

        // 直接匹配
        if (LOCAL_BLACK_AREA_CACHE.contains(area)) {
            return true;
        }

        return CollectionUtil.anyMatch(LOCAL_BLACK_AREA_CACHE, e -> StrUtil.contains(e, area));
    }

    private boolean isWhiteArea(String area) {
        if (LOCAL_WHITE_AREA_CACHE.isEmpty()) {
            return false;
        }

        // 直接匹配
        if (LOCAL_WHITE_AREA_CACHE.contains(area)) {
            return true;
        }

        return CollectionUtil.anyMatch(LOCAL_WHITE_AREA_CACHE, e -> StrUtil.contains(e, area));
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<PortalAreaBlackWhite> list = portalAreaBlackWhiteService.list();

        this.LOCAL_BLACK_AREA_CACHE = CollectionUtil.toSet(list, e -> e.getBlackWhite() == BlackWhiteTypeEnum.BLACK, e -> StrUtil.trim(e.getArea()));
        this.LOCAL_WHITE_AREA_CACHE = CollectionUtil.toSet(list, e -> e.getBlackWhite() == BlackWhiteTypeEnum.WHITE, e -> StrUtil.trim(e.getArea()));
    }
}
