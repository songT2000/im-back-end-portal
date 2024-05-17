package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.PortalIpBlackWhite;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.PortalIpBlackWhiteMapper;
import com.im.common.param.IdParam;
import com.im.common.param.PortalIpBlackWhiteAddAdminParam;
import com.im.common.param.PortalIpBlackWhiteEditAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalIpBlackWhiteService;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.ip.IpAddressUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 前台IP黑名单 服务实现类
 *
 * @author Max
 * @date 2018/5/18
 */
@Service
public class PortalIpBlackWhiteServiceImpl
        extends MyBatisPlusServiceImpl<PortalIpBlackWhiteMapper, PortalIpBlackWhite>
        implements PortalIpBlackWhiteService {
    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(AdminSessionUser sessionUser, PortalIpBlackWhiteAddAdminParam param) {
        String usernames = param.getUsernames();
        if (StrUtil.isNotBlank(usernames)) {
            usernames = usernames.replaceAll("，", ",");
            usernames = usernames.replaceAll("\n", ",");
            usernames = StrUtil.cleanBlank(usernames);
        }

        String ipArr = param.getIp();
        ipArr = ipArr.replaceAll("，", ",");
        ipArr = ipArr.replaceAll("\n", ",");
        ipArr = StrUtil.cleanBlank(ipArr);

        // IP支持IPV4/IPV4掩码/IPV4段/IPV6
        Set<String> ipSet = StrUtil.strArrToSet(ipArr);
        for (String ip : ipSet) {
            if (!IpAddressUtil.isIpv4(ip) && !IpAddressUtil.isIpv4Mask(ip) && !IpAddressUtil.isIpv4Range(ip) && !IpAddressUtil.isIpv6(ip)) {
                return RestResponse.failed(ResponseCode.IP_FORMATTING_INCORRECT, ip);
            }
        }

        // 是否存在
        Integer count = lambdaQuery().eq(PortalIpBlackWhite::getIp, ipArr).count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        PortalIpBlackWhite ipBlack = new PortalIpBlackWhite();
        ipBlack.setUsernames(usernames);
        ipBlack.setIp(ipArr);
        ipBlack.setBlackWhite(param.getBlackWhite());
        ipBlack.setCreateAdminId(sessionUser.getId());
        ipBlack.setUpdateAdminId(sessionUser.getId());
        ipBlack.setRemark(param.getRemark());

        boolean saved = save(ipBlack);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_IP_BLACK_WHITE);

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, PortalIpBlackWhiteEditAdminParam param) {
        String usernames = param.getUsernames();
        if (StrUtil.isNotBlank(usernames)) {
            usernames = usernames.replaceAll("，", ",");
            usernames = usernames.replaceAll("\n", ",");
            usernames = StrUtil.cleanBlank(usernames);
        }

        String ipArr = param.getIp();
        ipArr = ipArr.replaceAll("，", ",");
        ipArr = ipArr.replaceAll("\n", ",");
        ipArr = StrUtil.cleanBlank(ipArr);

        // IP支持IPV4/IPV4掩码/IPV4段/IPV6
        Set<String> ipSet = StrUtil.strArrToSet(ipArr);
        for (String ip : ipSet) {
            if (!IpAddressUtil.isIpv4(ip) && !IpAddressUtil.isIpv4Mask(ip) && !IpAddressUtil.isIpv4Range(ip) && !IpAddressUtil.isIpv6(ip)) {
                return RestResponse.failed(ResponseCode.IP_FORMATTING_INCORRECT, ip);
            }
        }

        // 检查IP是否存在
        Integer count = lambdaQuery().eq(PortalIpBlackWhite::getIp, ipArr).ne(PortalIpBlackWhite::getId, param.getId()).count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        boolean updated = lambdaUpdate()
                .eq(PortalIpBlackWhite::getId, param.getId())
                .set(PortalIpBlackWhite::getUsernames, usernames)
                .set(PortalIpBlackWhite::getIp, ipArr)
                .set(PortalIpBlackWhite::getBlackWhite, param.getBlackWhite())
                .set(PortalIpBlackWhite::getUpdateAdminId, sessionUser.getId())
                .set(PortalIpBlackWhite::getUpdateTime, LocalDateTime.now())
                .set(PortalIpBlackWhite::getRemark, param.getRemark())
                .update();

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_IP_BLACK_WHITE);

        return updated ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        boolean removed = lambdaUpdate().eq(PortalIpBlackWhite::getId, param.getId()).remove();

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_IP_BLACK_WHITE);

        return removed ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }
}
