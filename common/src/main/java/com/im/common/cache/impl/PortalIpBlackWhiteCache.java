package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.PortalIpBlackWhite;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalIpBlackWhiteService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.ip.IpAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>前台IP黑白名单缓存</p>
 *
 * @author Barry
 * @date 2020-06-16
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.PORTAL_IP_BLACK_WHITE, redis = false, local = true)
@Component
public class PortalIpBlackWhiteCache implements BaseCacheHandler {
    private PortalIpBlackWhiteService portalIpBlackWhiteService;

    private Set<String> LOCAL_ALL_BLACK_IP_CACHE = new HashSet<>();
    private Set<String> LOCAL_ALL_WHITE_IP_CACHE = new HashSet<>();

    private Map<String, Set<String>> LOCAL_USERNAME_BLACK_IP_CACHE = new HashMap<>();
    private Map<String, Set<String>> LOCAL_USERNAME_WHITE_IP_CACHE = new HashMap<>();

    @Autowired
    public void setPortalIpBlackWhiteService(PortalIpBlackWhiteService portalIpBlackWhiteService) {
        this.portalIpBlackWhiteService = portalIpBlackWhiteService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public RestResponse checkIpBlackWhite(String username, String ip) {
        if (LOCAL_ALL_BLACK_IP_CACHE.isEmpty()
                && LOCAL_ALL_WHITE_IP_CACHE.isEmpty()
                && LOCAL_USERNAME_BLACK_IP_CACHE.isEmpty()
                && LOCAL_USERNAME_WHITE_IP_CACHE.isEmpty()) {
            return RestResponse.OK;
        }

        String realUsername = username == null ? null : username.toLowerCase();
        String realIp = StrUtil.cleanBlank(ip);

        // 如果列表中有黑名单，且IP在黑名单范围内，拒绝访问
        if (isInAllBlackIp(realIp)) {
            return RestResponse.failed(ResponseCode.IP_NOT_ALLOWED, ip);
        }
        if (StrUtil.isNotBlank(realUsername) && isInUsernameBlackIp(realUsername, realIp)) {
            return RestResponse.failed(ResponseCode.IP_NOT_ALLOWED, ip);
        }

        // 如果列表中有白名单，且IP不在白名单范围内，拒绝访问
        // 意思是，如果列表中有白名单，则走白名单模式
        if (!isInAllWhiteIp(realIp)) {
            return RestResponse.failed(ResponseCode.IP_NOT_ALLOWED, ip);
        }
        if (StrUtil.isNotBlank(realUsername) && !isInUsernameWhiteIp(realUsername, realIp)) {
            return RestResponse.failed(ResponseCode.IP_NOT_ALLOWED, ip);
        }

        return RestResponse.OK;
    }

    private boolean isInAllBlackIp(String ip) {
        // true拒绝访问，false可以访问

        if (LOCAL_ALL_BLACK_IP_CACHE.isEmpty()) {
            return false;
        }

        // 直接匹配
        if (LOCAL_ALL_BLACK_IP_CACHE.contains(ip)) {
            return true;
        }

        // 掩码/IP段匹配
        String blackIp = CollectionUtil.findFirst(LOCAL_ALL_BLACK_IP_CACHE, e -> IpAddressUtil.isInIpv4Mask(e, ip) || IpAddressUtil.isInIPv4Range(e, ip));
        if (StrUtil.isNotBlank(blackIp)) {
            return true;
        }
        return false;
    }

    private boolean isInUsernameBlackIp(String username, String ip) {
        // true拒绝访问，false可以访问

        if (StrUtil.isBlank(username)) {
            return false;
        }

        if (LOCAL_USERNAME_BLACK_IP_CACHE.isEmpty() || !LOCAL_USERNAME_BLACK_IP_CACHE.containsKey(username)) {
            return false;
        }

        Set<String> blackIpSet = LOCAL_USERNAME_BLACK_IP_CACHE.get(username);
        if (CollectionUtil.isEmpty(blackIpSet)) {
            return false;
        }

        // 直接匹配
        if (blackIpSet.contains(ip)) {
            return true;
        }

        // 掩码/IP段匹配
        String blackIp = CollectionUtil.findFirst(blackIpSet, e -> IpAddressUtil.isInIpv4Mask(e, ip) || IpAddressUtil.isInIPv4Range(e, ip));
        if (StrUtil.isNotBlank(blackIp)) {
            return true;
        }
        return false;
    }

    private boolean isInAllWhiteIp(String ip) {
        // true可以访问，false拒绝访问

        if (LOCAL_ALL_WHITE_IP_CACHE.isEmpty()) {
            return true;
        }

        // 直接匹配
        if (LOCAL_ALL_WHITE_IP_CACHE.contains(ip)) {
            return true;
        }

        // 掩码/IP段匹配
        String whiteIp = CollectionUtil.findFirst(LOCAL_ALL_WHITE_IP_CACHE, e -> IpAddressUtil.isInIpv4Mask(e, ip) || IpAddressUtil.isInIPv4Range(e, ip));
        if (StrUtil.isNotBlank(whiteIp)) {
            return true;
        }
        return false;
    }

    private boolean isInUsernameWhiteIp(String username, String ip) {
        // true可以访问，false拒绝访问

        if (StrUtil.isBlank(username)) {
            return true;
        }

        if (LOCAL_USERNAME_WHITE_IP_CACHE.isEmpty() || !LOCAL_USERNAME_WHITE_IP_CACHE.containsKey(username)) {
            return true;
        }

        Set<String> whiteIpSet = LOCAL_USERNAME_WHITE_IP_CACHE.get(username);
        if (CollectionUtil.isEmpty(whiteIpSet)) {
            return true;
        }

        // 直接匹配
        if (whiteIpSet.contains(ip)) {
            return true;
        }

        // 掩码/IP段匹配
        String whiteIp = CollectionUtil.findFirst(whiteIpSet, e -> IpAddressUtil.isInIpv4Mask(e, ip) || IpAddressUtil.isInIPv4Range(e, ip));
        if (StrUtil.isNotBlank(whiteIp)) {
            return true;
        }
        return false;
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<PortalIpBlackWhite> list = portalIpBlackWhiteService.list();

        Set<String> allBlackIp = new HashSet<>();
        Set<String> allWhiteIp = new HashSet<>();

        Map<String, Set<String>> usernameBlackIp = new HashMap<>();
        Map<String, Set<String>> usernameWhiteIp = new HashMap<>();

        for (PortalIpBlackWhite blackWhite : list) {

            Set<String> ipSet = StrUtil.strArrToSetTrim(blackWhite.getIp());

            if (StrUtil.isBlank(blackWhite.getUsernames())) {
                if (blackWhite.getBlackWhite() == BlackWhiteTypeEnum.BLACK) {
                    allBlackIp.addAll(ipSet);
                }
                else if (blackWhite.getBlackWhite() == BlackWhiteTypeEnum.WHITE) {
                    allWhiteIp.addAll(ipSet);
                }
            } else {
                Set<String> usernameSet = StrUtil.strArrToSetTrim(blackWhite.getUsernames().toLowerCase());

                if (blackWhite.getBlackWhite() == BlackWhiteTypeEnum.BLACK) {
                    for (String username : usernameSet) {
                        if (!usernameBlackIp.containsKey(username)) {
                            usernameBlackIp.put(username, new HashSet<>());
                        }
                        usernameBlackIp.get(username).addAll(ipSet);
                    }
                }
                else if (blackWhite.getBlackWhite() == BlackWhiteTypeEnum.WHITE) {
                    for (String username : usernameSet) {
                        if (!usernameWhiteIp.containsKey(username)) {
                            usernameWhiteIp.put(username, new HashSet<>());
                        }
                        usernameWhiteIp.get(username).addAll(ipSet);
                    }
                }
            }
        }

        this.LOCAL_ALL_BLACK_IP_CACHE = allBlackIp;
        this.LOCAL_ALL_WHITE_IP_CACHE = allWhiteIp;

        this.LOCAL_USERNAME_BLACK_IP_CACHE = usernameBlackIp;
        this.LOCAL_USERNAME_WHITE_IP_CACHE = usernameWhiteIp;
    }
}
