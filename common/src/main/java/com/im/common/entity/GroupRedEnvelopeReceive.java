package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 群红包领取记录
 *
 * @author Barry
 * @date 2021-12-20
 */
@Data
@NoArgsConstructor
public class GroupRedEnvelopeReceive extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -6904836115605334958L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 群红包ID
     **/
    private Long envelopeId;

    /**
     * 领取用户ID
     **/
    private Long receiveUserId;

    /**
     * 金额
     */
    private BigDecimal amount;
}
