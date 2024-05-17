package com.im.common.util.redis;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.constant.RedisSessionKeyEnum;
import com.im.common.response.ResponseCode;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.jwt.JwtInfo;
import com.im.common.util.jwt.JwtUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 模拟Session，实际存到redis
 *
 * @author Barry
 */
public abstract class RedisSessionManager {
    private static final Log LOG = LogFactory.get();

    /**
     * redis session过期分钟
     **/
    public static final Long REDIS_SESSION_EXPIRE_MINUTES = 30L;

    private HashOperations<String, String, String> hashOperations;
    private SetOperations<String, String> setOperations;
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisSessionManager(HashOperations<String, String, String> hashOperations,
                               SetOperations<String, String> setOperations,
                               StringRedisTemplate stringRedisTemplate) {
        this.hashOperations = hashOperations;
        this.setOperations = setOperations;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 当前session redis key前缀
     *
     * @return
     */
    public abstract RedisKeyEnum getSessionKeyPrefix();

    /**
     * 当前session是处理哪个用户类的
     *
     * @return
     */
    public abstract Type getSessionUserClassType();

    /**
     * 获取token session的 key，这里存放实际用户信息
     *
     * @param token token
     * @return
     */
    public String getTokenSessionKey(String token) {
        return getSessionKeyPrefix().getVal() + ":" + token;
    }

    /**
     * 获取用户session的key，这里存放所有在线的token
     *
     * @param token token
     * @return
     */
    public String getUserSessionKeyByToken(String token) {
        JwtInfo jwtInfo = JwtUtil.getJwtInfo(token);
        String username = jwtInfo.getUsername();

        return getUserSessionKeyByUsername(username);
    }

    /**
     * 获取用户session的key，这里存放所有在线的token
     *
     * @param username 用户名
     * @return
     */
    public String getUserSessionKeyByUsername(String username) {
        return "TOKEN_CONTAINER:" + getSessionKeyPrefix().getVal() + ":" + username;
    }

    /**
     * 往session中设置值，如果session存在的话
     *
     * @param token token
     * @param key   hash key
     * @param value 要设置的值
     */
    private void setSessionAttributeIfHasSession(String token, String key, String value) {
        String tokenSessionKey = getTokenSessionKey(token);

        Boolean hasSession = stringRedisTemplate.hasKey(tokenSessionKey);
        if (Boolean.TRUE.equals(hasSession)) {

            hashOperations.put(tokenSessionKey, key, value);
            // 重新设置token会话过期时间
            stringRedisTemplate.expire(tokenSessionKey, REDIS_SESSION_EXPIRE_MINUTES, TimeUnit.MINUTES);

            // 重新设置用户会话过期时间
            String userSessionKey = getUserSessionKeyByToken(token);
            stringRedisTemplate.expire(userSessionKey, REDIS_SESSION_EXPIRE_MINUTES, TimeUnit.MINUTES);
        }
    }

    /**
     * 往session中设置值，如果session不存在，则会创建
     *
     * @param token token
     * @param key   hash key
     * @param value 要设置的值
     */
    private void setSessionAttributeOrCreate(String token, String key, String value) {
        // 设置token会话
        String tokenSessionKey = getTokenSessionKey(token);
        hashOperations.put(tokenSessionKey, key, value);
        // 设置token会话过期时间
        stringRedisTemplate.expire(tokenSessionKey, REDIS_SESSION_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 设置用户会话
        String userSessionKey = getUserSessionKeyByToken(token);
        setOperations.add(userSessionKey, token);
        // 设置用户会话过期时间
        stringRedisTemplate.expire(userSessionKey, REDIS_SESSION_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    private Object getSessionAttribute(String token, String key) {
        return hashOperations.get(getTokenSessionKey(token), key);
    }

    public <T extends RedisSessionUser> void setSessionUser(String token, T sessionUser) {
        setSessionAttributeOrCreate(token, RedisSessionKeyEnum.USER.getVal(), JSON.toJSONString(sessionUser));
        refreshSession(token);
    }

    /**
     * 获取当前登录用户
     *
     * @param token token
     * @return RedisSessionUser
     */
    public <T extends RedisSessionUser> T getSessionUser(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }

        String sessionUserStr = (String) getSessionAttribute(token, RedisSessionKeyEnum.USER.getVal());
        return JSON.parseObject(sessionUserStr, getSessionUserClassType());
    }

    /**
     * 获取当前登录用户，/api/auth开头的方法不需要判断是否为空，拦截器会处理，非/api/auth则需要判断空的情况
     *
     * @param request HttpServletRequest
     * @return RedisSessionUser
     */
    public <T extends RedisSessionUser> T getSessionUser(HttpServletRequest request) {
        String token = JwtUtil.getTokenFromHeader(request);

        if (StrUtil.isBlank(token)) {
            return null;
        }

        return getSessionUser(token);
    }

    public void refreshSession(String token) {
        setSessionAttributeIfHasSession(token, RedisSessionKeyEnum.LAST_ACCESS_TIME.getVal(), DateTimeUtil.getNowDateTimeStr());
    }

    public void refreshSession(HttpServletRequest request) {
        String token = JwtUtil.getTokenFromHeader(request);
        if (StrUtil.isNotBlank(token)) {
            refreshSession(token);
        }
    }

    public void removeSession(String token) {
        // 删除token会话
        String tokenSessionKey = getTokenSessionKey(token);
        stringRedisTemplate.delete(tokenSessionKey);

        // 删除用户会话中的token
        String userSessionKey = getUserSessionKeyByToken(token);
        setOperations.remove(userSessionKey, token);
    }

    public Set<String> listAllTokenByUsername(String username) {
        String userSessionKey = getUserSessionKeyByUsername(username);

        Set<String> members = setOperations.members(userSessionKey);
        return members;
    }

    public Set<String> listAllTokenByToken(String token) {
        JwtInfo jwtInfo = JwtUtil.getJwtInfo(token);
        return listAllTokenByUsername(jwtInfo.getUsername());
    }

    /**
     * 往session中设置登出消息，注意这个不是登出方法，用户主动登出请调用userService.logout，强制登出调userService.kickOut
     * 当session不存在时，是不会设置的
     *
     * @param token      token
     * @param logoutCode 登出消息
     */
    public void setLogoutCode(String token, ResponseCode logoutCode) {
        // 往token会话中设置logout code
        setSessionAttributeIfHasSession(token, RedisSessionKeyEnum.LOGOUT_CODE.getVal(), logoutCode.name());

        // 删除用户会话中的token
        String userSessionKey = getUserSessionKeyByToken(token);
        setOperations.remove(userSessionKey, token);
    }

    public String getLogoutCode(String token) {
        return (String) getSessionAttribute(token, RedisSessionKeyEnum.LOGOUT_CODE.getVal());
    }
}
