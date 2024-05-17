package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.UserGroup;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.UserGroupService;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>用户组缓存</p>
 *
 * @author Barry
 * @date 2021-04-11
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.USER_GROUP, redis = false, local = true)
@Component
public class UserGroupCache implements BaseCacheHandler {
    private UserGroupService userGroupService;

    private Map<Long, String> LOCAL_ID_NAME_CACHE = new HashMap<>();

    @Autowired
    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public String getNameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_NAME_CACHE.get(id);
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<UserGroup> list = userGroupService.list();

        this.LOCAL_ID_NAME_CACHE = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getName());
    }
}
