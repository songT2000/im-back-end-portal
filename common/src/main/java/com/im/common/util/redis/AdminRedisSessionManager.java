package com.im.common.util.redis;

import com.alibaba.fastjson.TypeReference;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

/**
 * 管理端用户session
 *
 * @author Barry
 * @date 2020-09-30
 */
@Component
public class AdminRedisSessionManager extends RedisSessionManager {
    @Autowired
    public AdminRedisSessionManager(HashOperations<String, String, String> hashOperations,
                                    SetOperations<String, String> setOperations,
                                    StringRedisTemplate stringRedisTemplate) {
        super(hashOperations, setOperations, stringRedisTemplate);
    }

    @Override
    public RedisKeyEnum getSessionKeyPrefix() {
        return RedisKeyEnum.ADMIN_USER_SESSION;
    }

    @Override
    public Type getSessionUserClassType() {
        return new TypeReference<AdminSessionUser>() {
        }.getType();
    }

    @Override
    public AdminSessionUser getSessionUser(String token) {
        return super.getSessionUser(token);
    }

    @Override
    public AdminSessionUser getSessionUser(HttpServletRequest request) {
        return super.getSessionUser(request);
    }
}
