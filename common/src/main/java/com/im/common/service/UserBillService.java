package com.im.common.service;

import com.im.common.entity.UserBill;
import com.im.common.entity.enums.UserBillTypeEnum;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户账变 服务类
 *
 * @author Barry
 * @date 2021-06-26
 */
public interface UserBillService extends MyBatisPlusService<UserBill> {
    /**
     * 新增账变
     *
     * @param userId     用户ID
     * @param amount     金额
     * @param balance    数据的余额，应该是增减之后的最新数据
     * @param orderNum   注单号
     * @param type       账变类型
     * @param createTime 订单创建时间
     * @param reportDate 记账日
     * @param remark     备注
     * @return boolean
     */
    boolean addBalanceBill(long userId, BigDecimal amount, BigDecimal balance, String orderNum, UserBillTypeEnum type,
                           LocalDateTime createTime, String reportDate, String remark);
}
