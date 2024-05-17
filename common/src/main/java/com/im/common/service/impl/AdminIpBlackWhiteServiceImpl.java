package com.im.common.service.impl;

import cn.hutool.core.net.Ipv4Util;
import com.im.common.cache.base.CacheProxy;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.AdminIpBlackWhite;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.AdminIpBlackWhiteMapper;
import com.im.common.param.AdminIpBlackWhiteAddAdminParam;
import com.im.common.param.AdminIpBlackWhiteEditAdminParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminIpBlackWhiteService;
import com.im.common.service.GoogleAuthService;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.ip.IpAddressUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 后台黑白名单
 *
 * @author Barry
 * @date 2021-06-24
 */
@Service
public class AdminIpBlackWhiteServiceImpl
        extends MyBatisPlusServiceImpl<AdminIpBlackWhiteMapper, AdminIpBlackWhite>
        implements AdminIpBlackWhiteService {
    private CacheProxy cacheProxy;
    private GoogleAuthService googleAuthService;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Autowired
    public void setGoogleAuthService(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(AdminSessionUser sessionUser, AdminIpBlackWhiteAddAdminParam param) {
        RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
        if (!googleRsp.isOkRsp()) {
            return googleRsp;
        }

        String ip = StrUtil.cleanBlank(param.getIp());
        // IP支持IPV4/IPV4掩码/IPV4段/IPV6
        if (!IpAddressUtil.isIpv4(ip) && !IpAddressUtil.isIpv4Mask(ip) && !IpAddressUtil.isIpv4Range(ip) && !IpAddressUtil.isIpv6(ip)) {
            return RestResponse.failed(ResponseCode.IP_FORMATTING_INCORRECT, ip);
        }

        // 如果是IPV4段，则不能超过256个IP
        if (IpAddressUtil.isIpv4Range(ip)) {
            String[] ipRange = StrUtil.splitToArray(ip, CommonConstant.RANGE_EN);
            int rangeCount = Ipv4Util.countByIpRange(ipRange[0], ipRange[1]);
            if (rangeCount > 256) {
                return RestResponse.failed(ResponseCode.IP_FORMATTING_INCORRECT, ip);
            }
        }

        // 是否存在
        Integer count = lambdaQuery().eq(AdminIpBlackWhite::getIp, ip).count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        AdminIpBlackWhite ipBlackWhite = new AdminIpBlackWhite();
        ipBlackWhite.setIp(ip);
        ipBlackWhite.setBlackWhite(param.getBlackWhite());
        ipBlackWhite.setUpdateAdminId(sessionUser.getId());
        ipBlackWhite.setRemark(StrUtil.trim(param.getRemark()));
        ipBlackWhite.setCreateTime(LocalDateTime.now());
        ipBlackWhite.setUpdateTime(ipBlackWhite.getCreateTime());

        boolean saved = save(ipBlackWhite);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_IP_BLACK_WHITE);

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, AdminIpBlackWhiteEditAdminParam param) {
        RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
        if (!googleRsp.isOkRsp()) {
            return googleRsp;
        }

        String ip = StrUtil.cleanBlank(param.getIp());
        // IP支持IPV4/IPV4掩码/IPV4段/IPV6
        if (!IpAddressUtil.isIpv4(ip) && !IpAddressUtil.isIpv4Mask(ip) && !IpAddressUtil.isIpv4Range(ip) && !IpAddressUtil.isIpv6(ip)) {
            return RestResponse.failed(ResponseCode.IP_FORMATTING_INCORRECT, ip);
        }

        // 如果是IPV4段，则不能超过256个IP
        if (IpAddressUtil.isIpv4Range(ip)) {
            String[] ipRange = StrUtil.splitToArray(ip, CommonConstant.RANGE_EN);
            int rangeCount = Ipv4Util.countByIpRange(ipRange[0], ipRange[1]);
            if (rangeCount > 256) {
                return RestResponse.failed(ResponseCode.IP_FORMATTING_INCORRECT, ip);
            }
        }

        // 是否存在
        Integer count = lambdaQuery().eq(AdminIpBlackWhite::getIp, ip).ne(AdminIpBlackWhite::getId, param.getId()).count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        boolean updated = lambdaUpdate()
                .eq(AdminIpBlackWhite::getId, param.getId())
                .set(AdminIpBlackWhite::getIp, ip)
                .set(AdminIpBlackWhite::getBlackWhite, param.getBlackWhite())
                .set(AdminIpBlackWhite::getRemark, StrUtil.trim(param.getRemark()))
                .set(AdminIpBlackWhite::getUpdateAdminId, sessionUser.getId())
                .set(AdminIpBlackWhite::getUpdateTime, LocalDateTime.now())
                .update();

        if (updated) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_IP_BLACK_WHITE);
            return RestResponse.OK;
        }

        return RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(AdminSessionUser sessionUser, IdGoogleParam param) {
        RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
        if (!googleRsp.isOkRsp()) {
            return googleRsp;
        }

        boolean removed = lambdaUpdate().eq(AdminIpBlackWhite::getId, param.getId()).remove();

        if (removed) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_IP_BLACK_WHITE);
            return RestResponse.OK;
        }

        return RestResponse.SYS_DATA_STATUS_ERROR;
    }
}
