package com.im.common.mapper;

import com.im.common.entity.GroupRedEnvelope;
import com.im.common.entity.enums.GroupRedEnvelopeStatusEnum;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 群红包 Mapper 接口
 *
 * @author Barry
 * @date 2021-12-20
 */
@Repository
public interface GroupRedEnvelopeMapper extends MyBatisPlusMapper<GroupRedEnvelope> {
    /**
     * @param id     ID
     * @param amount 金额
     */
    boolean addReceived(@Param("id") long id, @Param("amount") BigDecimal amount, @Param("status") GroupRedEnvelopeStatusEnum status);
}
