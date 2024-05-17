package com.im.common.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.I18nLanguage;
import com.im.common.entity.PortalUser;
import com.im.common.entity.SysNotice;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.entity.tim.TimFriend;
import com.im.common.service.I18nLanguageService;
import com.im.common.service.SysNoticeService;
import com.im.common.service.TimFriendService;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.TimFriendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

/**
 * 好友关系缓存
 *
 * @author max.stark
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.TIM_FRIEND, redis = true, local = true)
@Component
public class TimFriendCache implements BaseCacheHandler {
    private TimFriendService timFriendService;
    private ListOperations<String, String> listOperations;
    private RedisTemplate redisTemplate;

    private Map<Long, List<Long>> LOCAL_ID_FRIENDS_CACHE = new HashMap<>();



    @Override
    public void reloadRedis() {
        loadAndCache(this::resolveListForRedis);
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    /**
     * 查询用户好友ID列表
     * @param id
     * @return
     */
    public List<Long> getFriendByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_FRIENDS_CACHE.get(id);
    }

    /**
     * 查询某个用户好友
     * @param id
     *
     * @return List<TimFriendVO>
     */
    public List<TimFriendVO> listFromRedis(Long id) {
        List<String> stringList = listOperations.range(formatIdKey(id), 0, -1);
        if (CollectionUtil.isEmpty(stringList)) {
            return new ArrayList<>();
        }
        return CollectionUtil.toList(stringList, s -> JSONObject.parseObject(s, TimFriendVO.class));
    }

    /**
     * 查询好友分页
     * @param id
     *
     * @return List<TimFriendVO>
     */
    public List<TimFriendVO> pageFromRedis(Long id, long start, long end) {
        List<String> stringList = listOperations.range(formatIdKey(id), start, end);
        if (CollectionUtil.isEmpty(stringList)) {
            return new ArrayList<>();
        }
        return CollectionUtil.toList(stringList, s -> JSONObject.parseObject(s, TimFriendVO.class));
    }


    /**
     * 加载并缓存数据
     *
     * @param consumer 处理方法
     */
    private void loadAndCache(Consumer<List<TimFriend>> consumer) {
        List<TimFriend> list = timFriendService.list(
                new LambdaQueryWrapper<TimFriend>()
                        .orderByAsc(TimFriend::getAliasName));

        Optional.ofNullable(list).ifPresent(consumer);
    }

    /**
     * 解析并缓存数据
     *
     * @param list 好友列表
     */
    private void resolveListForRedis(List<TimFriend> list) {
        Set<Long> ids = CollectionUtil.toSet(list, TimFriend::getUserId);
        for (Long id : ids) {
            //删除原来数据
            redisTemplate.delete(formatIdKey(id));

            List<TimFriendVO> voList = CollectionUtil.toList(list,
                    e -> e.getUserId().equals(id),
                    TimFriendVO::new);
            List<String> strings = CollectionUtil.toList(voList, s -> JSONObject.toJSONString(s));
            if (CollectionUtil.isEmpty(strings)) {
                continue;
            }
            listOperations.rightPushAll(formatIdKey(id), strings);
        }
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        // 只查需要的字段，避免每次加载太多数据
        List<TimFriend> list = timFriendService.lambdaQuery()
                .select(TimFriend::getUserId, TimFriend::getFriendUserId)
                .list();

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param list List
     */
    private void resolveList(List<TimFriend> list) {
        Map<Long, List<Long>> friendMaps = new HashMap<>();
        Set<Long> ids = CollectionUtil.toSet(list, TimFriend::getUserId);
        for (Long id : ids) {
            List<TimFriendVO> voList = CollectionUtil.toList(list,
                    e -> e.getUserId().equals(id),
                    TimFriendVO::new);
            List<Long> friends = CollectionUtil.toList(voList, TimFriendVO::getFriendUserId);
            if (CollectionUtil.isEmpty(friends)) {
                continue;
            }
            friendMaps.put(id, friends);
        }

        LOCAL_ID_FRIENDS_CACHE = friendMaps;
    }

    private String formatIdKey(Long id) {
        return StrUtil.format(RedisKeyEnum.TIM_FRIEND.getVal(), id);
    }


    @Autowired
    public void setListOperations(ListOperations<String, String> listOperations) {
        this.listOperations = listOperations;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setTimFriendService(TimFriendService timFriendService) {
        this.timFriendService = timFriendService;
    }
}
