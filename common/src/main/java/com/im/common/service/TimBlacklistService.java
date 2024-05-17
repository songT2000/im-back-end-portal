package com.im.common.service;

import com.im.common.entity.tim.TimBlacklist;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.List;

/**
 * 用户黑名单服务
 */
public interface TimBlacklistService extends MyBatisPlusService<TimBlacklist> {

    /**
     * 添加名单单（用于回掉事件）
     * @param list
     */
    void bind(List<TimBlacklist> list);
    /**
     * 解除黑名单（用于回掉事件）
     */
    void dismiss(List<TimBlacklist> list);

    /**
     * 后台管理员添加用户黑名单
     */
    RestResponse addForAdmin(Long userId,Long blacklistUserId);


    /**
     * 后台管理员删除用户黑名单
     */
    RestResponse deleteForAdmin(Long userId,Long blacklistUserId);

    /**
     * 同步所有前一天活跃过的用户的黑名单
     */
    void sync();

    /**
     * 同步某个用户的黑名单
     * @param userId        用户ID
     */
    void sync(Long userId);
}
