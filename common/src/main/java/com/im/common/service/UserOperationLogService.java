package com.im.common.service;

import com.im.common.entity.UserOperationLog;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 用户操作日志 服务类
 *
 * @author Barry
 * @date 2020-05-25
 */
public interface UserOperationLogService extends MyBatisPlusService<UserOperationLog> {
    /**
     * 把日志存储到redis队列，后续由调度任务统一处理
     *
     * @param log UserOperationLog
     */
    void pushQueen(UserOperationLog log);

    /**
     * 处理redis队列中待处理的数据
     *
     * @param count 最多处理多少条
     * @return 处理成功的条数
     */
    int processQueen(int count);
}
