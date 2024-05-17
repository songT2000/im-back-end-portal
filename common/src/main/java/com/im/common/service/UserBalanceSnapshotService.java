package com.im.common.service;

import com.im.common.entity.PortalUser;
import com.im.common.entity.UserBalanceSnapshot;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 用户余额快照
 *
 * @author max.stark
 */
public interface UserBalanceSnapshotService extends MyBatisPlusService<UserBalanceSnapshot> {
    /**
     * 新增余额快照
     *
     * @param user
     * @param date
     */
    void add(PortalUser user, String date);
}
