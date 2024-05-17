package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.im.common.entity.UserBill;
import com.im.common.entity.enums.UserBillTypeEnum;
import com.im.common.mapper.UserBillMapper;
import com.im.common.service.UserBillService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户账变 服务实现类
 *
 * @author Barry
 * @date 2021-08-21
 */
@Service
public class UserBillServiceImpl
        extends MyBatisPlusServiceImpl<UserBillMapper, UserBill>
        implements UserBillService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addBalanceBill(long userId, BigDecimal amount, BigDecimal balance, String orderNum, UserBillTypeEnum type,
                                  LocalDateTime createTime, String reportDate, String remark) {
        UserBill bill = new UserBill();
        bill.setId(IdWorker.getId());
        bill.setUserId(userId);
        bill.setType(type);
        bill.setAmount(amount);
        bill.setBalance(balance);
        bill.setOrderNum(orderNum);
        bill.setReportDate(reportDate);
        bill.setRemark(remark);
        bill.setCreateTime(createTime);
        bill.setUpdateTime(createTime);
        return save(bill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean fastSaveBatch(List<UserBill> list, int batchSize) {
        if (CollectionUtil.isEmpty(list)) {
            return true;
        }

        // 原生SQL
        String sql = "INSERT INTO `user_bill` (`id`, `user_id`, `type`, `amount`, `balance`, `order_num`, `report_date`," +
                "`remark`, `create_time`, `update_time`) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // 一个批次执行1W条
        List<List<UserBill>> batchList = CollectionUtil.splitBySubList(list, batchSize);

        for (List<UserBill> subBatchList : batchList) {
            if (CollectionUtil.isEmpty(subBatchList)) {
                continue;
            }
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    // 注意这里下标是从1开始
                    int index = 1;
                    UserBill one = subBatchList.get(i);
                    ps.setLong(index++, one.getId());
                    ps.setLong(index++, one.getUserId());
                    ps.setString(index++, one.getType().getVal());
                    ps.setBigDecimal(index++, one.getAmount());
                    ps.setBigDecimal(index++, one.getBalance());
                    ps.setString(index++, one.getOrderNum());
                    ps.setString(index++, one.getReportDate());
                    ps.setString(index++, one.getRemark());
                    ps.setString(index++, DateTimeUtil.toDateTimeStr(one.getCreateTime()));
                    LocalDateTime updateTime = Optional.ofNullable(one.getUpdateTime()).orElse(one.getCreateTime());
                    ps.setString(index++, DateTimeUtil.toDateTimeStr(updateTime));
                }

                @Override
                public int getBatchSize() {
                    return subBatchList.size();
                }
            });
        }

        return true;
    }
}
