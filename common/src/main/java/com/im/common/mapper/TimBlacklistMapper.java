package com.im.common.mapper;

import com.im.common.entity.tim.TimBlacklist;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户黑名单 Mapper 接口
 */
@Repository
public interface TimBlacklistMapper extends MyBatisPlusMapper<TimBlacklist> {
}
