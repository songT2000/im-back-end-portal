package com.im.common.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.AdminMenu;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.AdminMenuService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.RequestUtil;
import com.im.common.util.i18n.I18nTranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>菜单缓存</p>
 *
 * @author Barry
 * @date 2019-11-20
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.ADMIN_MENU, redis = false, local = true)
@Component
public class AdminMenuCache implements BaseCacheHandler {
    private AdminMenuService adminMenuService;

    private Map<Long, String> LOCAL_ID_NAME_CACHE = new HashMap<>();
    private Map<String, String> LOCAL_CODE_NAME_CACHE = new HashMap<>();
    private Map<String, String> LOCAL_URL_NAME_CACHE = new HashMap<>();
    private Map<String, Long> LOCAL_CODE_ID_CACHE = new HashMap<>();
    private Map<String, Long> LOCAL_URL_ID_CACHE = new HashMap<>();
    private Map<Long, Long> LOCAL_ID_PARENT_ID_CACHE = new HashMap<>();

    @Autowired
    public void setAdminMenuService(AdminMenuService adminMenuService) {
        this.adminMenuService = adminMenuService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public String getNameByIdFromLocal(long id) {
        String value = LOCAL_ID_NAME_CACHE.get(id);

        if (StrUtil.isNotBlank(value)) {
            return I18nTranslateUtil.translate(value);
        }
        return value;
    }

    public String getNameByCodeFromLocal(String code) {
        String value = LOCAL_CODE_NAME_CACHE.get(code);

        if (StrUtil.isNotBlank(value)) {
            return I18nTranslateUtil.translate(value);
        }
        return value;
    }

    public String getNameByUrlFromLocal(String url) {
        String value = LOCAL_URL_NAME_CACHE.get(url);

        if (StrUtil.isNotBlank(value)) {
            return I18nTranslateUtil.translate(value);
        }
        return value;
    }

    public Long getIdByCodeFromLocal(String code) {
        return LOCAL_CODE_ID_CACHE.get(code);
    }

    public Long getIdByUrlFromLocal(String url) {
        return LOCAL_URL_ID_CACHE.get(url);
    }

    public Long getParentIdByIdFromLocal(long id) {
        return LOCAL_ID_PARENT_ID_CACHE.get(id);
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<AdminMenu> list = adminMenuService.list();

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param list List
     */
    private void resolveList(List<AdminMenu> list) {
        Map<Long, String> idNameMap = CollectionUtil.toMapWithKeyValue(list, AdminMenu::getId, menu -> menu.getName().toString());
        Map<String, String> codeNameMap = CollectionUtil.toMapWithKeyValue(list, AdminMenu::getCode, menu -> menu.getName().toString());
        Map<String, String> urlNameMap = CollectionUtil.toMapWithKeyValue(list,
                menu -> StrUtil.isNotBlank(menu.getUrl()),
                menu -> RequestUtil.formatUrlPath(menu.getUrl()), menu -> menu.getName().toString());
        Map<String, Long> codeIdMap = CollectionUtil.toMapWithKeyValue(list, AdminMenu::getCode, AdminMenu::getId);
        Map<String, Long> urlIdMap = CollectionUtil.toMapWithKeyValue(list,
                menu -> StrUtil.isNotBlank(menu.getUrl()),
                menu -> RequestUtil.formatUrlPath(menu.getUrl()), AdminMenu::getId);

        Map<Long, Long> idParentIdMap = CollectionUtil.toMapWithKeyValue(list, AdminMenu::getId, AdminMenu::getParentId);

        this.LOCAL_ID_NAME_CACHE = idNameMap;
        this.LOCAL_CODE_NAME_CACHE = codeNameMap;
        this.LOCAL_URL_NAME_CACHE = urlNameMap;
        this.LOCAL_CODE_ID_CACHE = codeIdMap;
        this.LOCAL_URL_ID_CACHE = urlIdMap;
        this.LOCAL_ID_PARENT_ID_CACHE = idParentIdMap;
    }
}
