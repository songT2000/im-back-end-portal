package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.PortalUserEmoji;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.PortalUserEmojiMapper;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserEmojiService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.PortalUserEmojiVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortalUserEmojiServiceImpl
        extends MyBatisPlusServiceImpl<PortalUserEmojiMapper, PortalUserEmoji>
        implements PortalUserEmojiService {

    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    public RestResponse delete(Long id) {
        removeById(id);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_USER_EMOJI);
        return RestResponse.OK;
    }

    @Override
    public RestResponse add(PortalUserEmoji param) {
        save(param);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_USER_EMOJI);
        return RestResponse.ok(new PortalUserEmojiVo(param));
    }
}
