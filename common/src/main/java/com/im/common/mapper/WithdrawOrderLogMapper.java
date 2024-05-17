package com.im.common.mapper;

import com.im.common.entity.WithdrawOrderLog;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 提现订单日志 Mapper 接口
 *
 * @author Barry
 * @date 2020-05-23
 */
@Repository
public interface WithdrawOrderLogMapper extends MyBatisPlusMapper<WithdrawOrderLog> {
}
