package com.im.common.service.impl;

import com.im.common.entity.PortalUser;
import com.im.common.entity.UserBalanceSnapshot;
import com.im.common.mapper.UserBalanceSnapshotMapper;
import com.im.common.service.UserBalanceSnapshotService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户余额快照 服务类
 *
 * @author max.stark
 */
@Service
public class UserBalanceSnapshotServiceImpl extends MyBatisPlusServiceImpl<UserBalanceSnapshotMapper, UserBalanceSnapshot>
        implements UserBalanceSnapshotService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PortalUser user, String date) {
        UserBalanceSnapshot balanceSnapshot = lambdaQuery()
                .eq(UserBalanceSnapshot::getUserId, user.getId())
                .eq(UserBalanceSnapshot::getDate, date).one();

        if (balanceSnapshot == null) {
            balanceSnapshot = new UserBalanceSnapshot();
            balanceSnapshot.setUserId(user.getId());
            balanceSnapshot.setDate(date);
            balanceSnapshot.setBalance(user.getBalance());
            save(balanceSnapshot);
        } else {
            lambdaUpdate()
                    .eq(UserBalanceSnapshot::getId, balanceSnapshot.getId())
                    .set(UserBalanceSnapshot::getBalance, user.getBalance())
                    .update();
        }
    }
}
