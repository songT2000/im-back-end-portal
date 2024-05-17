package com.im.common.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Barry
 * @date 2021-03-02
 */
@Service
public class MqttServiceImpl implements MqttService {
    private static final Log LOG = LogFactory.get();

    private SysConfigCache sysConfigCache;
    private ListOperations<String, String> redisList;
    private HashOperations<String, String, String> redisHash;
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setRedisList(ListOperations<String, String> redisList) {
        this.redisList = redisList;
    }

    @Autowired
    public void setRedisHash(HashOperations<String, String, String> redisHash) {
        this.redisHash = redisHash;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
