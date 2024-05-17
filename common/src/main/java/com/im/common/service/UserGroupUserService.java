package com.im.common.service;

import com.im.common.entity.UserGroupUser;
import com.im.common.param.UserGroupEditUserAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.Set;

/**
 * 用户组内用户 服务类
 *
 * @author Barry
 * @date 2021-04-11
 */
public interface UserGroupUserService extends MyBatisPlusService<UserGroupUser> {
    /**
     * 编辑
     *
     * @param param
     * @return
     */
    RestResponse editForAdmin(UserGroupEditUserAdminParam param);

    /**
     * 编辑
     *
     * @param userId
     * @param userGroupIds
     */
    void adjustUserGroup(long userId, Set<Long> userGroupIds);
}
