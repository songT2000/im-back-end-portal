package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.UserGroupUser;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.UserGroupUserService;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>用户组内用户缓存</p>
 *
 * @author Barry
 * @date 2021-04-11
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.USER_GROUP_USER, redis = false, local = true)
@Component
public class UserGroupUserCache implements BaseCacheHandler {
    private UserGroupUserService userGroupUserService;

    /**
     * 每个人的用户组<用户ID, [组ID列表]>
     */
    private Map<Long, Set<Long>> LOCAL_USER_ID_GROUP_ID_SET_CACHE = new HashMap<>();

    @Autowired
    public void setUserGroupUserService(UserGroupUserService userGroupUserService) {
        this.userGroupUserService = userGroupUserService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public Set<Long> getGroupSetByUserIdFromLocal(Long userId) {
        if (userId == null) {
            return new TreeSet<>();
        }
        return Optional.ofNullable(LOCAL_USER_ID_GROUP_ID_SET_CACHE.get(userId)).orElse(new TreeSet<>());
    }

    /**
     * 判断该用户是否拥有这些组的任意一个
     *
     * @param userId
     * @param dataGroupSet 数据的用户组
     * @return
     */
    public boolean hasAnyGroupByUserIdFromLocal(Long userId, Set<Long> dataGroupSet) {
        if (userId == null || CollectionUtil.isEmpty(dataGroupSet)) {
            return false;
        }
        Set<Long> userGroupSet = getGroupSetByUserIdFromLocal(userId);
        if (CollectionUtil.isEmpty(userGroupSet)) {
            return false;
        }

        boolean hasAnyGroup = CollectionUtil.anyMatch(dataGroupSet, e -> userGroupSet.contains(e));
        return hasAnyGroup;
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<UserGroupUser> userGroupUserList = userGroupUserService.list();
        if (CollectionUtil.isEmpty(userGroupUserList)) {
            this.LOCAL_USER_ID_GROUP_ID_SET_CACHE = new HashMap<>();
            return;
        }

        // <用户ID, <组ID列表>>
        Map<Long, Set<Long>> userIdGroupIdSetTmp = new HashMap<>();

        // 单个用户
        for (UserGroupUser userGroupUser : userGroupUserList) {
            Long userId = userGroupUser.getUserId();
            Long groupId = userGroupUser.getGroupId();

            userIdGroupIdSetTmp.compute(userId, (key, value) -> {
                value = Optional.ofNullable(value).orElse(new TreeSet<>());
                value.add(groupId);
                return value;
            });
        }

        this.LOCAL_USER_ID_GROUP_ID_SET_CACHE = userIdGroupIdSetTmp;
    }
}
