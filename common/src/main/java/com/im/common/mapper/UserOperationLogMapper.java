package com.im.common.mapper;

import com.im.common.entity.UserOperationLog;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户操作日志表 Mapper 接口
 *
 * @author Barry
 * @date 2020-05-23
 */
@Repository
public interface UserOperationLogMapper extends MyBatisPlusMapper<UserOperationLog> {
}
