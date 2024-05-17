package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.service.TimGroupService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>群组缓存</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.TIM_GROUP, redis = false, local = true)
@Component
public class TimGroupCache implements BaseCacheHandler {
    private TimGroupService timGroupService;

    private Map<Long, String> LOCAL_SYS_ID_NAME_CACHE = new HashMap<>();
    private Map<String, String> LOCAL_TIM_ID_NAME_CACHE = new HashMap<>();

    private Map<Long, String> LOCAL_SYS_ID_TIM_ID_CACHE = new HashMap<>();
    private Map<String, Long> LOCAL_TIM_ID_SYS_ID_CACHE = new HashMap<>();

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public String getNameBySysIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_SYS_ID_NAME_CACHE.get(id);
    }

    public String getNameByTimIdFromLocal(String id) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        return LOCAL_TIM_ID_NAME_CACHE.get(id);
    }

    public String getTimIdBySysIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_SYS_ID_TIM_ID_CACHE.get(id);
    }

    public Long getSysIdByTimIdFromLocal(String id) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        return LOCAL_TIM_ID_SYS_ID_CACHE.get(id);
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        // 只查需要的字段，避免每次加载太多数据
        List<TimGroup> list = timGroupService.lambdaQuery()
                .select(TimGroup::getId, TimGroup::getGroupId, TimGroup::getGroupName)
                .list();

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param list List
     */
    private void resolveList(List<TimGroup> list) {
        Map<Long, String> sysIdNameMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getGroupName());
        Map<String, String> timIdNameMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getGroupId(), e -> e.getGroupName());
        Map<Long, String> sysIdTimIdMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getGroupId());
        Map<String, Long> timIdSysIdMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getGroupId(), e -> e.getId());

        LOCAL_SYS_ID_NAME_CACHE = sysIdNameMap;
        LOCAL_TIM_ID_NAME_CACHE = timIdNameMap;
        LOCAL_SYS_ID_TIM_ID_CACHE = sysIdTimIdMap;
        LOCAL_TIM_ID_SYS_ID_CACHE = timIdSysIdMap;
    }
}
