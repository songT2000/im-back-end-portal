package com.im.common.util.redis;

import com.alibaba.fastjson.TypeReference;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.vo.PortalSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

/**
 * 买家session
 *
 * @author Barry
 * @date 2020-09-30
 */
@Component
public class PortalRedisSessionManager extends RedisSessionManager {
    @Autowired
    public PortalRedisSessionManager(HashOperations<String, String, String> hashOperations,
                                     SetOperations<String, String> setOperations,
                                     StringRedisTemplate stringRedisTemplate) {
        super(hashOperations, setOperations, stringRedisTemplate);
    }

    @Override
    public RedisKeyEnum getSessionKeyPrefix() {
        return RedisKeyEnum.PORTAL_USER_SESSION;
    }

    @Override
    public Type getSessionUserClassType() {
        return new TypeReference<PortalSessionUser>() {
        }.getType();
    }

    @Override
    public PortalSessionUser getSessionUser(String token) {
        return super.getSessionUser(token);
    }

    @Override
    public PortalSessionUser getSessionUser(HttpServletRequest request) {
        return super.getSessionUser(request);
    }
}
