package com.im.common.cache.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.AdminUser;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.AdminUserService;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>后台管理用户缓存</p>
 *
 * @author Barry
 * @date 2019-11-20
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.ADMIN_USER, redis = false, local = true)
@Component
public class AdminUserCache implements BaseCacheHandler {
    private AdminUserService adminUserService;

    private Map<Long, String> LOCAL_ID_USERNAME_CACHE = new HashMap<>();
    private Map<String, Long> LOCAL_USERNAME_ID_CACHE = new HashMap<>();

    @Autowired
    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public String getUsernameByIdFromLocal(long id) {
        return getUsernameByIdFromLocal(id, false);
    }

    public String getUsernameByIdFromLocal(long id, boolean deepGet) {
        String username = LOCAL_ID_USERNAME_CACHE.get(id);

        if (username == null && deepGet) {
            LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getId, id);
            AdminUser user = adminUserService.getOne(wrapper);
            if (user != null) {
                username = user.getUsername();
                LOCAL_ID_USERNAME_CACHE.put(id, username);
            }
        }

        return username;
    }

    public Long getIdByUsernameFromLocal(String username) {
        return getIdByUsernameFromLocal(username, false);
    }

    public Long getIdByUsernameFromLocal(String username, boolean deepGet) {
        Long userId = LOCAL_USERNAME_ID_CACHE.get(username.toLowerCase());

        if (userId == null && deepGet) {
            LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, username);
            AdminUser user = adminUserService.getOne(wrapper);
            if (user != null) {
                userId = user.getId();
                LOCAL_USERNAME_ID_CACHE.put(username.toLowerCase(), userId);
            }
        }

        return userId;
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<AdminUser> list = adminUserService.list();

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param list List
     */
    private void resolveList(List<AdminUser> list) {
        Map<Long, String> idMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getUsername());
        Map<String, Long> usernameMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getUsername().toLowerCase(), e -> e.getId());

        this.LOCAL_ID_USERNAME_CACHE = idMap;
        this.LOCAL_USERNAME_ID_CACHE = usernameMap;
    }
}
