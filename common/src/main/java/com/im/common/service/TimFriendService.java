package com.im.common.service;

import com.im.common.entity.tim.TimFriend;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.List;

public interface TimFriendService extends MyBatisPlusService<TimFriend> {
    /**
     * @param userId
     * @return
     */
    TimFriend getByUserId(long userId, long friendUserId);

    /**
     * 从回掉事件中批量处理好友事件
     */
    void batchHandler(List<TimFriend> list);

    /**
     * 解除双方的好友关系（不支持单方面删除好友）
     * @param userId            用户ID
     * @param friendUserId      好友ID
     */
    void delete(Long userId,Long friendUserId);

    /**
     * 管理员新增某个用户的好友
     * @param userId            用户ID
     * @param friendUserId      好友用户ID
     */
    RestResponse addForAdmin(Long userId,Long friendUserId);

    /**
     * 管理员删除某个用户的好友
     * @param userId            用户ID
     * @param friendUserId      好友用户ID
     */
    RestResponse deleteForAdmin(Long userId,Long friendUserId);

    /**
     * 管理员删除某个用户所有的好友
     * @param userId            用户ID
     */
    RestResponse deleteAllForAdmin(Long userId);

    /**
     * 批量解除好友关系
     */
    void batchDelete(List<TimFriend> list);

    /**
     * 同步所有前一天活跃过的用户的关系链
     */
    void sync();

    /**
     * 同步某个用户的关系链
     * @param userId        用户ID
     */
    void sync(Long userId);

}
