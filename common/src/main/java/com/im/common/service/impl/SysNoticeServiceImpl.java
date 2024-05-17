package com.im.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.SysNotice;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.SysNoticeMapper;
import com.im.common.param.IdGoogleParam;
import com.im.common.param.SysNoticeAddParam;
import com.im.common.param.SysNoticeEditParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.GoogleAuthService;
import com.im.common.service.SysNoticeService;
import com.im.common.util.NumberUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 系统公告
 *
 * @author max.stark
 */
@Service
public class SysNoticeServiceImpl
        extends MyBatisPlusServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {
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
    public RestResponse addForAdmin(AdminSessionUser sessionUser, SysNoticeAddParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        SysNotice sysNotice = new SysNotice();
        sysNotice.setTitle(StrUtil.trim(param.getTitle()));
        sysNotice.setSimpleContent(StrUtil.trim(param.getSimpleContent()));
        sysNotice.setContent(StrUtil.trim(param.getContent()));
        sysNotice.setTop(param.getTop());
        sysNotice.setShowing(param.getShowing());
        sysNotice.setSort(param.getSort());
        sysNotice.setLanguageCode(StrUtil.trim(param.getLanguageCode()));

        // 保存
        boolean save = save(sysNotice);
        if (!save) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SYS_NOTICE);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, SysNoticeEditParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        {
            // 检查基本参数
            Integer count = lambdaQuery().eq(SysNotice::getId, param.getId()).count();
            if (!NumberUtil.isGreatThenZero(count)) {
                return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
            }
        }

        LambdaUpdateChainWrapper<SysNotice> wrapper = lambdaUpdate()
                .eq(SysNotice::getId, param.getId())
                .set(SysNotice::getSort, param.getSort())
                .set(SysNotice::getTop, param.getTop())
                .set(SysNotice::getShowing, param.getShowing())
                .set(SysNotice::getLanguageCode, param.getLanguageCode())
                .set(StrUtil.isNotBlank(param.getTitle()), SysNotice::getTitle, StrUtil.trim(param.getTitle()))
                .set(StrUtil.isNotBlank(param.getContent()), SysNotice::getContent, StrUtil.trim(param.getContent()))
                .set(StrUtil.isNotBlank(param.getSimpleContent()), SysNotice::getSimpleContent, StrUtil.trim(param.getSimpleContent()))
                .set(SysNotice::getUpdateTime, LocalDateTime.now());

        boolean updated = wrapper.update();

        // 修改
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SYS_NOTICE);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(AdminSessionUser sessionUser, IdGoogleParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        boolean removed = removeById(param.getId());
        if (!removed) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SYS_NOTICE);

        return RestResponse.OK;
    }

}
