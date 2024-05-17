package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.UserGroupCache;
import com.im.common.entity.UserGroupUser;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.UserGroupUserMapper;
import com.im.common.param.UserGroupEditUserAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserGroupUserService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户组内用户 服务实现类
 *
 * @author Barry
 * @date 2021-04-11
 */
@Service
public class UserGroupUserServiceImpl
        extends MyBatisPlusServiceImpl<UserGroupUserMapper, UserGroupUser>
        implements UserGroupUserService {
    private UserGroupCache userGroupCache;
    private CacheProxy cacheProxy;

    @Autowired
    public void setUserGroupCache(UserGroupCache userGroupCache) {
        this.userGroupCache = userGroupCache;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(UserGroupEditUserAdminParam param) {
        // 组必须存在
        String groupName = userGroupCache.getNameByIdFromLocal(param.getGroupId());
        if (StrUtil.isBlank(groupName)) {
            return RestResponse.failed(ResponseCode.USER_GROUP_NOT_FOUND);
        }

        // 删除
        if (CollectionUtil.isNotEmpty(param.getDeleteList())) {
            List<Long> deleteUserIdList = new ArrayList<>();

            for (String username : param.getDeleteList()) {
                Long userId = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
                if (userId == null) {
                    return RestResponse.failed(ResponseCode.USER_NOT_FOUND, username);
                }
                deleteUserIdList.add(userId);
            }

            lambdaUpdate()
                    .eq(UserGroupUser::getGroupId, param.getGroupId())
                    .in(UserGroupUser::getUserId, deleteUserIdList)
                    .remove();
        }

        // 新增
        if (CollectionUtil.isNotEmpty(param.getAddList())) {
            List<UserGroupUser> addList = new ArrayList<>();

            for (String username : param.getAddList()) {
                Long userId = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
                if (userId == null) {
                    return RestResponse.failed(ResponseCode.USER_NOT_FOUND, username);
                }

                Integer count = lambdaQuery()
                        .eq(UserGroupUser::getGroupId, param.getGroupId())
                        .eq(UserGroupUser::getUserId, userId)
                        .count();
                if (NumberUtil.isGreatThenZero(count)) {
                    return RestResponse.failed(ResponseCode.USER_GROUP_USER_EXISTED, username);
                }

                addList.add(new UserGroupUser(param.getGroupId(), userId));
            }

            boolean saved = saveBatch(addList);
            if (!saved) {
                return RestResponse.SYS_DATA_STATUS_ERROR;
            }
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_USER);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustUserGroup(long userId, Set<Long> userGroupIds) {
        if (CollectionUtil.isEmpty(userGroupIds)) {
            // 退出所有组
            lambdaUpdate().eq(UserGroupUser::getUserId, userId).remove();
        } else {
            // 先找出原来的数据
            List<UserGroupUser> oldList = lambdaQuery().eq(UserGroupUser::getUserId, userId).list();
            Set<Long> oldGroupIdSet = CollectionUtil.toSet(oldList, e -> e.getGroupId());

            // 需要删除的
            {
                Set<Long> deleteGroupIdSet = CollectionUtil.difference(userGroupIds, oldGroupIdSet);
                if (CollectionUtil.isNotEmpty(deleteGroupIdSet)) {
                    lambdaUpdate().eq(UserGroupUser::getUserId, userId).in(UserGroupUser::getGroupId, deleteGroupIdSet).remove();
                }
            }

            // 需要新增的
            {
                Set<Long> addGroupIdSet = CollectionUtil.difference(oldGroupIdSet, userGroupIds);
                if (CollectionUtil.isNotEmpty(addGroupIdSet)) {
                    List<UserGroupUser> list = CollectionUtil.toList(addGroupIdSet, e -> new UserGroupUser(e, userId));
                    saveBatch(list);
                }
            }
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_USER);
    }
}