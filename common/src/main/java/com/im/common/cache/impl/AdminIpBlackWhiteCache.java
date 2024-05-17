package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.AdminIpBlackWhite;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminIpBlackWhiteService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.ip.IpAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 后台IP黑白名单
 *
 * @author max.stark
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.ADMIN_IP_BLACK_WHITE, redis = false, local = true)
@Component
public class AdminIpBlackWhiteCache implements BaseCacheHandler {
    private AdminIpBlackWhiteService adminIpBlackWhiteService;

    private Set<String> LOCAL_BLACK_IP_CACHE = new HashSet<>();
    private Set<String> LOCAL_WHITE_IP_CACHE = new HashSet<>();

    @Autowired
    public void setAdminIpWhiteService(AdminIpBlackWhiteService adminIpBlackWhiteService) {
        this.adminIpBlackWhiteService = adminIpBlackWhiteService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public RestResponse checkIpBlackWhite(String ip) {
        if (LOCAL_BLACK_IP_CACHE.isEmpty() && LOCAL_WHITE_IP_CACHE.isEmpty()) {
            return RestResponse.OK;
        }

        String realIp = StrUtil.cleanBlank(ip);

        // 如果列表中有黑名单，且IP在黑名单范围内，拒绝访问
        if (isBlackIp(realIp)) {
            return RestResponse.failed(ResponseCode.IP_NOT_ALLOWED, ip);
        }

        // 如果列表中有白名单，且IP不在白名单范围内，拒绝访问
        // 意思是，如果列表中有白名单，则走白名单模式
        if (!LOCAL_WHITE_IP_CACHE.isEmpty() && !isWhiteIp(realIp)) {
            return RestResponse.failed(ResponseCode.IP_NOT_ALLOWED, ip);
        }

        return RestResponse.OK;
    }

    private boolean isBlackIp(String ip) {
        if (LOCAL_BLACK_IP_CACHE.isEmpty()) {
            return false;
        }

        // 直接匹配
        if (LOCAL_BLACK_IP_CACHE.contains(ip)) {
            return true;
        }

        // 掩码/IP段匹配
        String blackIp = CollectionUtil.findFirst(LOCAL_BLACK_IP_CACHE, e -> IpAddressUtil.isInIpv4Mask(e, ip) || IpAddressUtil.isInIPv4Range(e, ip));
        return StrUtil.isNotBlank(blackIp);
    }

    private boolean isWhiteIp(String ip) {
        if (LOCAL_WHITE_IP_CACHE.isEmpty()) {
            return false;
        }

        // 直接匹配
        if (LOCAL_WHITE_IP_CACHE.contains(ip)) {
            return true;
        }

        // 掩码/IP段匹配
        String whiteIp = CollectionUtil.findFirst(LOCAL_WHITE_IP_CACHE, e -> IpAddressUtil.isInIpv4Mask(e, ip) || IpAddressUtil.isInIPv4Range(e, ip));
        return StrUtil.isNotBlank(whiteIp);
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<AdminIpBlackWhite> list = adminIpBlackWhiteService.list();

        this.LOCAL_BLACK_IP_CACHE = CollectionUtil.toSet(list, e -> e.getBlackWhite() == BlackWhiteTypeEnum.BLACK, e -> StrUtil.trim(e.getIp()));
        this.LOCAL_WHITE_IP_CACHE = CollectionUtil.toSet(list, e -> e.getBlackWhite() == BlackWhiteTypeEnum.WHITE, e -> StrUtil.trim(e.getIp()));
    }
}
