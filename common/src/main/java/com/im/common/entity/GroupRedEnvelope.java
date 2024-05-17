package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.GroupRedEnvelopeStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 群红包
 *
 * @author Barry
 * @date 2021-12-20
 */
@Data
@NoArgsConstructor
public class GroupRedEnvelope extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1750125027323643116L;

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
     * 组ID
     **/
    private Long groupId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 拆分个数
     */
    private Integer num;

    /**
     * 状态
     */
    private GroupRedEnvelopeStatusEnum status;

    /**
     * 已领取金额
     */
    private BigDecimal receivedAmount;

    /**
     * 已领取个数
     */
    private Integer receivedNum;

    /**
     * 过期时间，过期后自动退回
     */
    private LocalDateTime expireTime;

    /**
     * 备注，用来显示在红包上的文字
     */
    private String remark;

    /**
     * IM的群消息ID，用于后续更新发红包消息的状态
     **/
    private Long msgSeq;
}
