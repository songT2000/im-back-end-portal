package com.im.common.mapper;

import com.im.common.entity.Bank;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 银行 Mapper 接口
 *
 * @author Barry
 * @date 2020-05-23
 */
@Repository
public interface BankMapper extends MyBatisPlusMapper<Bank> {
}
