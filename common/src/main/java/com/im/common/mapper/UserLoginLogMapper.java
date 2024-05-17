package com.im.common.mapper;

import com.im.common.entity.UserLoginLog;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户登录日志表 Mapper 接口
 *
 * @author Barry
 * @date 2020-05-23
 */
@Repository
public interface UserLoginLogMapper extends MyBatisPlusMapper<UserLoginLog> {
}
