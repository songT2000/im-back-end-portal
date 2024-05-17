package com.im.common.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.AdminRole;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.AdminRoleService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.i18n.I18nContext;
import com.im.common.util.i18n.I18nTranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>角色缓存</p>
 *
 * @author Barry
 * @date 2019-11-20
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.ADMIN_ROLE, redis = false, local = true)
@Component
public class AdminRoleCache implements BaseCacheHandler {
    private AdminRoleService adminRoleService;

    private Map<Long, String> LOCAL_ID_NAME_CACHE = new HashMap<>();

    @Autowired
    public void setAdminRoleService(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public String getNameByIdFromLocal(long id) {
        return getNameByIdFromLocal(id, false);
    }

    public String getNameByIdFromLocal(long id, boolean deepGet) {
        String value = LOCAL_ID_NAME_CACHE.get(id);

        if (value == null && deepGet) {
            LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<AdminRole>().eq(AdminRole::getId, id);
            AdminRole one = adminRoleService.getOne(wrapper);
            if (one != null) {
                if (I18nContext.hasLanguage()) {
                    return one.getName();
                } else {
                    // 这里是I18N值，可以用来翻译
                    value = one.getName();
                    LOCAL_ID_NAME_CACHE.put(id, value);
                }
            }
        }

        if (StrUtil.isNotBlank(value)) {
            return I18nTranslateUtil.translate(value);
        }
        return value;
    }


    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<AdminRole> list = adminRoleService.list();

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param list List
     */
    private void resolveList(List<AdminRole> list) {
        Map<Long, String> idMap = CollectionUtil.toMapWithKeyValue(list, AdminRole::getId, role -> role.getName());

        this.LOCAL_ID_NAME_CACHE = idMap;
    }
}
