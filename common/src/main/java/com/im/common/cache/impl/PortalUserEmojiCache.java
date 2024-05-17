package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.PortalUserEmoji;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.PortalUserEmojiService;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户自定义表情包
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.PORTAL_USER_EMOJI, redis = false, local = true)
@Component
public class PortalUserEmojiCache implements BaseCacheHandler {
    private PortalUserEmojiService portalUserEmojiService;
    private Map<Long, List<PortalUserEmoji>> LOCAL_ID_EMOJI_CACHE = new HashMap<>();

    @Autowired
    public void setPortalUserEmojiService(PortalUserEmojiService portalUserEmojiService) {
        this.portalUserEmojiService = portalUserEmojiService;
    }

    @Override
    public void reloadLocal() {
        List<PortalUserEmoji> list = portalUserEmojiService.lambdaQuery().orderByDesc(PortalUserEmoji::getCreateTime).list();

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 查询用户所有自定义表情包
     */
    public List<PortalUserEmoji> getPortalUserEmojiList(Long userId) {
        if (userId == null) {
            return null;
        }
        return LOCAL_ID_EMOJI_CACHE.get(userId);
    }

    /**
     * 解析并缓存数据
     */
    private void resolveList(List<PortalUserEmoji> list) {
        // 分组
        Map<Long, List<PortalUserEmoji>> idMap = CollectionUtil.toMapList(list, PortalUserEmoji::getUserId);

        LOCAL_ID_EMOJI_CACHE = idMap;
    }
}
