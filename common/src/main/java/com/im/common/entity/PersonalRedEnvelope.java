package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.PersonalRedEnvelopeStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 个人红包
 *
 * @author Barry
 * @date 2021-12-20
 */
@Data
@NoArgsConstructor
public class PersonalRedEnvelope extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -2652381712164037047L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 订单号
     **/
    private String orderNum;

    /**
     * 用户ID
     **/
    private Long userId;

    /**
     * 领取用户ID
     **/
    private Long receiveUserId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 状态
     */
    private PersonalRedEnvelopeStatusEnum status;

    /**
     * 领取时间
     */
    private LocalDateTime receiveTime;

    /**
     * 过期时间，过期后自动退回
     */
    private LocalDateTime expireTime;

    /**
     * 备注，用来显示在红包上的文字
     */
    private String remark;

    /**
     * IM的消息ID，用于后续更新发红包消息的状态
     */
    private String msgKey;
}
