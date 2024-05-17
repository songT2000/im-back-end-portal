package com.im.common.util.user;

import com.im.common.cache.impl.AdminUserCache;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.spring.SpringContextUtil;

/**
 * 用户工具类
 *
 * @author Barry
 * @date 2020-06-02
 */
public final class UserUtil {
    private static PortalUserCache portalUserCache = null;
    private static AdminUserCache adminUserCache = null;

    public static Long getUserIdByUsernameFromLocal(String username, PortalTypeEnum portalType) {
        if (portalType == PortalTypeEnum.PORTAL) {
            return getPortalUserCache().getIdByUsernameFromLocal(username);
        } else if (portalType == PortalTypeEnum.ADMIN) {
            return getAdminUserCache().getIdByUsernameFromLocal(username);
        }

        return null;
    }

    public static String getUsernameByIdFromLocal(Long userId, PortalTypeEnum portalType) {
        if (userId == null) {
            return null;
        }

        if (portalType == PortalTypeEnum.PORTAL) {
            return getPortalUserCache().getUsernameByIdFromLocal(userId);
        } else if (portalType == PortalTypeEnum.ADMIN) {
            return getAdminUserCache().getUsernameByIdFromLocal(userId);
        }

        return null;
    }

    public static String getUserNicknameByIdFromLocal(Long userId) {
        if (userId == null) {
            return null;
        }
        return getPortalUserCache().getNicknameByIdFromLocal(userId);
    }

    public static String getUserAvatarByIdFromLocal(Long userId) {
        if (userId == null) {
            return null;
        }
        return getPortalUserCache().getAvatarByIdFromLocal(userId);
    }

    public static PortalUserCache getPortalUserCache() {
        if (portalUserCache == null) {
            portalUserCache = SpringContextUtil.getBean(PortalUserCache.class);
        }
        return portalUserCache;
    }

    private static AdminUserCache getAdminUserCache() {
        if (adminUserCache == null) {
            adminUserCache = SpringContextUtil.getBean(AdminUserCache.class);
        }
        return adminUserCache;
    }
}
