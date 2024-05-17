package com.im.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.AppVersion;
import com.im.common.entity.Bank;
import com.im.common.entity.enums.AppTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.AppVersionMapper;
import com.im.common.param.AppVersionAddParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.AppVersionService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AppVersionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * app应用版本管理
 */
@Service
public class AppVersionServiceImpl extends MyBatisPlusServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    public RestResponse<AppVersionVO> queryLatest(AppTypeEnum appType) {
        List<AppVersion> list = lambdaQuery().eq(AppVersion::getAppType, appType)
                .orderByDesc(AppVersion::getVersionCode).list();
        return CollectionUtil.isEmpty(list)? RestResponse.OK :RestResponse.ok(new AppVersionVO(list.get(0)));
    }

    @Override
    public RestResponse add(AppVersionAddParam param) {
        // 检查版本号是否增加
        List<AppVersion> list = lambdaQuery().eq(AppVersion::getAppType, param.getAppType())
                .orderByDesc(AppVersion::getVersionCode).list();

        if (CollectionUtil.isNotEmpty(list) && list.get(0).getVersionCode()>= param.getVersionCode()) {
            return RestResponse.failed(ResponseCode.APP_VERSION_CODE_MUST_INCREASE);
        }

        AppVersion appVersion = new AppVersion();
        BeanUtil.copyProperties(param,appVersion);
        boolean saved = save(appVersion);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.APP_VERSION);

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    public RestResponse delete(Long id) {

        boolean removed = removeById(id);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.APP_VERSION);

        return removed ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }
}
