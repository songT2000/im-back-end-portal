package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.AppAutoReplyConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.AppAutoReplyConfigMapper;
import com.im.common.param.AppAutoReplyConfigAddParam;
import com.im.common.param.IdParam;
import com.im.common.response.RestResponse;
import com.im.common.service.AppAutoReplyConfigService;
import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自动回复消息配置 服务实现类
 */
@Service
public class AppAutoReplyConfigServiceImpl
        extends MyBatisPlusServiceImpl<AppAutoReplyConfigMapper, AppAutoReplyConfig>
        implements AppAutoReplyConfigService {
    private CacheProxy cacheProxy;
    private TiSingleChatService tiSingleChatService;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Autowired
    public void setTiSingleChatService(TiSingleChatService tiSingleChatService) {
        this.tiSingleChatService = tiSingleChatService;
    }

    @Override
    public RestResponse add(AppAutoReplyConfigAddParam param) {
        AppAutoReplyConfig config = new AppAutoReplyConfig();
        config.setContent(param.getContent());
        config.setNote(param.getNote());
        config.setUsernames(param.getUsernames().replaceAll("，", ""));
        config.setMsgType(param.getMsgType());
        config.setStartTime(param.getStartTime());
        config.setEndTime(param.getEndTime());
        boolean updated = save(config);
        if (updated) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.APP_AUTO_REPLY_CONFIG);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse delete(IdParam param) {
        boolean updated = removeById(param.getId());

        if (updated) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.APP_AUTO_REPLY_CONFIG);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

}
