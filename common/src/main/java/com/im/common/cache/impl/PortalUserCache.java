package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.PortalUser;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.PortalUserService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>用户缓存</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.PORTAL_USER, redis = false, local = true)
@Component
public class PortalUserCache implements BaseCacheHandler {
    private PortalUserService portalUserService;

    private Map<Long, String> LOCAL_ID_USERNAME_CACHE = new HashMap<>();
    private Map<String, Long> LOCAL_USERNAME_ID_CACHE = new HashMap<>();
    private Map<Long, String> LOCAL_ID_NICKNAME_CACHE = new HashMap<>();
    private Map<Long, String> LOCAL_ID_AVATAR_CACHE = new HashMap<>();
    private Map<Long, String> LOCAL_ID_WITHDRAW_NAME_CACHE = new HashMap<>();

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public String getUsernameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_USERNAME_CACHE.get(id);
    }

    /**
     * 如果缓存获取不到，就上数据库再拿一遍
     *
     * @param id
     * @return
     */
    public String deepGetUsernameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        String username = getUsernameByIdFromLocal(id);
        if (username == null) {
            PortalUser user = getUserById(id);
            if (user != null) {
                username = user.getUsername();
                cacheUser(user);
            }
        }
        return username;
    }

    public Long getIdByUsernameFromLocal(String username) {
        return LOCAL_USERNAME_ID_CACHE.get(username.toLowerCase());
    }

    /**
     * 如果缓存获取不到，就上数据库再拿一遍
     *
     * @param username
     * @return
     */
    public Long deepGetIdByUsernameFromLocal(String username) {
        Long userId = getIdByUsernameFromLocal(username);
        if (userId == null) {
            PortalUser user = getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
                cacheUser(user);
            }
        }
        return userId;
    }

    public String getNicknameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_NICKNAME_CACHE.get(id);
    }

    public String getAvatarByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_AVATAR_CACHE.get(id);
    }

    public String getWithdrawNameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_WITHDRAW_NAME_CACHE.get(id);
    }

    private PortalUser getUserById(Long id) {
        PortalUser user = portalUserService
                .lambdaQuery()
                .select(PortalUser::getId, PortalUser::getUsername, PortalUser::getNickname, PortalUser::getAvatar, PortalUser::getWithdrawName)
                .eq(PortalUser::getId, id)
                .one();
        return user;
    }

    private PortalUser getUserByUsername(String username) {
        PortalUser user = portalUserService
                .lambdaQuery()
                .select(PortalUser::getId, PortalUser::getUsername, PortalUser::getNickname, PortalUser::getAvatar, PortalUser::getWithdrawName)
                .eq(PortalUser::getUsername, username)
                .one();
        return user;
    }

    private void cacheUser(PortalUser user) {
        LOCAL_ID_USERNAME_CACHE.put(user.getId(), user.getUsername());
        LOCAL_USERNAME_ID_CACHE.put(user.getUsername().toLowerCase(), user.getId());
        if (StrUtil.isNotBlank(user.getNickname())) {
            LOCAL_ID_NICKNAME_CACHE.put(user.getId(), user.getNickname());
        }
        if (StrUtil.isNotBlank(user.getAvatar())) {
            LOCAL_ID_AVATAR_CACHE.put(user.getId(), user.getAvatar());
        }
        if (StrUtil.isNotBlank(user.getWithdrawName())) {
            LOCAL_ID_WITHDRAW_NAME_CACHE.put(user.getId(), user.getWithdrawName());
        }
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        // 只查需要的字段，避免每次加载太多数据
        List<PortalUser> list = portalUserService.lambdaQuery()
                .select(PortalUser::getId, PortalUser::getUsername, PortalUser::getNickname, PortalUser::getAvatar, PortalUser::getWithdrawName)
                .list();

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param list List
     */
    private void resolveList(List<PortalUser> list) {
        Map<Long, String> idMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getUsername());
        Map<String, Long> usernameMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getUsername().toLowerCase(), e -> e.getId());
        Map<Long, String> nicknameMap = CollectionUtil.toMapWithKeyValue(list, e -> StrUtil.isNotBlank(e.getNickname()), e -> e.getId(), e -> e.getNickname());
        Map<Long, String> avatarMap = CollectionUtil.toMapWithKeyValue(list, e -> StrUtil.isNotBlank(e.getAvatar()), e -> e.getId(), e -> e.getAvatar());
        Map<Long, String> withdrawNameMap = CollectionUtil.toMapWithKeyValue(list, e -> StrUtil.isNotBlank(e.getWithdrawName()), e -> e.getId(), e -> e.getWithdrawName());

        LOCAL_ID_USERNAME_CACHE = idMap;
        LOCAL_USERNAME_ID_CACHE = usernameMap;
        LOCAL_ID_NICKNAME_CACHE = nicknameMap;
        LOCAL_ID_AVATAR_CACHE = avatarMap;
        LOCAL_ID_WITHDRAW_NAME_CACHE = withdrawNameMap;
    }
}
