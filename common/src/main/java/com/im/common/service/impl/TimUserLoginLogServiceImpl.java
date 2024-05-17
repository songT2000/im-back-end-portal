package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.im.common.entity.tim.TimUserLoginLog;
import com.im.common.mapper.TimUserLoginLogMapper;
import com.im.common.service.TimUserLoginLogService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimUserLoginLogServiceImpl extends MyBatisPlusServiceImpl<TimUserLoginLogMapper, TimUserLoginLog>
        implements TimUserLoginLogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void put(TimUserLoginLog loginLog) {
        //先删除用户当天之前的记录再创建
        LambdaQueryWrapper<TimUserLoginLog> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(TimUserLoginLog::getUserId, loginLog.getUserId())
                .eq(TimUserLoginLog::getLoginDate, loginLog.getLoginDate());
        remove(queryWrapper);

        save(loginLog);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<TimUserLoginLog> getByDate(LocalDate date) {
        return lambdaQuery().eq(TimUserLoginLog::getLoginDate, date).list();
    }
}
