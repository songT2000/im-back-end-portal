package com.im.common.mapper;

import com.im.common.entity.UserBalanceSnapshot;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;


/**
 * 余额快照 Mapper 接口
 * @author max.stark
 */
@Repository
public interface UserBalanceSnapshotMapper extends MyBatisPlusMapper<UserBalanceSnapshot> {
}
