package com.im.common.service;

import com.im.common.entity.tim.TimUserLoginLog;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户每日登陆信息服务
 */
public interface TimUserLoginLogService extends MyBatisPlusService<TimUserLoginLog> {

    /**
     * 保存用户活跃信息
     */
    void put(TimUserLoginLog loginLog);

    /**
     * 查询某天的活跃用户信息
     */
    List<TimUserLoginLog> getByDate(LocalDate date);

}
