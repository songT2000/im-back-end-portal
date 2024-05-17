package com.im.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.im.common.constant.CommonConstant;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.UserOperationLog;
import com.im.common.mapper.UserOperationLogMapper;
import com.im.common.service.UserOperationLogService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.redis.RedisKeySerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户操作日志 服务实现类
 *
 * @author Barry
 * @date 2020-05-25
 */
@Service
public class UserOperationLogServiceImpl
        extends MyBatisPlusServiceImpl<UserOperationLogMapper, UserOperationLog>
        implements UserOperationLogService {
    private ListOperations<String, String> redisList;
    private RedisKeySerializer keySerializer;

    @Autowired
    public void setRedisList(ListOperations<String, String> redisList) {
        this.redisList = redisList;
    }

    @Autowired
    public void setKeySerializer(RedisKeySerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    @Override
    public void pushQueen(UserOperationLog actionLog) {
        redisList.rightPush(RedisKeyEnum.USER_ACTION_LOG.getVal(), JSON.toJSONString(actionLog));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int processQueen(int count) {
        List<UserOperationLog> userOperationLogs = popupFromRedis(count);
        if (CollectionUtil.isEmpty(userOperationLogs)) {
            return CommonConstant.INT_0;
        }

        saveBatch(userOperationLogs);
        return userOperationLogs.size();
    }

    private List<UserOperationLog> popupFromRedis(int count) {
        Long length = redisList.size(RedisKeyEnum.USER_ACTION_LOG.getVal());
        if (length == null || length <= 0) {
            return new ArrayList<>();
        }

        byte[] keyByte = keySerializer.serialize(RedisKeyEnum.USER_ACTION_LOG.getVal());
        int getLength = count > length ? length.intValue() : count;

        List<Object> callbacks = redisList.getOperations().executePipelined((RedisCallback<List<String>>) connection -> {
            connection.lRange(keyByte, CommonConstant.INT_0, getLength - 1);
            connection.lTrim(keyByte, getLength, -1);

            return null;
        });

        List<UserOperationLog> list = new ArrayList<>();

        for (Object callback : callbacks) {
            if (callback instanceof ArrayList) {
                List<Object> jsonStrList = (ArrayList) callback;

                for (Object o : jsonStrList) {
                    list.add(JSON.parseObject(o.toString(), UserOperationLog.class));
                }
            }
        }

        return list;
    }
}