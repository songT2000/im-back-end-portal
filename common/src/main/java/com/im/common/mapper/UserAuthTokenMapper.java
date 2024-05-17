package com.im.common.mapper;

import com.im.common.entity.UserAuthToken;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 前台登录token表 Mapper 接口
 *
 * @author Barry
 * @date 2019/10/11
 */
@Repository
public interface UserAuthTokenMapper extends MyBatisPlusMapper<UserAuthToken> {
}
