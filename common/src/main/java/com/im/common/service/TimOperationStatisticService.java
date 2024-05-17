package com.im.common.service;

import com.im.common.entity.tim.TimOperationStatistic;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 运营数据服务
 */
public interface TimOperationStatisticService extends MyBatisPlusService<TimOperationStatistic> {

    /**
     * 同步运营数据
     */
    void sync();

}
