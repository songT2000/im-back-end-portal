package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.im.common.entity.tim.TimFriendAwait;
import com.im.common.mapper.TimFriendAwaitMapper;
import com.im.common.service.TimFriendAwaitService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimFriendAwaitServiceImpl extends MyBatisPlusServiceImpl<TimFriendAwaitMapper, TimFriendAwait> implements TimFriendAwaitService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long userId, Long friendUserId) {
        LambdaUpdateWrapper<TimFriendAwait> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TimFriendAwait::getUserId,userId)
                .eq(TimFriendAwait::getFriendUserId,friendUserId);
        remove(updateWrapper);
        updateWrapper.eq(TimFriendAwait::getUserId,friendUserId)
                .eq(TimFriendAwait::getFriendUserId,userId);
        remove(updateWrapper);
    }
}
