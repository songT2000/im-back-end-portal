package com.im.common.mapper;

import com.im.common.entity.WithdrawOrder;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 提现订单 Mapper 接口
 *
 * @author Barry
 * @date 2021-09-29
 */
@Repository
public interface WithdrawOrderMapper extends MyBatisPlusMapper<WithdrawOrder> {
}
