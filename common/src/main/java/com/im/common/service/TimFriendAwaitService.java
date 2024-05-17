package com.im.common.service;

import com.im.common.entity.tim.TimFriendAwait;
import com.im.common.util.mybatis.service.MyBatisPlusService;

public interface TimFriendAwaitService extends MyBatisPlusService<TimFriendAwait> {

    /**
     * 删除好友双方的申请信息
     * @param userId            用户ID
     * @param friendUserId      好友用户ID
     */
    void delete(Long userId,Long friendUserId);

}
