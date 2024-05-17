package com.im.common.service.impl;

import com.im.common.entity.RechargeOrderLog;
import com.im.common.mapper.RechargeOrderLogMapper;
import com.im.common.service.RechargeOrderLogService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 充值订单日志 服务实现类
 *
 * @author Barry
 * @date 2019-10-22
 */
@Service
public class RechargeOrderLogServiceImpl
        extends MyBatisPlusServiceImpl<RechargeOrderLogMapper, RechargeOrderLog>
        implements RechargeOrderLogService {
}
