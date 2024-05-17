package com.im.common.service.impl;

import com.im.common.entity.WithdrawOrderLog;
import com.im.common.mapper.WithdrawOrderLogMapper;
import com.im.common.service.WithdrawOrderLogService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 提现订单日志 服务实现类
 *
 * @author Barry
 * @date 2019-10-22
 */
@Service
public class WithdrawOrderLogServiceImpl
        extends MyBatisPlusServiceImpl<WithdrawOrderLogMapper, WithdrawOrderLog>
        implements WithdrawOrderLogService {
}
