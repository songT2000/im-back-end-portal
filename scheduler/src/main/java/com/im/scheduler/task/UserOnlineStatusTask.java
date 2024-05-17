package com.im.scheduler.task;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.im.common.constant.RedisSessionKeyEnum;
import com.im.common.entity.AdminUser;
import com.im.common.entity.PortalUser;
import com.im.common.entity.UserAuthToken;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.service.AdminUserService;
import com.im.common.service.PortalUserService;
import com.im.common.service.UserAuthTokenService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.redis.RedisSessionManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户在线状态同步任务
 *
 * @author Barry
 * @date 2021-01-19
 */
@Component
public class UserOnlineStatusTask {
    private static final Log LOG = LogFactory.get();
    private static final String TASK_NAME = "用户在线状态同步任务";

    private UserAuthTokenService userAuthTokenService;
    private PortalUserService portalUserService;
    private AdminUserService adminUserService;
    private HashOperations<String, String, String> redisHash;
    private RedisSessionManagerFactory redisSessionManagerFactory;

    @Autowired
    public void setUserAuthTokenService(UserAuthTokenService userAuthTokenService) {
        this.userAuthTokenService = userAuthTokenService;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Autowired
    public void setRedisHash(HashOperations<String, String, String> redisHash) {
        this.redisHash = redisHash;
    }

    @Autowired
    public void setRedisSessionManagerFactory(RedisSessionManagerFactory redisSessionManagerFactory) {
        this.redisSessionManagerFactory = redisSessionManagerFactory;
    }

    @Scheduled(fixedRate = 60 * 1000)
    private void task() {
        start();
    }

    private void start() {
        try {
            startSync();
        } catch (Exception e) {
            LOG.error(e, "执行[{}]失败", TASK_NAME);
        }
    }

    /**
     * 开始执行
     */
    private void startSync() throws Exception {
        // 移除已经过期的token
        List<UserAuthToken> expiredTokens = userAuthTokenService.listExpiredUserAuthToken();
        Optional.ofNullable(expiredTokens).ifPresent(this::deleteExpiredToken);

        // 如果在线用户在redis中不存在，则把用户修改为离线，但不删除token
        compareOnlineUserWithRedis();
    }

    private void compareOnlineUserWithRedis() {
        // {
        //     List<Long> onlineUserIds = portalUserService.listOnlineUserId();
        //     compareOnlineUserWithRedis(onlineUserIds, PortalTypeEnum.PORTAL);
        // }

        {
            List<Long> onlineUserIds = adminUserService.listOnlineUserId();
            compareOnlineUserWithRedis(onlineUserIds, PortalTypeEnum.ADMIN);
        }
    }

    private void compareOnlineUserWithRedis(List<Long> onlineUserIds, PortalTypeEnum portalType) {
        if (CollectionUtil.isEmpty(onlineUserIds)) {
            return;
        }
        // 列出所有redis token
        List<Map<String, String>> redisSessions = new ArrayList<>();
        String tokenSessionScanKey = redisSessionManagerFactory.getSessionManager(portalType).getTokenSessionKey("*");
        redisHash.getOperations().execute((RedisCallback<Object>) connection -> {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(tokenSessionScanKey).count(100).build());
            while (cursor.hasNext()) {
                byte[] next = cursor.next();

                // 获取session中存储的数据
                Map<String, String> entries = redisHash.entries(new String(next));
                redisSessions.add(entries);
            }

            return null;
        });

        // 实际离线用户
        Set<Long> offlineUserIds = new HashSet<>();

        // 用户session
        Map<Long, List<Map<String, String>>> userSessions = new HashMap<>(10);
        for (Map<String, String> redisSession : redisSessions) {
            // session中的用户信息
            String userInfo = redisSession.get(RedisSessionKeyEnum.USER.getVal());

            if (StrUtil.isNotBlank(userInfo)) {
                JSONObject jsonObject = JSONObject.parseObject(userInfo);
                Object userIdObj = jsonObject.get("id");
                if (userIdObj != null) {
                    Long userId = Long.valueOf(userIdObj.toString());
                    if (!userSessions.containsKey(userId)) {
                        userSessions.put(userId, new ArrayList<>());
                    }
                    userSessions.get(userId).add(redisSession);
                }
            }
        }

        // 开始比对
        for (Long onlineUserId : onlineUserIds) {

            // 该用户的所有session
            List<Map<String, String>> userSessionMap = userSessions.get(onlineUserId);

            // 是否还有有效session
            boolean hasValidSession = false;

            if (CollectionUtil.isNotEmpty(userSessionMap)) {
                // 如果没有有效session了，把用户状态设置为离线
                for (Map<String, String> userSession : userSessionMap) {
                    String userInfo = userSession.get(RedisSessionKeyEnum.USER.getVal());
                    if (StrUtil.isNotBlank(userInfo)) {
                        String logoutCode = userSession.get(RedisSessionKeyEnum.LOGOUT_CODE.getVal());
                        if (StrUtil.isBlank(logoutCode)) {
                            hasValidSession = true;
                            break;
                        }
                    }

                }
            }

            // 如果没有有效session了，把用户状态设置为离线
            if (!hasValidSession) {
                offlineUserIds.add(onlineUserId);
            }
        }

        // 下线登录失效的用户
        if (CollectionUtil.isNotEmpty(offlineUserIds)) {
            updateUserOffline(offlineUserIds, portalType);

            LOG.info("{}，下线登录失效={}，portalType={}", TASK_NAME, CollectionUtil.join(offlineUserIds, ","), portalType.getVal());
        }
    }

    /**
     * 删除过期token
     *
     * @param expiredTokens 过期token集合
     */
    private void deleteExpiredToken(List<UserAuthToken> expiredTokens) {
        for (UserAuthToken expiredToken : expiredTokens) {
            // 物理删除token
            userAuthTokenService.removeById(expiredToken.getId());

            LOG.info("{}，删除过期token={}", TASK_NAME, JSON.toJSONString(expiredToken));
        }
    }

    private void updateUserOffline(Set<Long> offlineUserIds, PortalTypeEnum portalType) {
        if (portalType == PortalTypeEnum.PORTAL) {

            // LambdaUpdateWrapper<PortalUser> userWrapper = new LambdaUpdateWrapper<>();
            // userWrapper.in(PortalUser::getId, offlineUserIds);
            // userWrapper.set(PortalUser::getOnline, Boolean.FALSE);
            // portalUserService.update(userWrapper);

        } else if (portalType == PortalTypeEnum.ADMIN) {

            LambdaUpdateWrapper<AdminUser> userWrapper = new LambdaUpdateWrapper<>();
            userWrapper.in(AdminUser::getId, offlineUserIds);
            userWrapper.set(AdminUser::getOnline, Boolean.FALSE);
            adminUserService.update(userWrapper);

        }
    }
}
